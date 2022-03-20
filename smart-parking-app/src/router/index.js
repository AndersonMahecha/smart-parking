import { createRouter, createWebHistory } from "vue-router";
import HomeGeneral from "@/components/HomeGeneral";
import EntryRegister from "@/components/EntryRegister";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeGeneral,
  },
  {
    path: "/registro",
    name: "registro",
    component: EntryRegister,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
