<template>
  <div class="container">
    <div class="columns is-flex has-text-centered is-vcentered">
      <div class="column">
        <div class="field">
          <label class="label is-size-3">Usuario</label>
          <div class="control field has-addons has-addons-fullwidth">
            <input
              v-model="user"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
            <button class="button" :class="port ? 'is-success ' : 'is-danger'">
              <span class="icon"> <i class="fa-solid fa-id-card"></i></span>
              <span>Leer</span>
            </button>
          </div>
        </div>
        <div class="field">
          <label class="label is-size-3">Placa</label>
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
          <label class="label is-size-3">Entrada</label>
          <div class="control">
            <input
              v-model="time"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
          </div>
        </div>
        <div class="field">
          <label class="label is-size-3">Tiempo total</label>
          <div class="control">
            <input
              v-model="time"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
          </div>
        </div>
        <div class="field">
          <label class="label is-size-3">Costo total</label>
          <div class="control">
            <input
              v-model="time"
              class="input big-size has-text-centered"
              type="text"
              disabled
            />
          </div>
        </div>
        <button class="button is-fullwidth is-primary is-size-3">Pagar</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["port"],
  data() {
    return {};
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
  font-size: 1rem;
}
</style>
