<template>
  <nav-bar-general />
  <entry-register />
</template>

<script>
import EntryRegister from "./components/EntryRegister.vue";
import NavBarGeneral from "./components/NavBarGeneral.vue";

export default {
  name: "App",
  components: {
    EntryRegister,
    NavBarGeneral,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
