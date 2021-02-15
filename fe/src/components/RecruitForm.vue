<template>
    <b-container>
        <b-form @submit="onSubmit" @reset="onReset">
            <b-form-group  label-cols="4" label-cols-lg="2" label="회사" label-for="input-company" label-size="sm">
                <b-form-select
                        id="input-company"
                        v-model="form.company"
                        :options="companies"
                        size="sm"
                        required
                ></b-form-select>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="제목" label-for="input-title" label-size="sm">
                <b-form-input id="input-title" v-model="form.title" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="타입" label-for="input-jobType" label-size="sm">
                <b-form-input id="input-jobType" v-model="form.jobType" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="계열사" label-for="input-companyType" label-size="sm">
                <b-form-input id="input-companyType" v-model="form.companyType" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="태그" label-for="input-tag" label-size="sm">
                <b-form-input id="input-tag" v-model="form.tags" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="종료일" label-for="input-date" label-size="sm">
                <b-form-input id="input-date" v-model="form.term" size="sm" type="date"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="관심여부" label-for="radio-favorite" label-size="sm">
                <b-form-radio-group id="radio-favorite" v-model="form.favorite" name="radio-sub-component">
                    <b-form-radio value="ALL">전체</b-form-radio>
                    <b-form-radio value="1">관심</b-form-radio>
                    <b-form-radio value="0">본적없음</b-form-radio>
                    <b-form-radio value="-1">관심없음</b-form-radio>
                </b-form-radio-group>
            </b-form-group>


            <b-button type="submit" variant="primary">검색</b-button>
            <b-button type="reset" variant="danger">초기화</b-button>
        </b-form>
    </b-container>
</template>

<script>
    export default {
        name: "recruitForm",
        props: ["companies"],
        data() {
            return {
                form: {
                    company: 'ALL',
                    title: '',
                    jobType: '',
                    companyType: '',
                    tags: '',
                    term: '',
                    favorite: 'ALL'
                },
                oldCompany: 'ALL'
            }
        },
        computed: {
            computedForm: function() {
                return Object.assign({}, this.form);
            }
        },
        mounted: function () {
            this.$emit('searchRecruits', Object.assign({}, this.form));
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();
                if(this.form.company !== this.oldCompany) {
                    this.$emit('searchRecruits', Object.assign({}, this.form));
                    this.oldCompany = this.form.company;
                } else {
                    this.$emit('filterList', Object.assign({}, this.form));
                }
            },
            onReset(evt) {
                evt.preventDefault();
                // Reset our form values
                this.form.company = 'ALL';
                this.form.jobType = '';
                this.form.companyType = '';
                this.form.title = '';
                this.form.tags = '';
                this.form.term = '';
                this.form.favorite = 'ALL';
            },
        }
    }
</script>