<template>
    <div>
        <recruit-form :companies="companies" @searchRecruits="searchRecruits" @filterList="filterList"/>
        <br/>
        <recruit-list :recruits="filteredRecruits"/>
    </div>
</template>

<script>
    import RecruitForm from "@/components/RecruitForm";
    import RecruitList from "@/components/RecruitList";

    export default {
        name: 'Recruit',
        components: {RecruitForm, RecruitList},
        data: function () {
            return {
                recruits: [],
                filteredRecruits: [],
                companies: []
            }
        },
        mounted: function () {
            this.getCompanies();
        },
        methods: {
            searchRecruits: function (params) {
                const vm = this;
                vm.axios.get("/api/recruits/" + params.company)
                    .then((result) => {
                        vm.recruits = result.data;
                        vm.filterList(params);
                    });
            },

            filterList: function (params) {
                const favorite = params['favorite'];
                const queryTerm = new Date(params['term']);

                delete params['company'];
                delete params['favorite'];
                delete params['term'];

                Object.keys(params).forEach((key) => (params[key].trim() === '') && delete params[key]);

                this.filteredRecruits = this.recruits
                    .filter(recruit => {
                        // company: 'ALL',
                        //     title: '',
                        //     jobType: '',
                        //     tags: '',
                        //     term: '',
                        //     favorite: ''
                        const keys = Object.keys(params);
                        for (const key of keys) {
                            if (!recruit[key]) {
                                return false;
                            }

                            const regex = new RegExp(params[key], "i");

                            if (!regex.test(recruit[key])) {
                                return false;
                            }
                        }

                        const term = new Date(recruit['term']);

                        if(term > queryTerm) {
                            return false;
                        }

                        return favorite === 'ALL' || favorite === recruit['favorite'].toString();


                    });
            },

            getCompanies: function () {
                const vm = this;

                this.axios.get("/api/companies/")
                    .then((result) => {
                        vm.companies = result.data;
                    });
            }
        }
    }
</script>