<template>
    <b-container fluid>
        <b-table striped hover :items="items" :fields="fields" responsive="sm" :sort-by.sync="sortBy"
                 :sort-desc.sync="sortDesc">
            <template v-slot:cell(link)="data">
                <b-link :href="data.item.link" target="_blank">
                    Link
                </b-link>
            </template>
            <template v-slot:cell(favorite)="data">
                <b-button size="sm" pill variant="outline-primary" @click="clickFavorite(data)">
                    <b-icon icon="star-fill" font-scale="1" v-if="data.item.favorite"></b-icon>
                    <b-icon icon="star" font-scale="1" v-else></b-icon>
                </b-button>

            </template>
        </b-table>
    </b-container>
</template>

<script>
    export default {
        name: 'bookList',
        props: ["books"],
        data() {
            return {
                fields: [
                    {key: "name", sortable: true},
                    {key: "intro", sortable: false},
                    {key: "author", sortable: false},
                    {key: "publisher", sortable: false},
                    {key: "price", sortable: true},
                    {key: "publishDate", sortable: true},
                    {key: "link", sortable: false},
                    {key: "favorite", sortable: true}
                ],
                items: [],
                sortBy: "favorite",
                sortDesc: true

            }
        },
        watch: {
            books: function (newVal) {
                this.items = newVal;
            }
        },
        methods: {
            clickFavorite: function (data) {
                var index = this.items.indexOf(data.item);
                var item = this.items[index];
                item.favorite = !item.favorite;

                this.axios.put("/api/books", item)
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