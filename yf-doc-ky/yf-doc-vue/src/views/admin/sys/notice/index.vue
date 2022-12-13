<template>

  <div>

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @add="handleAdd"
      @edit="handleUpdate"
    >

      <template slot="filter-content">

        <el-input v-model="listQuery.params.title" style="width: 200px" placeholder="搜索公告标题" class="filter-item" />

      </template>

      <template slot="data-columns">

        <el-table-column
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          label="标题"
          prop="title"
        >
          <template slot-scope="scope">
            <el-link type="primary" @click="handleUpdate(scope.row.id)">
              {{ scope.row.title }}</el-link>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="发布时间"
          prop="createTime"
        />

        <el-table-column
          align="center"
          label="状态"
        >

          <template slot-scope="scope">
            {{ scope.row.state | stateFilter }}
          </template>
        </el-table-column>

      </template>
    </data-table>

    <el-dialog title="公告管理" :close-on-click-modal="false" :visible.sync="dialogVisible" width="70%">

      <el-form :model="formData" label-position="top">

        <el-form-item label="标题">
          <el-input v-model="formData.title" />
        </el-form-item>

        <el-form-item label="内容">
          <Tinymce ref="editor" v-model="formData.content" :height="200" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button v-permission="['sys:notice:add','sys:notice:update']" type="primary" @click="handleSave">确 定</el-button>
      </div>

    </el-dialog>

  </div>

</template>

<script>
import DataTable from '@/components/DataTable'
import { saveData, fetchDetail } from '@/api/sys/notice/notice'
import Tinymce from '@/components/Tinymce'
import permission from '@/directive/permission'

export default {
  directives: { permission },
  components: { Tinymce, DataTable },
  filters: {

    // 订单状态
    userState(value) {
      const map = {
        '0': '正常',
        '1': '禁用'
      }
      return map[value]
    }
  },
  data() {
    return {

      treeData: [],
      defaultProps: {
        value: 'id',
        label: 'deptName',
        children: 'children'
      },
      dialogVisible: false,

      listQuery: {
        current: 1,
        size: 10,
        params: {
        }
      },

      formData: {
        title: '',
        content: ''
      },

      options: {
        add: {
          enable: true,
          permission: 'sys:notice:add'
        },
        edit: {
          enable: true,
          permission: 'sys:notice:update'
        },
        delete: {
          enable: true,
          permission: 'sys:notice:delete',
          url: '/api/sys/notice/delete'
        },
        // 列表请求URL
        listUrl: '/api/sys/notice/paging',
        // 批量操作列表
        multiActions: [
          {
            value: 'enable',
            label: '启用',
            permission: 'sys:notice:update',
            url: '/api/sys/notice/state'
          }, {
            value: 'disable',
            label: '禁用',
            permission: 'sys:notice:update',
            url: '/api/sys/notice/state'
          }
        ]
      }
    }
  },

  created() {

  },

  methods: {

    handleUploadSuccess(response) {
      // 上传图片赋值
      this.formData.avatar = response.data.url
    },

    handleAdd() {
      this.formData = {}
      this.dialogVisible = true
    },

    handleUpdate(id) {
      this.dialogVisible = true
      fetchDetail(id).then(res => {
        this.formData = res.data
      })
    },

    handleSave() {
      saveData(this.formData).then(() => {
        this.$message({
          type: 'success',
          message: '公告保存成功!'
        })
        this.dialogVisible = false
        this.$refs.pagingTable.getList()
      })
    }
  }
}
</script>

<style>

.el-dialog__body{
  padding: 0px 20px;
}
</style>
