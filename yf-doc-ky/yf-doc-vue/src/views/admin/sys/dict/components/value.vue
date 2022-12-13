<template>

  <div>

    <el-dialog title="字典项" :close-on-click-modal="false" :visible.sync="valueDialog" width="500px">

      <el-form ref="valueForm" :model="valueForm" :rules="valueFormRules" label-position="left" label-width="100px">

        <el-form-item label="名称" prop="title">
          <el-input v-model="valueForm.title" placeholder="显示的名称" />
        </el-form-item>

        <el-form-item v-if="dicType===1" label="数据值" prop="value">
          <el-input v-model="valueForm.value" :disabled="valueForm.id" placeholder="编程引用的值" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="valueForm.remark" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="valueDialog = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveValue">确 定</el-button>
      </div>

    </el-dialog>

    <el-drawer
      title=" "
      :wrapper-closable="false"
      :visible.sync="drawerVisible"
      @close="drawerClose"
    >

      <div style="padding: 10px">

        <el-button type="primary" size="small" style="margin-bottom: 20px" @click="addValueDialog({id: '0'})">添加</el-button>

        <el-table
          ref="table"
          :data="tableData"
          row-key="id"
          border
          :default-expand-all="true"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          :header-cell-style="{'background':'#f2f3f4', 'color':'#555', 'font-weight':'bold', 'line-height':'32px'}"
        >

          <el-table-column label="名称" prop="title" align="center" />

          <el-table-column v-if="dicType!==2" label="字典值" prop="value" align="center" />

          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button v-if="dicType===2" title="添加下级" size="mini" icon="el-icon-plus" circle @click="addValueDialog(scope.row)" />
              <el-button title="编辑" size="mini" icon="el-icon-edit" circle @click="editValueDialog(scope.row)" />
              <el-button title="删除" size="mini" icon="el-icon-delete" circle @click="handleDeleteValue(scope.row.id)" />
            </template>
          </el-table-column>

        </el-table>

      </div>

    </el-drawer>

  </div>

</template>

<script>
import { fetchTree, saveValue, deleteValue } from '@/api/sys/dict/value'

export default {
  name: 'SysDicValue',
  props: {
    drawer: Boolean,
    dicCode: String,
    dicType: Number
  },
  data() {
    return {

      drawerVisible: false,
      // 字典值相关
      valueDialog: false,
      valueForm: {
        dicCode: '',
        id: '',
        parentId: '',
        title: '',
        remark: ''
      },
      valueFormRules: {
        title: [
          { required: true, message: '名称不能为空！' }
        ],
        value: [
          { required: true, message: '数据值不能为空！' }
        ]
      },

      tableData: []
    }
  },

  watch: {

    dicCode() {
      this.valueForm.dicCode = this.dicCode
      this.fetchValueTree()
    },

    drawer() {
      this.drawerVisible = this.drawer
    }
  },

  created() {

  },

  methods: {

    drawerClose() {
      this.$emit('close')
    },

    fetchValueTree() {
      fetchTree({ dicCode: this.valueForm.dicCode }).then(res => {
        this.tableData = res.data
      })
    },

    addValueDialog(item) {
      this.valueForm = { parentId: item.id, dicCode: this.dicCode }
      this.valueDialog = true
    },

    editValueDialog(item) {
      this.valueForm = item
      this.valueDialog = true
    },

    handleSaveValue() {
      this.$refs.valueForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveValue(this.valueForm).then(() => {
          this.$message({
            type: 'success',
            message: '保存成功!'
          })
          this.valueDialog = false

          this.fetchValueTree()
        })
      })
    },
    handleDeleteValue(id) {
      this.$confirm('确实要删除吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteValue({ ids: [id] }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.valueDialog = false

          this.fetchValueTree()
        })
      })
    }

  }
}
</script>

<style scoped>

  /deep/
  .el-drawer__header{
    margin-bottom: 0px;
    padding: 10px;
  }
</style>
