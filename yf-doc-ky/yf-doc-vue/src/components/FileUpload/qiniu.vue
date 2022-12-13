<template>
  <div class="content">
    <el-upload
      :action="conf.endpoint"
      :before-upload="beforeUpload"
      :before-remove="beforeRemove"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :on-exceed="handleExceed"
      :drag="listType!=='picture'"
      :accept="accept"
      :limit="limit"
      :file-list="fileList"
      :list-type="listType"
      :data="dataObj"
    >

      <el-button v-if="listType==='picture'" size="small" type="primary">点击上传</el-button>

      <i v-if="listType!=='picture'" class="el-icon-upload" />
      <div v-if="listType!=='picture'" class="el-upload__text">
        将文件拖到此处，或
        <em>点击上传</em>
      </div>
      <div v-if="tips" slot="tip" class="el-upload__tip">{{ tips }}</div>
    </el-upload>

  </div>
</template>

<script>
import { buildDir } from '@/utils/file-utils'
import { fetchToken } from '@/api/sys/config/qiniu'
import { fetchDetail } from '@/api/sys/config/upload'

export default {
  name: 'FileUploadQiniu',
  props: {
    value: String,
    accept: String,
    tips: String,
    listType: String,
    limit: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      fileList: [], // 文件列
      dataObj: {
        token: ''
      },
      conf: {
        endpoint: ''
      }
    }
  },
  watch: {
    // 检测查询变化
    value: {
      handler() {
        this.fillValue()
      }
    }
  },
  mounted() {
    this.fetchConfig()
  },

  created() {
    this.fillValue()
  },
  methods: {

    // 获取基础配置
    fetchConfig() {
      fetchDetail().then(res => {
        this.conf = res.data.qiniuUpload

        // 获得上传Token
        fetchToken().then(res => {
          this.dataObj.token = res.data.token
        }).catch(err => {
          this.$message.error(`获取上传令牌失败： ${err} `)
        })
      })
    },

    // 填充数据
    fillValue() {
      this.fileList = []
      this.fileUrl = this.value
      if (this.fileUrl) {
        this.fileList = [{ name: this.fileUrl, url: this.fileUrl }]
      }
    },

    // 文件超出个数限制时的钩子
    handleExceed() {
      this.$message.warning(`每次只能上传 ${this.limit} 个文件`)
    },
    // 删除文件之前的钩子
    beforeRemove() {
      return this.$confirm(`确定移除文件吗？`)
    },
    // 文件列表移除文件时的钩子
    handleRemove() {
      this.$emit('input', '')
      this.fileList = []
    },
    // 文件上传成功时的钩子
    handleSuccess(response, file, fileList) {
      this.fileList = fileList

      this.$emit('input', this.conf.url + response.key)
    },

    // 文件上传前的校验
    beforeUpload(file) {
      const temporary = file.name.lastIndexOf('.')
      const fileNameLength = file.name.length
      const fileFormat = file.name.substring(
        temporary + 1,
        fileNameLength
      )
      this.dataObj.key = buildDir() + '.' + fileFormat
    }

  }
}
</script>
