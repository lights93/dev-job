<template>
    <b-container fluid>
        <b-table striped hover :items="recruits" responsive="sm">
            <!--<template v-slot:cell(link)="data">-->
                <!--<b-link :href="data.item.link" target="_blank">-->
                    <!--Link-->
                <!--</b-link>-->
            <!--</template>-->
        </b-table>
    </b-container>
</template>

<script>
    export default {
        name: 'recruitList',
        props: ["recruits"],
    }
</script>