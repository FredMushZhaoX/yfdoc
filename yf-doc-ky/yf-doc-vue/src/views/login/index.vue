<template>

  <div>

    <el-row>

      <el-col :lg="14" :md="15" class="left hidden-sm-and-down">
        <img src="@/assets/web/images/bg_login.png" style="height: 80vh">
      </el-col>

      <el-col :lg="10" :md="14" class="right">

        <div class="box">

          <el-tabs v-model="activeName">
            <el-tab-pane label="账号密码登录" name="account">

              <el-form v-if="activeName === 'account'" ref="postForm" :model="postForm" :rules="loginRules">
                <el-form-item prop="username">
                  <el-input
                    v-model="postForm.username"
                    style="width: 100%"
                    placeholder="用户名"
                    prefix-icon="el-icon-user"
                  />
                </el-form-item>

                <el-form-item prop="password">
                  <el-input
                    v-model="postForm.password"
                    show-password
                    style="width: 100%"
                    placeholder="密码"
                    type="password"
                    prefix-icon="el-icon-lock"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button type="success" :loading="loading" style="height:44px;width: 100%;color:#fff;border-color:#01CB94;background-color: #01CB94" @click.native.prevent="accountLogin">登录</el-button>
                </el-form-item>
                <div style="text-align: right; line-height: 20px">
                  <el-link href="/#/register">立即注册</el-link>
                </div>
              </el-form>

            </el-tab-pane>

          </el-tabs>
        </div>

      </el-col>

    </el-row>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import LoginDemo from './components/demo'
export default {
  components: { LoginDemo },
  data() {
    return {
      activeName: 'account',
      loading: false,
      h5Visible: false,
      mpVisible: false,
      wxVisible: false,
      staging: process.env.NODE_ENV === 'staging',
      postForm: {
        username: 'admin',
        password: 'admin',
        captchaKey: '',
        captchaValue: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        captchaValue: [{ required: true, trigger: 'blur', message: '验证码不能为空' }]
      }
    }
  },

  computed: {
    ...mapGetters([
      'siteData'
    ])
  },

  methods: {

    showH5Code() {
      this.h5Visible = true
    },
    showMpCode() {
      this.mpVisible = true
    },

    loginBack(res) {
      this.$router.push({ path: '/admin/dashboard' })
    },

    accountLogin() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.postForm)
            .then(res => {
              this.loginBack(res)
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>
