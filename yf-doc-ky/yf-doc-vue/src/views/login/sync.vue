<template>

  <div class="app-container">
    登录成功，正在跳转..
  </div>

</template>

<script>

export default {
  created() {
    // 获取数据
    const token = this.$route.params.token
    const roleType = this.$route.params.roleType

    this.$store.dispatch('user/sync', token).then(() => {
      // 完成跳转
      setTimeout(() => {
        this.loginBack(roleType)
      }, 2000)
    })
  },
  methods: {

    loginBack(roleType) {
      this.$router.push({ path: '/admin/dashboard' })
    }

  }
}
</script>
