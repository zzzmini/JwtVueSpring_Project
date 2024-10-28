<template>
  <div>
    <ul>
        <li>
            <router-link to="/login">로그인</router-link>
        </li>
        <li>
            <router-link to="/join">회원가입</router-link>
        </li>
        <li>
            <router-link to="/admin">Admin Page</router-link>
        </li>
        <li>
            <router-link to="/user">User Page</router-link>
        </li>
    </ul>
  </div>
</template>

<script>
export default {
    name : "MainView",
}
</script>

<style>

</style>