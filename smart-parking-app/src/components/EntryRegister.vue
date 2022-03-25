<template>
  <div class="container">
    <div class="columns is-flex has-text-centered is-vcentered">
      <div class="column">
        <div class="field">
          <label class="label is-size-1">USUARIO</label>
          <div class="control">
            <input
              v-model="user"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
          </div>
        </div>
        <div class="field">
          <label class="label is-size-1">PLACA</label>
          <div class="control">
            <input
              v-model="placa"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
          </div>
        </div>
        <div class="field">
          <label class="label is-size-1">HORA Y FECHA</label>
          <div class="control">
            <input
              v-model="time"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
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
      placa: "",
      user: "",
      time: "",
    };
  },
  watch: {
    placa() {
      this.getCurrentDateTime();
    },
    user() {
      this.getCurrentDateTime();
    },
  },
  methods: {
    currentDateTime() {
      const current = new Date();
      const date =
        current.getFullYear() +
        "/" +
        (current.getMonth() + 1) +
        "/" +
        current.getDate();
      const time =
        current.getHours() +
        ":" +
        current.getMinutes() +
        ":" +
        current.getSeconds();
      const dateTime = date + " " + time;

      this.time = dateTime;
    },
  },
};
</script>

<style scoped>
.columns.is-vcentered {
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  align-items: center;
}
.input.big-size {
  font-size: 5rem;
}
</style>
