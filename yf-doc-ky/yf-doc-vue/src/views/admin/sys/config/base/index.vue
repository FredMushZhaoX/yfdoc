<template>
  <div class="app-container">

    <el-form ref="postForm" :model="postForm" :rules="rules" label-position="left" label-width="150px">

      <el-form-item label="系统名称">
        <el-input v-model="postForm.siteName" placeholder="系统显示名称，用于登录页面、浏览器标题等" />
      </el-form-item>

      <el-form-item label="后台LOGO">
        <file-upload v-model="postForm.backLogo" />
      </el-form-item>

      <el-form-item label="临时目录">
        <el-input v-model="postForm.tmpDir" placeholder="系统临时文件存放，如模板文件，缓存文件等" />
      </el-form-item>

      <el-form-item label="版权信息">
        <el-input v-model="postForm.copyRight" placeholder="学员端底部的版权信息" />
      </el-form-item>

      <el-row style="margin-top: 20px">
        <el-button type="primary" @click="submitForm">保存</el-button>
      </el-row>

    </el-form>

  </div>
</template>

<script>
import { fetchDetail, saveData } from '@/api/sys/config/base'
import FileUpload from '@/components/FileUpload'

export default {
  name: 'BaseConfig',
  components: { FileUpload },
  data() {
    return {
      postForm: {
        id: '1',
        uploadType: 1,
        ossUpload: {

        },
        localUpload: {
          localDir: ''
        }
      },
      loading: false,
      rules: {
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {

    fetchData() {
      fetchDetail().then(response => {
        this.postForm = response.data
      })
    },
    submitForm() {
      console.log(JSON.stringify(this.postForm))

      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        this.loading = true

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '配置保存成功！',
            type: 'success',
            duration: 2000
          })
        })
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
