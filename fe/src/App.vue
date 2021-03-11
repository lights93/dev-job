<template>
  <div id="app">
    <recruit-nav :user="user.name"></recruit-nav>
    <router-view :userRecruits="user.recruits" :userBooks="user.books"/>
  </div>
</template>

<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

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
  // @ is an alias to /src
  import RecruitNav from '@/components/RecruitNav.vue'

  export default {
    name: 'app',
    components: {
      RecruitNav
    },
    data: function () {
      return {
        user: {
          "name": undefined,
          "recruits": [],
          "books": [],
        }
      }
    },
    mounted: function () {
      this.getUser();
    },
    methods: {
      getUser: function () {
        const vm = this;

        this.axios.get("/api/user")
                .then((result) => {
                  vm.user = result.data;
                });
      }
    }
  }
</script>
