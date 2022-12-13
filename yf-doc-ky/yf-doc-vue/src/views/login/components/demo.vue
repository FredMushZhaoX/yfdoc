<template>

  <div style="line-height: 35px">
    <div class="title-line">演示账号 <span style="font-size: 12px; color: #ff0000">（直接登录，无需注册）</span></div>
    <el-button :loading="loading2" size="mini" round @click="adminLogin">管理员登录</el-button>
  </div>
</template>

<script>

export default {
  name: 'LoginDemo',

  data() {
    return {
      loading1: false,
      loading2: false,
      loginForm: {}
    }
  },
  methods: {

    adminLogin() {
      this.loading2 = true
      this.loginForm.username = 'admin'
      this.loginForm.password = 'admin'
      this.handleLogin()
    },

    handleLogin() {
      this.$store.dispatch('user/login', this.loginForm)
        .then(res => {
          // 其它跳到后台
          this.$router.push({ path: '/admin/dashboard' })
          setTimeout(function() {
            this.loading1 = false
            this.loading2 = false
          }, 1800)
        })
        .catch(() => {
          this.loading1 = false
          this.loading2 = false
        })
    }
  }
}
</script>

<style scoped>

  .el-divider__text{
    font-size: 12px;
    color: #666;
  }

  .title-line{
    border-bottom: #eee 1px solid;
    font-size: 14px;
    line-height: 30px;
    margin-bottom: 10px;
    font-weight: 700;
  }

</style>
