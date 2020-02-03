<template>
    <b-container fluid>
        <b-table striped hover :items="items" :fields="fields" responsive="sm" :sort-by.sync="sortBy" :sort-desc.sync="sortDesc">
            <template v-slot:cell(link)="data">
                <b-link :href="data.item.link" target="_blank">
                    Link
                </b-link>
            </template>
            <template v-slot:cell(favorite)="data">
                <b-button size="sm" pill variant="outline-primary" @click="clickFavorite(data.index)">
                    <b-icon icon="star-fill" font-scale="1" v-if="data.item.favorite"></b-icon>
                    <b-icon icon="star" font-scale="1" v-else></b-icon>
                </b-button>

            </template>
        </b-table>
    </b-container>
</template>

<script>
    export default {
        name: 'recruitList',
        props: ["recruits"],
        data() {
            return {
                fields: [
                    {key: "company", sortable: true},
                    {key: "title", sortable: true},
                    {key: "jobType", sortable: true},
                    {key: "term", sortable: true},
                    {key: "companyType", sortable: true},
                    {key: "tags", sortable: false},
                    {key: "link", sortable: false},
                    {key: "favorite", sortable: true}
                ],
                items: [],
                sortBy: "jobType",
                sortDesc: false

            }
        },
        watch: {
            recruits: function (newVal) {
                this.items = newVal;
            }
        },
        methods: {
            clickFavorite: function (index) {
                var item = this.items[index];
                item.favorite = !item.favorite;

                this.axios.put("/api/recruits", item)
                    .then(() => {
                        this.items.splice(index, 1, item);
                    })
                    .catch(error => {
                        alert(error);
                    });
            }
        }
    }
</script>