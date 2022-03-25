import { createRouter, createWebHistory } from "vue-router";
import HomeGeneral from "@/components/HomeGeneral";
import UsersComponent from "@/components/UsersComponent";
import UserRegister from "@/components/UserRegister";
import EntryRegister from "@/components/EntryRegister";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomeGeneral,
  },
  {
    path: "/usuarios",
    name: "Users",
    component: UsersComponent,
  },
  {
    path: "/usuarios/registro",
    name: "RegisterUser",
    component: UserRegister,
  },
  {
    path: "/vehicle/entry",
    name: "Entry-Vehicle",
    component: EntryRegister,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
