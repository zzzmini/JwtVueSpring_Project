<template>
  <div>
    <h4>회원가입 페이지</h4>
    <hr />
    User <input type="text" v-model="username" /><br />
    email <input type="text" v-model="email" /><br />
    password <input type="password" v-model="password" /><br />
    <hr />
    <button @click="join">회원가입</button>
    <hr />
    <router-link to="/">Return</router-link>
    <hr />
  </div>
</template>

<script>
import router from "@/router";
import axios from "axios";

export default {
  name: "JoinView",
  data() {
    return {
      username: "",
      email: "",
      password: "",
      role: "ROLE_USER",
    };
  },
  methods: {
    join() {
      const user = {
        username: this.username,
        email: this.email,
        password: this.password,
        role: this.role,
      };
      axios({
        method: "post",
        url: "/join",
        data: JSON.stringify(user),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then(() => {
            alert("회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.");
            router.replace("/login");
        })
        .catch(() => {
          alert("오류입니다. 이미 존재하는 이메일입니다.");
        });
    },
  },
};
</script>

<style></style>
