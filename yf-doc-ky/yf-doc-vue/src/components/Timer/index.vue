<template>

  <span>{{ hour }}小时{{ min }}分钟{{ sec }}秒</span>

</template>

<script>

export default {
  name: 'Timer',
  props: {
    value: Number
  },

  data() {
    return {
      timer: null,
      leftSeconds: 0,
      hour: '00',
      min: '00',
      sec: '00'
    }
  },

  watch: {
    value: {
      handler() {
        if (this.timer) {
          clearInterval(this.timer)
        }
        this.leftSeconds = this.value
        this.timer = setInterval(this.countdown, 1000)
      }
    }
  },

  created() {
    this.leftSeconds = this.value
  },

  methods: {

    // 进行倒计时
    countdown() {
      // 倒计时结束了
      if (this.leftSeconds < 0) {
        this.$emit('timeout')
        return
      }

      // 时
      const hour = parseInt(this.leftSeconds / 3600)
      const min = parseInt(this.leftSeconds / 60 % 60)
      const sec = parseInt(this.leftSeconds % 60)

      this.hour = hour > 9 ? hour : '0' + hour
      this.min = min > 9 ? min : '0' + min
      this.sec = sec > 9 ? sec : '0' + sec
      this.leftSeconds -= 1
    }

  }
}
</script>

