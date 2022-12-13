<template>
  <div class="upload-container">
    <el-button :style="{background:color,borderColor:color}" icon="el-icon-upload" size="mini" type="primary" @click="dialogVisible=true">
      上传
    </el-button>
    <el-dialog
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      :append-to-body="true"
      width="500px"
    >
      <file-upload v-model="fileUrl" class="editor-slide-upload" accept="image/png, image/jpeg, image/gif" />

      <div slot="footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="handleSubmit">
          确认
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>

import FileUpload from '@/components/FileUpload'
export default {
  name: 'EditorSlideUpload',
  components: { FileUpload },
  props: {
    color: {
      type: String,
      default: '#1890ff'
    }
  },
  data() {
    return {
      fileUrl: '',
      dialogVisible: false
    }
  },
  methods: {
    handleSubmit() {
      const arr = [{ name: this.fileUrl, url: this.fileUrl }]
      this.$emit('successCBK', arr)
      this.dialogVisible = false
      this.fileUrl = null
    }
  }
}
</script>

<style lang="scss" scoped>
.editor-slide-upload {
  margin-bottom: 20px;
  /deep/ .el-upload--picture-card {
    width: 100%;
  }
}
</style>
