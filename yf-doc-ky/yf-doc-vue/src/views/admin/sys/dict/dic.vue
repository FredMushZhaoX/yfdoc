<template>

  <div>

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @add="addDicDialog"
      @edit="editDicDialog"
    >

      <template slot="filter-content">

        <el-input v-model="listQuery.params.title" style="width: 200px" placeholder="搜索字典ID/名称" class="filter-item" />

      </template>

      <template slot="data-columns">

        <el-table-column
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          label="名称"
          prop="title"
        />

        <el-table-column
          align="center"
          label="编码"
          prop="code"
        />

        <el-table-column
          align="center"
          label="描述"
          prop="remark"
        />

        <el-table-column
          align="center"
          label="配置"
        >

          <template slot-scope="scope">
            <el-link v-permission="['sys:dict:value:sub']" type="primary" size="small" icon="el-icon-s-tools" style="margin-left: 10px" @click="showCatalog(scope.row.code)">字典项</el-link>
          </template>
        </el-table-column>

      </template>
    </data-table>

    <el-dialog title="数据字典" :close-on-click-modal="false" :visible.sync="dicDialog" width="500px">

      <el-form ref="dicForm" :model="dicForm" :rules="dicFormRules" label-position="left" label-width="100px">

        <el-form-item label="名称" prop="title">
          <el-input v-model="dicForm.title" />
        </el-form-item>

        <el-form-item label="编码" prop="code">
          <el-input v-model="dicForm.code" :disabled="dicForm.id" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="dicForm.remark" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dicDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveDic">确 定</el-button>
      </div>

    </el-dialog>

    <sys-dic-value :drawer="drawer" :dic-code="dicCode" :dic-type="1" @close="drawer = false" />

  </div>

</template>

<script>
import DataTable from '@/components/DataTable'
import { saveDic } from '@/api/sys/dict/dict'
import SysDicValue from './components/value'
import permission from '@/directive/permission'

export default {
  components: { SysDicValue, DataTable },
  directives: { permission },
  data() {
    return {

      // 当前维护的字典编码
      dicCode: '',

      // 字典项相关
      dicDialog: false,
      dicForm: {
        type: 1
      },
      dicFormRules: {
        title: [
          { required: true, message: '字典名称不能为空！' }
        ],
        code: [
          { required: true, message: '字典编码不能为空！' }
        ]
      },
      listQuery: {
        current: 1,
        size: 10,
        params: {
          type: 1
        }
      },
      options: {

        add: {
          enable: true,
          permission: 'sys:dict:value:add'
        },
        edit: {
          enable: true,
          permission: 'sys:dict:value:update'
        },
        delete: {
          enable: true,
          permission: 'sys:dict:value:delete',
          url: '/api/sys/dic/delete'
        },

        // 列表请求URL
        listUrl: '/api/sys/dic/paging'
      },

      drawer: false

    }
  },

  created() {

  },

  methods: {

    addDicDialog() {
      this.dicForm = { type: 1 }
      this.dicDialog = true
    },

    editDicDialog(id, item) {
      this.dicForm = item
      this.dicDialog = true
    },

    handleSaveDic() {
      this.$refs.dicForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveDic(this.dicForm).then(() => {
          this.$message({
            type: 'success',
            message: '字典保存成功!'
          })
          this.dicDialog = false
          this.$refs.pagingTable.getList()
        })
      })
    },

    showCatalog(code) {
      this.dicCode = code
      this.drawer = true
    }

  }
}
</script>
