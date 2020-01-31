<template>
    <b-container fluid>
        <b-table striped hover :items="items" :fields="fields" show-empty empty-text="조회 결과가 없습니다." responsive="sm">
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
                fields: ["company", "title", "link", "jobType", "term", "companyType", "tags", "favorite"],
                items: []
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
                this.items.splice(index, 1, item);

                this.axios.put("/api/recruits", this.items[index])
                    .then(() => {
                        // do nothing
                    })
                    .catch(error => {
                        alert(error);
                    });
            }
        }
    }
</script>