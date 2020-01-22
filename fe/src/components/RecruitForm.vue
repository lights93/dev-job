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

            <b-form-group label-cols="4" label-cols-lg="2" label="타입" label-for="input-type" label-size="sm">
                <b-form-input id="input-type" v-model="form.jobType" size="sm"></b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label="링크" label-for="input-link" label-size="sm">
                <b-form-input id="input-link" v-model="form.link" size="sm"></b-form-input>
            </b-form-group>

            <!--<b-form-group label-cols="4" label-cols-lg="2" label="종료일" label-for="input-date" label-size="sm">-->
                <!--<b-form-input id="input-date" v-model="form.term" size="sm" type="date"></b-form-input>-->
            <!--</b-form-group>-->

            <b-button type="submit" variant="primary">검색</b-button>
            <b-button type="reset" variant="danger">초기화</b-button>
        </b-form>
    </b-container>
</template>

<script>
    export default {
        name: "recruitForm",
        data() {
            return {
                form: {
                    company: 'All',
                    title: '',
                    jobType: '',
                    link: ''
                    // term: ''
                },
                companies: ['All', 'Naver', 'Kakao', 'Line', 'Woowa'],
            }
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();
                this.$emit('searchRecruits', this.form);
            },
            onReset(evt) {
                evt.preventDefault();
                // Reset our form values
                this.form.company = 'All';
                this.form.jobType = '';
                this.form.title = '';
                this.form.link = '';
                // this.form.term = '';
            },
        }
    }
</script>