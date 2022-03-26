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
import { registerEntry } from "@/api/EntryApi";
import { readTagId } from "@/SerialScanner";
export default {
  props: ["port"],
  data() {
    return {
      placa: "",
      user: "",
      time: "",
    };
  },
  watch: {},
  methods: {
    readData() {
      this.user = "";
      this.time = "";
      this.placa + "";
      readTagId(this.port)
        .then((tagId) =>
          registerEntry(tagId).then((response) => {
            const user = response.client.user;
            this.user = user.firstName + " " + user.middleName;
            this.time = response.entry;
          })
        )
        .finally(() => {
          window.setTimeout(this.readData, 5000);
        });
    },
  },
  mounted() {
    this.readData();
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
