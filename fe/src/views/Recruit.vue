<template>
    <div>
        <recruit-form :companies="companies" @searchRecruits="searchRecruits"/>
        <br/>
        <recruit-list :recruits="recruits"/>
    </div>
</template>

<script>
    import RecruitForm from "@/components/RecruitForm";
    import RecruitList from "@/components/RecruitList";

    export default {
        name: 'Recruit',
        components: {RecruitForm, RecruitList},
        data: function() {
            return {
                recruits: [],
                companies: []
            }
        },
        mounted: function () {
            this.getCompanies();
        },
        methods: {
            searchRecruits: function (params) {
                const vm = this;
                this.axios.get("/api/crawl/" + params.company)
                    .then((result) => {
                        vm.recruits = result.data;
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