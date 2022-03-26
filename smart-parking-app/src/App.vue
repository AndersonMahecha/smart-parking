<template>
  <div>
    <nav-bar-general @connected="saveConnection" />
    <router-view :port="port" />
  </div>
</template>

<script>
import NavBarGeneral from "./components/NavBarGeneral.vue";

export default {
  data() {
    return {
      port: null,
      routerId: true,
    };
  },
  components: { NavBarGeneral },
  methods: {
    saveConnection(port) {
      this.port = port;
    },
  },
  watch: {},
};
</script>

<style></style>
