<template>

  <el-input v-model="model.captchaValue" placeholder="输入验证码">
    <template slot="prepend">
      <div @click="changeCode()">
        <img :src="`/api/common/captcha/gen?key=${model.captchaKey}`" style="height: 30px; width: auto">
      </div>
    </template>
  </el-input>

</template>

<script>
import { uuid } from 'vue-uuid'

export default {
  name: 'YfCaptcha',
  props: {
    value: {
      type: Object,
      default: function() {
        return {}
      }
    }

  },
  data() {
    return {
      // 值
      model: { captchaValue: '', captchaKey: '' }
    }
  },
  watch: {
    // 检测查询变化
    value: {
      handler(val) {
        this.model = val
      },
      deep: true
    }
  },
  created() {
    this.model = this.value
    this.changeCode()
  },
  methods: {
    changeCode() {
      this.model.captchaKey = uuid.v4()
      this.$emit('input', this.model)
    }
  }
}
</script>

<style scoped>

  /deep/
  .el-input-group__prepend{
    background: #fff;
    cursor: pointer;
  }

  .el-input-group__prepend div{
    display: flex;
    align-items: center;
    width: 80px
  }

</style>
