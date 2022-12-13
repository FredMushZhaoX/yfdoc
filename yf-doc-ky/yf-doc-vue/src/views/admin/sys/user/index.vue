<template>

  <div class="app-container">

    <el-row :gutter="20">

      <el-col :span="6">

        <el-card>

          <el-tree
            ref="tree"
            :data="treeData"
            default-expand-all
            node-key="id"
            highlight-current
            :close-on-click-modal="false"
            :props="defaultProps"
            @node-click="departSelected"
          />

        </el-card>

      </el-col>

      <el-col :span="18">

        <data-table
          ref="pagingTable"
          :options="options"
          :list-query="listQuery"
          @add="handleAdd"
          @edit="handleUpdate"
          @multi-actions="handleMultiActions"
        >

          <template slot="filter-content">

            <role-select v-model="listQuery.params.roleIds" class="filter-item" style="width: 350px;" />
            <el-input v-model="listQuery.params.userName" style="width: 200px" placeholder="搜索登录名或姓名" class="filter-item" />

            <el-button-group class="filter-item" style="float:  right">
              <el-button v-permission="['sys:user:export']" size="mini" icon="el-icon-download" @click="exportExcel">导出</el-button>
            </el-button-group>

          </template>

          <template slot="data-columns">

            <el-table-column
              type="selection"
              width="55"
            />

            <el-table-column
              align="center"
              label="登录账号"
              prop="userName"
            />

            <el-table-column
              align="center"
              label="真实姓名"
              prop="realName"
            />

            <el-table-column
              align="center"
              label="归属部门"
              prop="deptCode_dictText"
            />

            <el-table-column
              align="center"
              label="角色"
              prop="roleNames"
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

      </el-col>

    </el-row>

    <el-dialog title="管理用户" :close-on-click-modal="false" :append-to-body="true" :visible.sync="dialogVisible" width="50%">

      <el-form ref="postForm" :model="formData" label-position="left" :rules="rules" label-width="100px">

        <el-form-item label="登录账号" prop="userName">
          <el-input v-model="formData.userName" />
        </el-form-item>

        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" />
        </el-form-item>

        <el-form-item label="登录密码" prop="password">
          <el-input v-model="formData.password" placeholder="不修改请留空" type="password" />
        </el-form-item>

        <el-form-item label="所属部门" prop="deptCode">
          <depart-tree-select v-model="formData.deptCode" />
        </el-form-item>

        <el-form-item label="用户角色" prop="roles">
          <role-select v-model="formData.roles" />
        </el-form-item>

        <el-form-item label="电子邮箱">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item label="身份证号">
          <el-input v-model="formData.idCard" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>

    </el-dialog>

  </div>

</template>

<script>
import DataTable from '@/components/DataTable'
import { saveData, fetchDetail, exportExcel, apiBatchDept, apiBatchRole } from '@/api/sys/user/user'
import { fetchTree } from '@/api/sys/depart/depart'
import DepartTreeSelect from '@/components/DepartTreeSelect'
import RoleSelect from '@/components/RoleSelect'
import ImportExcel from '@/components/ImportExcel'
import permission from '@/directive/permission'

export default {
  name: 'SysUserList',
  directives: { permission },
  components: { ImportExcel, DepartTreeSelect, DataTable, RoleSelect },
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
  props: {
    roleIds: Array
  },
  data() {
    return {

      importVisible: false,

      rules: {

        userName: [
          { required: true, message: '用户不能为空！' }
        ],
        realName: [
          { required: true, message: '姓名不能为空！' }
        ],
        password: [
          { required: true, message: '密码不能为空！' }
        ],
        deptCode: [
          { required: true, message: '部门不能为空！' }
        ],

        roles: [
          { required: true, message: '必须选择一个角色！' }
        ]
      },

      dialogVisible: false,

      listQuery: {
        current: 1,
        size: 10,
        params: {
          roleId: '',
          deptCode: ''
        }
      },

      formData: {
        avatar: ''
      },

      options: {

        add: {
          enable: true,
          permission: 'sys:user:add'
        },
        edit: {
          enable: true,
          permission: 'sys:user:update'
        },
        delete: {
          enable: true,
          permission: 'sys:user:delete',
          url: '/api/sys/user/delete'
        },

        // 列表请求URL
        listUrl: '/api/sys/user/paging',
        // 批量操作列表
        multiActions: [
          {
            value: 'enable',
            label: '启用',
            url: '/api/sys/user/state'
          }, {
            value: 'disable',
            label: '禁用',
            url: '/api/sys/user/state'
          }
        ]
      },

      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'deptName'
      },

      deptVisible: false,
      batchDeptForm: {
        deptCode: '',
        userIds: []
      },

      roleVisible: false,
      batchRoleForm: {
        roleIds: [],
        userIds: []
      }
    }
  },

  watch: {
    roleIds: {
      handler(val) {
        console.log('role-val', val)
        this.listQuery.params.roleIds = val
        this.$refs.pagingTable.getList()
      }
    }
  },

  created() {
    const departId = this.$route.params.departId
    if (typeof departId !== 'undefined') {
      this.listQuery.params.departId = departId
    }

    if (this.roleIds != null && this.roleIds.length > 0) {
      this.listQuery.params.roleIds = this.roleIds
    }

    // 获取部门树结构
    fetchTree().then(res => {
      this.treeData = res.data
    })
  },

  methods: {

    handleUploadSuccess(response) {
      // 上传图片赋值
      this.formData.avatar = response.data.url
    },

    handleAdd() {
      this.formData = { departId: this.formData.departId }
      this.dialogVisible = true
      this.rules.password = [{ required: true, message: '密码不能为空！' }]
    },

    handleUpdate(id) {
      fetchDetail(id).then(res => {
        this.dialogVisible = true
        this.formData = res.data
        this.formData.password = null
        this.rules.password = []
      })
    },

    departSelected(data) {
      this.formData.departId = data.id
      this.listQuery.params.deptCode = data.deptCode
    },
    handleSave() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveData(this.formData).then(() => {
          this.$message({
            type: 'success',
            message: '用户保存成功!'
          })
          this.dialogVisible = false
          this.$refs.pagingTable.getList()
        })
      })
    },

    exportExcel() {
      // 导出当前查询的数据
      exportExcel(this.listQuery.params)
    },

    showImport() {
      this.importVisible = true
    },

    importSuccess() {
      this.importVisible = false
      this.$refs.pagingTable.getList()
    },

    handleMultiActions(event) {
      if (event.opt === 'changeDept') {
        this.deptVisible = true
        this.batchDeptForm.flag = 1
        this.batchDeptForm.userIds = event.ids
      }

      if (event.opt === 'clearDept') {
        this.batchDeptForm.deptCode = ''
        this.batchDeptForm.userIds = event.ids
        this.saveBatchDept()
      }

      if (event.opt === 'changeRole') {
        this.roleVisible = true
        this.batchRoleForm.flag = 1
        this.batchRoleForm.userIds = event.ids
      }

      if (event.opt === 'clearRole') {
        this.roleVisible = true
        this.batchRoleForm.flag = 0
        this.batchRoleForm.userIds = event.ids
      }
    },

    saveBatchDept() {
      apiBatchDept(this.batchDeptForm).then(() => {
        this.$message({
          type: 'success',
          message: '批量操作用户部门成功!'
        })

        this.deptVisible = false
        this.$refs.pagingTable.getList()
      })
    },

    saveBatchRole() {
      apiBatchRole(this.batchRoleForm).then(() => {
        this.$message({
          type: 'success',
          message: '批量操作用户角色成功!'
        })

        this.roleVisible = false
        this.$refs.pagingTable.getList()
      })
    }
  }
}
</script>
