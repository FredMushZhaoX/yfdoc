<template>
  <div class="app-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" label-position="left" label-width="150px">
      <el-card>
        <el-form-item label="资料名称" prop="title">
          <el-input v-model="postForm.title" />
        </el-form-item>
        <el-form-item label="资料分类" prop="docType">
          <dic-catalog-tree v-model="postForm.docType" dic-code="document_type" />
        </el-form-item>
        <el-form-item label="资料来源" prop="docFrom">
          <el-input v-model="postForm.docFrom" />
        </el-form-item>
        <el-form-item label="关键字" prop="keyWord">
          <el-input v-model="postForm.keyWord" />
        </el-form-item>

        <el-form-item label="上传附件" prop="fileUrl">
          <file-upload v-model="postForm.fileUrl" :limit="1" :accept="accept" list-type="file" />
        </el-form-item>

        <el-form-item label="资料简介" prop="remarks">
          <el-input v-model="postForm.remarks" type="textarea" :rows="5" />
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

import { fetchDetail, saveData } from '@/api/document/document'
import DicCatalogTree from '@/components/DicTreeSelect'
import FileUpload from '@/components/FileUpload'

export default {
  components: {
    FileUpload,
    DicCatalogTree
  },
  data() {
    return {
      dialogVisible: false,
      excludes: [],
      curIndex: 0,
      accept: '.doc, .docx, .xls, .xlsx, .ppt, .pptx',
      postForm: {
        id: '',
        title: '',
        docType: '',
        docForm: '',
        remarks: ''
      },
      tableData: [
        { title: '默认文件夹', fileList: [{ title: '第一个文件夹', fileType: '视频文件' }] }
      ],
      loading: false,
      rules: {
        title: [
          { required: true, message: '资料名称不能为空！' }
        ],
        docType: [
          { required: true, message: '资料类别不能为空！' }
        ],
        fileUrl: [
          { required: true, message: '附件不能为空！' }
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
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }
        const loading = this.$loading({
          lock: true,
          text: '正在提交转换附件，请稍等',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '资料保存成功！',
            type: 'success',
            duration: 2000
          })
          loading.close()
          this.$router.push({ name: 'listDocument' })
        })
      })
    },

    // 移除目录
    removeDir(index) {
      this.postForm.dirList.splice(index, 1)
    },

    // 移除文件
    removeFile(fileList, index2) {
      fileList.splice(index2, 1)
    },

    createDir() {
      this.$prompt('请输入文件夹名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        for (let i = 0; i < this.postForm.dirList.length; i++) {
          if (this.postForm.dirList[i].title === value) {
            this.$message.error(`文件夹名称：${value}重复了，请换一个！`)
            return
          }
        }

        this.postForm.dirList.push({ title: value, fileList: [] })
      })
    },

    fileDialog(index) {
      this.curIndex = index
      this.dialogVisible = true
    },

    dialogSelect(objs) {
      if (objs != null && objs.length > 0) {
        for (let i = 0; i < objs.length; i++) {
          this.postForm.dirList[this.curIndex].fileList.push({ fileId: objs[i].id, title: objs[i].title, fileType: objs[i].fileType, fileType_dictText: objs[i].fileType_dictText, needLearn: 0, points: 0 })
        }
      }

      console.log('objs', objs)
    },

    sortDownDir(dirIndex) {
      const newIndex = dirIndex + 1
      const targetIndex = this.postForm.dirList.splice(dirIndex, 1)[0]
      this.postForm.dirList.splice(newIndex, 0, targetIndex)
    },

    sortUpDir(dirIndex) {
      const newIndex = dirIndex - 1
      const targetIndex = this.postForm.dirList.splice(dirIndex, 1)[0]
      this.postForm.dirList.splice(newIndex, 0, targetIndex)
    },

    sortDownFile(fileList, fileIndex) {
      const newIndex = fileIndex + 1
      const targetIndex = fileList.splice(fileIndex, 1)[0]
      fileList.splice(newIndex, 0, targetIndex)
    },

    sortUpFile(fileList, fileIndex) {
      const newIndex = fileIndex - 1
      const targetIndex = fileList.splice(fileIndex, 1)[0]
      fileList.splice(newIndex, 0, targetIndex)
    },

    // 处理已在列表的文件，避免重复加入
    processExcludes() {
      const ids = []

      for (let i = 0; i < this.postForm.dirList.length; i++) {
        const files = this.postForm.dirList[i].fileList

        for (let j = 0; j < files.length; j++) {
          ids.push(files[j].fileId)
        }
      }

      this.excludes = ids
    },

    onCancel() {
      this.$router.push({ name: 'listDocument' })
    }

  }
}
</script>
