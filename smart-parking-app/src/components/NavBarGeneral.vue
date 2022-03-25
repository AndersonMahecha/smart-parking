<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="https://www.usergioarboleda.edu.co">
        <img
          src="https://www.usergioarboleda.edu.co/wp-content/uploads/2015/03/USergioArboleda-LogoHead.png"
        />
      </a>

      <a
        role="button"
        class="navbar-burger"
        aria-label="menu"
        aria-expanded="false"
        data-target="navbarBasicExample"
      >
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div id="navbarBasicExample" class="navbar-menu">
      <div class="navbar-start">
        <router-link :to="{ name: 'Home' }" class="navbar-item"
          >Inicio</router-link
        >
        <router-link :to="{ name: 'Users' }" class="navbar-item"
          >Usuarios</router-link
        >
      </div>

      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <a class="button" @click="connect">{{
              port ? "Desconectar" : "Conectar"
            }}</a>
            <a class="button is-primary">
              <strong>Sign up</strong>
            </a>
            <a class="button is-light"> Log in </a>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  data() {
    return {
      port: null,
    };
  },
  methods: {
    async connect() {
      if (!this.port) {
        this.port = await navigator.serial.requestPort();
        this.port
          .open({ baudRate: 9600 })
          .then(() => this.$emit("connected", this.port));
      } else {
        this.port.close();
        this.port = null;
      }
    },
  },
};
</script>

<style lang="scss">
$navbar-item-img-max-height: 500;
</style>
