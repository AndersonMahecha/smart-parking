<template>
  <div class="container has-text-left">
    <h1 class="title">Registro de usuario</h1>
    <user-view @user-updated="updateModel" />

    <div class="columns">
      <div class="column is-four-fifths">
        <div class="field">
          <label class="label">Carnet</label>
          <div class="control">
            <input
              type="text"
              class="input is-primary"
              placeholder="ID carnet"
              v-model="client.licenseCode"
              disabled
            />
          </div>
        </div>
      </div>
    </div>
    <button class="button is-success" @click="createUser">Crear usuario</button>
  </div>
</template>
<script>
import UserView from "../views/UserView.vue";
import UsersApi from "@/UsersApi";
export default {
  components: { UserView },
  data() {
    return {
      client: {
        licenseCode: this.uuidv4(),
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

    uuidv4() {
      return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
        (
          c ^
          (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (c / 4)))
        ).toString(16)
      );
    },
  },
};
</script>
