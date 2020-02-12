<template>
    <b-container>
        <b-form @submit="onSubmit" @reset="onReset">
            <b-form-group label-cols="4" label-cols-lg="2" label="책명" label-for="input-name" label-size="sm">
                <b-form-input id="input-name" v-model="form.name" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="소개" label-for="input-intro" label-size="sm">
                <b-form-input id="input-intro" v-model="form.intro" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="저자" label-for="input-author" label-size="sm">
                <b-form-input id="input-author" v-model="form.author" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="출판사" label-for="input-publisher" label-size="sm">
                <b-form-input id="input-publisher" v-model="form.publisher" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="가격범위" label-for="input-price" label-size="sm">
                <b-input-group id="input-price">
                    <b-form-input id="input-price-lower" v-model="form.priceLower" size="sm" type="number"></b-form-input>
                     ~
                    <b-form-input id="input-price-upper" v-model="form.priceUpper" size="sm" type="number"></b-form-input>
                </b-input-group>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="출판일범위" label-for="input-publishDate" label-size="sm">
                <b-input-group id="input-publishDate">
                    <b-form-input id="input-fromDate" v-model="form.fromDate" size="sm" type="date"></b-form-input>
                    ~
                    <b-form-input id="input-toDate" v-model="form.toDate" size="sm" type="date"></b-form-input>
                </b-input-group>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="관심여부" label-for="radio-favorite" label-size="sm">
                <b-form-radio-group id="radio-favorite" v-model="form.favorite" name="radio-sub-component">
                    <b-form-radio value="ALL">전체</b-form-radio>
                    <b-form-radio value="true">관심</b-form-radio>
                    <b-form-radio value="false">관심없음</b-form-radio>
                </b-form-radio-group>
            </b-form-group>

            <b-button type="reset" variant="danger">초기화</b-button>
        </b-form>
    </b-container>
</template>

<script>
    export default {
        name: "bookForm",
        data() {
            return {
                form: {
                    name: '',
                    intro: '',
                    author: '',
                    publisher: '',
                    priceLower: 0,
                    priceUpper: 1000000,
                    fromDate: '1000-01-01',
                    toDate: '2999-12-01',
                    favorite: 'ALL'
                },
            }
        },
        watch: {
            computedForm: {
                deep: true,
                handler() {
                    this.$emit('filterList', Object.assign({}, this.form));
                }
            },
        },
        computed: {
            computedForm: function () {
                return Object.assign({}, this.form);
            }
        },
        mounted: function () {
            this.$emit('searchBooks');
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();
            },
            onReset(evt) {
                evt.preventDefault();

                // Reset our form values
                this.form.name = '';
                this.form.intro = '';
                this.form.author = '';
                this.form.publisher = '';
                this.form.priceLower = 0;
                this.form.priceUpper = 1000000;
                this.form.fromDate = '';
                this.form.toDate = '';
                this.form.favorite = 'ALL';
            },
        }
    }
</script>