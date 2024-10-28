<template>
  <div>
    <h4>로그인 페이지</h4>
    <hr />
    email <input type="text" v-model="email" /><br />
    password <input type="password" v-model="password" /><br />
    <hr />
    <button @click="login">로그인</button>
    <hr />
    <router-link to="/">Return</router-link>
    <hr />
  </div>
</template>

<script>
import router from "@/router";
import axios from "axios";
export default {
  name: "LoginView",
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    login() {
      const user = {
        email: this.email,
        password: this.password,
      };
      axios({
        method: "post",
        url: "/login",
        data: JSON.stringify(user),
        headers: {
          "Content-Type": "application/json",
        },
      }).then((response) => {
        console.log(response);
        if (response.status === 401) {
          alert("이메일 혹은 패스워드가 잘못 입력되었습니다.");
        } else {
          let accessToken = response.headers.authorization;  // 응답헤더에서 토큰 받기
          console.log("access 토큰 :", accessToken);
          localStorage.setItem("accessToken", accessToken); // 토큰 localStorage에 저장
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${accessToken}`;
          alert("로그인 성공");
          router.replace("/main");
        }
      })
      .catch(() => {
          alert("로그인 실패!!");
        });
    },
  },
};
</script>

<style></style>
