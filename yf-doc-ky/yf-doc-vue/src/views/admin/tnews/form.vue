<template>
  <div class="app-container">

    <el-form ref="postForm" :model="postForm" :rules="rules" label-position="left" label-width="100px">
      <el-card>
        <el-form-item label="标题" prop="title">
          <el-input v-model="postForm.title"  />
        </el-form-item>
          <el-form-item label="新闻类别" prop="newsType">
            <dic-list-select v-model="postForm.newsType" dic-code="news_type" />
          </el-form-item>
        <el-form-item label="缩略图" prop="imgUrl">
          <file-upload v-model="postForm.imgUrl" :limit="1" :tips="tips" :accept="accept" list-type="picture" />
        </el-form-item>
        <el-form-item label="新闻内容" prop="text">
          <Tinymce ref="editor" v-model="postForm.text" :height="200" />
        </el-form-item>
      </el-card>
      <div style="margin-top: 20px">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="info" @click="onCancel">返回</el-button>
      </div>
    </el-form>

  </div>
</template>

<script>

import { fetchDetail, saveData } from '@/api/tnews/tnews'
import DicListSelect from '@/components/DicListSelect'
import FileUpload from '@/components/FileUpload'
import Tinymce from '@/components/Tinymce'
export default {
  components: {
    FileUpload,
    DicListSelect,
    Tinymce
  },
  data() {
    return {
      dialogVisible: false,
      excludes: [],
      curIndex: 0,
      accept: '.png, .jpg, .gif, .jpeg, .bmp',
      postForm: {
        id: '',
        title: '',
        newsType: '',
        text: '',
        imgUrl: ''
      },
      tableData: [
        { title: '默认文件夹', fileList: [{ title: '第一个文件夹', fileType: '图片文件' }] }
      ],
      loading: false,
      rules: {
        title: [
          { required: true, message: '标题不能为空！' }
        ],
        newsType: [
          { required: true, message: '新闻类别不能为空！' }
        ],
        imgUrl: [
          { required: true, message: '缩略图不能为空！' }
        ]
      }
    }
  },

  watch: {

    // 检测查询变化
    'postForm.dirList': {
      handler() {
        this.processExcludes()
      },
      deep: true
    }

  },
  created() {
    const id = this.$route.params.id
    if (typeof id !== 'undefined') {
      this.fetchData(id)
    }
  },
  methods: {

    fetchData(id) {
      fetchDetail(id).then(response => {
        this.postForm = response.data
      })
    },
    submitForm() {
      console.log(JSON.stringify(this.postForm))

      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '新闻保存成功！',
            type: 'success',
            duration: 2000
          })

          this.$router.push({ name: 'listNews' })
        })
      })
    },
    onCancel() {
      this.$router.push({ name: 'listNews' })
    }

  }
}
</script>
