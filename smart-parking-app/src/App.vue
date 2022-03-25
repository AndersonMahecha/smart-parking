<template>
  <div>
    <nav-bar-general @connected="saveConnection" />
    <router-view v-bind:port="port" />
  </div>
</template>

<script>
import NavBarGeneral from "./components/NavBarGeneral.vue";

export default {
  data() {
    return {
      port: null,
    };
  },
  components: { NavBarGeneral },
  methods: {
    saveConnection(port) {
      this.port = port;
    },
  },
};
</script>

<style></style>
