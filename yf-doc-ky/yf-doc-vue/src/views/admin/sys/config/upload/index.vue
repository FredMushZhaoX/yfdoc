<template>
  <div class="app-container">

    <el-form ref="postForm" :model="postForm" :rules="rules" label-position="left" label-width="150px">

      <el-form-item label="方案类型">
        <el-radio-group v-model="postForm.uploadType" size="small">
          <el-radio :label="1">本地存储</el-radio>
        </el-radio-group>
      </el-form-item>

      <div v-if="postForm.uploadType===1">
        <el-form-item label="本地目录">
          <el-input v-model="postForm.localUpload.localDir" placeholder="本地目录地址" />
        </el-form-item>
      </div>

      <el-row style="margin-top: 20px">
        <el-button type="primary" @click="submitForm">保存</el-button>
      </el-row>

    </el-form>

  </div>
</template>

<script>
import { fetchDetail, saveData } from '@/api/sys/config/upload'

export default {
  name: 'UploadConfig',
  data() {
    return {

      postForm: {
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
        this.postForm.id = '1'

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '上传配置保存成功！',
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
