<template>

  <div>

    <el-form ref="loginForm" :model="sendForm" :rules="rules">

      <el-form-item prop="mobile">
        <el-input v-model="sendForm.mobile" placeholder="输入手机号" prefix-icon="el-icon-phone-outline">
          <template slot="append">
            <div class="send-btn" @click="sendSms()">
              <span v-if="left ==0 ">发送</span>
              <span v-else style="color: #ff0000">{{ left }}秒</span>
            </div>
          </template>
        </el-input>

      </el-form-item>

    </el-form>

    <el-dialog
      title="验证图形码"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      width="400px"
    >
      <yf-captcha v-model="sendForm" />

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handSend">确 定</el-button>
      </div>

    </el-dialog>

  </div>

</template>

<script>
import { send } from '@/api/ability/sms'
import YfCaptcha from '../Captcha/index'
import { isMobile } from '@/utils/validate'

export default {
  name: 'SmsInput',
  components: { YfCaptcha },
  props: {
    value: String
  },
  data() {
    const validateMobile = (rule, value, callback) => {
      if (!isMobile(value)) {
        callback(new Error('手机号码格式不正确！'))
      } else {
        callback()
      }
    }

    return {
      rules: {
        mobile: [{ required: true, trigger: 'blur', validator: validateMobile }]
      },
      // 值
      timer: '',
      left: '',
      dialogVisible: false,
      sendForm: {
        mobile: ''
      }
    }
  },

  watch: {
    // 检测查询变化
    'sendForm.mobile': {
      handler(val) {
        this.$emit('input', val)
      },
      deep: true
    }
  },
  methods: {
    sendSms() {
      if (this.left > 0) {
        return
      }

      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.dialogVisible = true
        }
      })
    },

    handSend() {
      send(this.sendForm).then(() => {
        this.left = 60
        this.timer = setInterval(this.calc, 1000)
        this.dialogVisible = false
        this.$message.success('短信发送成功，请查收手机短信！')
      })
    },

    calc() {
      this.left -= 1
      if (this.left === 0) {
        clearInterval(this.timer)
      }
    }
  }
}
</script>

<style scoped>

  .send-btn{
    cursor: pointer;
    width: 40px;
    text-align: center;
  }

</style>
