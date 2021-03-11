<template>
    <div>
        <book-form @searchBooks="searchBooks" @filterList="filterList"/>
        <br/>
        <book-list :books="filteredBooks"/>
    </div>
</template>

<script>
    import BookForm from "@/components/BookForm";
    import BookList from "@/components/BookList";

    export default {
        name: 'Book',
        components: {BookForm, BookList},
        data: function () {
            return {
                books: [],
                filteredBooks: []
            }
        },
        props: ["userBooks"],
        methods: {
            searchBooks: function () {
                const vm = this;
                vm.axios.get("/api/books/")
                    .then((result) => {
                        vm.books = result.data;
                        vm.updateFavorite();
                        vm.filteredBooks = vm.books;
                    });
            },

            updateFavorite: function () {
                if(!this.userBooks) {
                    return;
                }
                const map = new Map();

                this.userBooks.forEach(item => {
                    map.set(item.id, item.favorite);
                });

                this.books.map((item) => {
                    let key = item.id;
                    if(map.has(key)) {
                        item.favorite = map.get(key);
                    } else {
                        item.favorite = 0;
                    }
                })
            },

            filterList: function (params) {
                const favorite = params['favorite'];
                const fromDate = new Date(params['fromDate']);
                const toDate = new Date(params['toDate']);
                const priceLower = params['priceLower'];
                const priceUpper = params['priceUpper'];

                delete params['favorite'];
                delete params['fromDate'];
                delete params['toDate'];
                delete params['priceLower'];
                delete params['priceUpper'];

                Object.keys(params).forEach((key) => (params[key].trim() === '') && delete params[key]);

                this.filteredBooks = this.books
                    .filter(book => {
                        const keys = Object.keys(params);
                        for (const key of keys) {
                            if (!book[key]) {
                                return false;
                            }

                            const regex = new RegExp(params[key], "i");

                            if (!regex.test(book[key])) {
                                return false;
                            }
                        }

                        const date = new Date(book['publishDate']);

                        if (date < fromDate || date > toDate) {
                            return false;
                        }

                        if(book['price'] < priceLower || book['price'] > priceUpper) {
                            return false;
                        }

                        return favorite === 'ALL' || favorite === book['favorite'].toString();
                    });
            },
        }
    }
</script>