<template>
    <div>
        <recruit-form @searchRecruits="searchRecruits"/>
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
                recruits: []
            }
        },
        methods: {
            searchRecruits: function (params) {
                const vm = this;
                this.axios.get("/api/crawl/" + params.company.toLowerCase())
                    .then((result) => {
                        vm.recruits = result.data;
                    })
            }
        }
    }
</script>