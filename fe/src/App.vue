<template>
  <div id="app">
    <recruit-nav></recruit-nav>
    <router-view/>
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
    }
  }
</script>
