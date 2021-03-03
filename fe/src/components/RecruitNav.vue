<template>
    <b-navbar toggleable="lg" type="dark" variant="dark" id="nav">
        <b-navbar-brand href="#">devJob</b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
            <b-navbar-nav>
                <b-nav-item to="/">Home</b-nav-item>
                <b-nav-item to="/recruit">Recruit</b-nav-item>
                <b-nav-item to="/book">Book</b-nav-item>
            </b-navbar-nav>

            <!-- Right aligned nav items -->
            <b-navbar-nav class="ml-auto">
                <b-nav-item v-if="user === undefined"><b-button size="sm" @click="signIn()">Sign in</b-button></b-nav-item>
                <b-nav-item v-else><b-button size="sm" @click="signOut()">Sign out</b-button></b-nav-item>
            </b-navbar-nav>
        </b-collapse>
    </b-navbar>
</template>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
#nav {
    margin-bottom: 30px;

    a {
        font-weight: bold;
        /*color: #2c3e50;*/

        &.router-link-exact-active {
            color: #42b983;
        }
    }
}
</style>

<script>
    export default {
        name: 'recruitNav',
        props: ["user"],
        // data: function () {
        //     return {
        //         user: undefined
        //     }
        // },
        // mounted: function () {
            // this.getUser();
        // },
        methods: {
            // getUser: function () {
            //     const vm = this;
            //
            //     this.axios.get("/api/user")
            //         .then((result) => {
            //             console.log(result);
            //             vm.user = result.data.name;
            //             console.log(vm.user);
            //
            //         })
            // },

            signIn: function () {
                location.href="/auth/login";
            },

            signOut: function () {
                location.href="/logout";
            }
        }
    }
</script>

