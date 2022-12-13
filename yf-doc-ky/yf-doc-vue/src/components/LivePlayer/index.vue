<template>
  <iframe :src="`/player/live.html?source=${source}`" frameborder="none" style="width: 100%; min-height: 520px; overflow: hidden" />
</template>

<script>

export default {
  name: 'LivePlayer',
  props: {
    value: String
  },

  data() {
    return {
      source: ''
    }
  },

  watch: {
    value: {
      handler(val) {
        this.source = encodeURIComponent(val)
      },
      deep: true
    }
  },
  created() {
    this.source = encodeURIComponent(this.value)
    console.log('播放地址：', this.source)
  }
}
</script>

