<template>
  <div>
    <div style="height: 80px">
      <el-upload
        ref="upload"
        :action="action"
        accept=".xlsx"
        :multiple="false"
        :limit="1"
        :headers="header"
        :data="data"
        :on-success="importSuccess"
        list-type="file"
      >
        <el-button type="primary" icon="el-icon-upload">点击上传</el-button>
      </el-upload>
    </div>
    <el-link icon="el-icon-download" type="danger" style="float: right" @click="downloadTemplate">下载导入模板</el-link>
  </div>
</template>

<script>

import { getToken } from '@/utils/auth'
import { download } from '@/utils/request'

export default {
  name: 'ImportExcel',
  props: {
    title: String,
    action: String,
    data: Object,
    template: String
  },
  data() {
    return {
      // 上传头
      header: {}
    }
  },

  created() {
    this.header = { token: getToken() }
  },

  methods: {

    downloadTemplate() {
      return download(this.template, {}, `${this.title}导入模板.xlsx`)
    },

    importSuccess(resp) {
      // 清除内容
      this.$refs.upload.clearFiles()

      if (resp.code === 0) {
        this.$message({
          message: '数据导入成功！',
          type: 'success'
        })

        this.$emit('success', resp)
      } else {
        this.$message({
          message: resp.msg,
          type: 'error'
        })
      }
    }

  }
}
</script>
