<template>
  <div class="columns">
    <div class="column is-four-fifths">
      <div class="field">
        <label class="label">Nombre</label>
        <div class="control has-icons-left">
          <input
            class="input is-primary"
            type="text"
            placeholder="Nombre"
            v-model="user.firstName"
            @change="modelUpdated()"
          />
          <span class="icon is-small is-left">
            <i class="fas fa-user"></i>
          </span>
        </div>
      </div>

      <div class="field">
        <label class="label">Primer apellido</label>
        <div class="control has-icons-left">
          <input
            type="text"
            class="input is-primary"
            placeholder="Primer apellido"
            v-model="user.middleName"
            @change="modelUpdated()"
          />
          <span class="icon is-small is-left">
            <i class="fas fa-user"></i>
          </span>
        </div>
      </div>

      <div class="field">
        <label class="label">Segundo apellido</label>
        <div class="control has-icons-left">
          <input
            type="text"
            class="input is-primary"
            placeholder="Segundo apellido"
            v-model="user.lastName"
            @change="modelUpdated()"
          />
          <span class="icon is-small is-left">
            <i class="fas fa-user"></i>
          </span>
        </div>
      </div>

      <div class="field">
        <label class="label">Tipo de documento</label>
        <div class="control">
          <div class="select is-primary">
            <select v-model="user.documentType" @change="modelUpdated()">
              <option value="" selected disabled hidden>Seleccione uno</option>
              <option value="CC">Cedula</option>
              <option value="TI">Tarjeta de identidad</option>
              <option value="CE">Cedula de extranjeria</option>
            </select>
          </div>
        </div>
      </div>

      <div class="field">
        <label class="label">Numero de documento</label>
        <div class="control">
          <input
            type="number"
            class="input is-primary"
            placeholder="Numero de documento"
            v-model="user.documentNumber"
            @change="modelUpdated()"
          />
        </div>
      </div>

      <div class="field">
        <label class="label">Numero de contacto</label>
        <div class="control has-icons-left">
          <input
            type="number"
            class="input is-primary"
            placeholder="Numero de contacto"
            v-model="user.mobileNumber"
            @change="modelUpdated()"
          />
          <div class="icon is-small is-left">
            <i class="fa-solid fa-mobile"></i>
          </div>
        </div>
      </div>

      <div class="field">
        <label class="label">Email</label>
        <div class="control has-icons-left">
          <input
            type="email"
            class="input is-primary"
            placeholder="Email"
            v-model="user.email"
            @change="modelUpdated()"
          />
          <span class="icon is-small is-left">
            <i class="fa-solid fa-envelope" />
          </span>
        </div>
      </div>

      <div class="field">
        <label class="label">Tipo de usuario</label>
        <div class="control">
          <div class="select is-primary" @change="modelUpdated()">
            <select v-model="user.type">
              <option value="" selected disabled hidden>Seleccione uno</option>
              <option value="ES">Estudiante</option>
              <option value="PR">Profesor</option>
              <option value="AD">Administrativo</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        firstName: "",
        middleName: "",
        lastName: "",
        documentType: "",
        documentNumber: "",
        mobileNumber: "",
        email: "",
        type: "",
      },
    };
  },
  created() {
    this.localUser = this.user;
  },
  methods: {
    modelUpdated() {
      this.$emit("user-updated", this.user);
    },
  },
};
</script>

<style></style>
