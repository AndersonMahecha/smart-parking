<template>
  <div class="container">
    <div class="container">
      <div class="title is-4">Usuarios</div>
      <router-link
        class="button is-link is-success is-multiline"
        :to="{ name: 'RegisterUser' }"
        >Crear usuario</router-link
      >
    </div>
    <table class="table is-bordered is-center">
      <thead>
        <tr class="bg-gray-100 border-b-2 border-gray-400">
          <th>Nombre</th>
          <th>Apellidos</th>
          <th>Tipo documento</th>
          <th>Numero documento</th>
          <th>Correo electronico</th>
          <th>Numero de telefono</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="client in users"
          :key="client.id"
          class="border-b border-gray-200 hover:bg-gray-100 hover:bg-orange-100"
        >
          <td>{{ client.user.firstName }}</td>
          <td>{{ client.user.middleName }} {{ client.user.lastName }}</td>
          <td>{{ client.user.documentType }}</td>
          <td>{{ client.user.documentNumber }}</td>
          <td>{{ client.user.email }}</td>
          <td>{{ client.user.mobileNumber }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import usersApi from "@/api/UsersApi";
export default {
  data() {
    return {
      users: [],
    };
  },
  created() {
    usersApi.getUsers().then((users) => (this.users = users));
  },
};
</script>

<style scoped>
.button.is-multiline {
  min-height: 2.25em;
  white-space: unset;
  height: auto;
  flex-direction: column;
}
.up::before {
  content: "👆";
}
.down::before {
  content: "👇";
}
td {
  padding: 20px 0px;
  font-size: 0.6rem;
  text-align: center;
}
th {
  padding: 5px;
  font-size: 0.6rem;
}
@media (min-width: 640px) {
  td,
  th {
    padding: 20px;
    font-size: 1rem;
  }
  th {
    padding: 12px;
  }
}
</style>
