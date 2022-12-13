<template>
  <div>
    <file-upload-local v-if="conf.uploadType===1" v-model="fileUrl" :accept="accept" :tips="tips" :list-type="listType" />
    <file-upload-oss v-if="conf.uploadType===2" v-model="fileUrl" :accept="accept" :tips="tips" :list-type="listType" />
    <file-upload-qiniu v-if="conf.uploadType===3" v-model="fileUrl" :accept="accept" :tips="tips" :list-type="listType" />
  </div>
</template>

<script>

import { fetchDetail } from '@/api/sys/config/base'
import FileUploadOss from './oss'
import FileUploadLocal from './local'
import FileUploadQiniu from './qiniu'

export default {
  name: 'FileUpload',
  components: { FileUploadQiniu, FileUploadLocal, FileUploadOss },
  props: {
    value: String,
    accept: {
      type: String,
      default: '*'
    },
    tips: String,
    listType: {
      type: String,
      default: 'picture'
    }
  },
  data() {
    return {
      conf: '',
      fileUrl: ''
    }
  },

  watch: {
    // 检测查询变化
    value: {
      handler() {
        this.fillValue()
      }
    },

    // 检测查询变化
    fileUrl: {
      handler() {
        this.$emit('input', this.fileUrl)
      }
    }
  },
  mounted() {
    // 查找配置
    this.fetchConfig()
  },

  created() {
    this.fillValue()
  },

  methods: {

    // 查找配置情况
    fetchConfig() {
      fetchDetail().then(res => {
        this.conf = res.data
      })
    },

    fillValue() {
      this.fileUrl = this.value
    }

  }
}
</script>
