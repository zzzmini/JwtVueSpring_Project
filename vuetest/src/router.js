import { createWebHistory, createRouter } from "vue-router";
import Join from "./components/Join.vue";
import Login from "./components/Login.vue";
import Admin from "./components/Admin.vue";
import User from "./components/User.vue";
import Main from "./components/Main.vue";

const routes = [
  {
    path: "/",
    component: Main,
  },    
  {
    path: "/join",
    component: Join,
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/admin",
    component: Admin,
  },
  {
    path: "/user",
    component: User,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router; 