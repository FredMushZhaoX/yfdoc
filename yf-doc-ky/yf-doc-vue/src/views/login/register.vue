<template>

  <div>

    <el-row>

      <el-col :lg="14" :md="15" class="left hidden-sm-and-down">
        <img src="@/assets/web/images/bg_login.png" style="height: 80vh">
      </el-col>

      <el-col :lg="10" :md="14" class="right">

        <div class="box">

          <el-tabs v-model="activeName">
            <el-tab-pane label="注册新账号" name="account">

              <el-form ref="postForm" :model="postForm" :rules="rules">

                <el-form-item prop="deptCode">
                  <depart-select v-model="postForm.deptCode" icon="el-icon-school" />
                </el-form-item>

                <el-form-item prop="realName">
                  <el-input
                    v-model="postForm.realName"
                    style="width: 100%"
                    placeholder="姓名"
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
                  <el-button type="primary" :loading="loading" style="height:44px;width: 100%;color:#fff;border-color:#01CB94;background-color: #01CB94"@click.native.prevent="handleReg">注册</el-button>
                </el-form-item>

              </el-form>

            </el-tab-pane>

          </el-tabs>

          <div style="text-align: right; line-height: 10px">
            <el-link href="/#/login" style="margin-left: 10px">已有账号？</el-link>
          </div>

        </div>

      </el-col>

    </el-row>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import DepartSelect from '@/views/login/components/depart-select'

export default {
  name: 'Register',
  components: { DepartSelect },
  computed: {
    ...mapGetters([
      'siteData'
    ])
  },
  data() {
    return {
      activeName: 'account',
      postForm: {
        mobile: '',
        password: ''
      },
      rules: {
        password: [{ required: true, trigger: 'blur', message: '登录密码不能为空！' }],
        deptCode: [{ required: true, trigger: 'blur', message: '部门必须选择！' }],
        realName: [{ required: true, trigger: 'blur', message: '姓名不能为空！' }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      redirect: undefined,
      otherQuery: {}
    }
  },

  methods: {

    toLogin() {
      this.$router.push({ name: 'Login' })
    },

    handleReg() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/reg', this.postForm)
            .then(() => {
              this.$router.push({ path: '/admin/dashboard' })
              this.loading = false
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          console.log('提交错误！')
          return false
        }
      })
    }
  }
}
</script>

