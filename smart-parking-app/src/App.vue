<template>
  <div>
    <nav-bar-general />
    <router-view />
  </div>
</template>

<script>
import NavBarGeneral from "./components/NavBarGeneral.vue";

export default {
  components: { NavBarGeneral },
};
</script>

<style></style>
