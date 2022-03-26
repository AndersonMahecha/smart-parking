<template>
  <div class="container has-text-left">
    <h1 class="title">Registro de usuario</h1>
    <user-view @user-updated="updateModel" />

    <div class="columns">
      <div class="column is-four-fifths">
        <label class="label">Carnet</label>
        <div class="field has-addons">
          <div class="control">
            <input
              type="text"
              class="input is-primary is-fullwidth"
              placeholder="ID carnet"
              v-model="client.licenseCode"
              disabled
            />
          </div>
          <button
            class="button has-tooltip-multiline"
            :class="port ? 'is-success ' : 'is-danger'"
            @click="reading = true"
            data-tooltip="Recuerda primero validar que el lector este conectado"
          >
            <span class="icon"> <i class="fa-solid fa-id-card"></i></span>
            <span>Leer</span>
          </button>
        </div>
      </div>
    </div>
    <button class="button is-success" @click="createUser">Crear usuario</button>
  </div>
</template>
<script>
import UserView from "../views/UserView.vue";
import UsersApi from "@/api/UsersApi";
import { readTagId } from "@/SerialScanner";
export default {
  components: { UserView },
  props: ["port"],
  data() {
    return {
      reading: false,
      client: {
        licenseCode: "",
        user: {},
      },
    };
  },
  methods: {
    updateModel(user) {
      this.client.user = user;
    },

    createUser() {
      UsersApi.createUser(this.client).finally(() => {
        this.$router.replace({ name: "Users" });
      });
    },
  },
  watch: {
    reading() {
      (async () => {
        if (this.reading) {
          if (!this.port) {
            return;
          }
          this.client.licenseCode = "";
          this.client.licenseCode = await readTagId(this.port);
          this.reading = false;
        }
      })();
    },
  },
};
</script>
