<template>

  <iframe :src="fullUrl" width="100%" height="800px" />

</template>

<script>
export default {
  name: 'PdfFrame',
  props: {
    src: String
  },
  data() {
    return {
      viewUrl: '/pdf/web/viewer.html?file=',
      fullUrl: ''
    }
  },
  watch: {

    src: {
      handler() {
        this.fullUrl = this.viewUrl + this.src
      }
    }
  },
  created() {
    this.fullUrl = this.viewUrl + this.src
  }

}

</script>
