<template>

  <div>

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @add="handleAdd"
      @edit="handleUpdate"
    >

      <template slot="data-columns">

        <el-table-column
          label="角色ID"
          prop="id"
        />

        <el-table-column
          label="角色名称"
          prop="roleName"
        />

        <el-table-column
          label="角色类型"
          prop="roleType_dictText"
        />

        <el-table-column
          label="操作项"
          align="center"
        >
          <template slot-scope="scope">

            <el-link
              v-permission="['sys:role:grant']"
              type="primary"
              icon="el-icon-unlock"
              @click="changeGrant(scope.row.id)"
            >菜单授权</el-link>

            <el-link
              v-permission="['sys:user']"
              style="margin-left: 10px"
              type="primary"
              icon="el-icon-user"
              @click="showUser(scope.row.id)"
            >用户列表</el-link>

          </template>
        </el-table-column>

      </template>

    </data-table>

    <el-dialog title="管理角色" :close-on-click-modal="false" :visible.sync="dialogVisible" width="30%">

      <el-form ref="postForm" :model="postForm" label-position="left" :rules="rules" label-width="100px">

        <el-form-item label="角色ID" prop="id">
          <el-input v-model="postForm.id" :disabled="editMode"  @keyup.native="postForm.id=postForm.id.replace(/[^a-zA-Z]/g,'')"/>
        </el-form-item>

        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="postForm.roleName" />
        </el-form-item>

        <el-form-item label="角色类型" prop="roleType">
          <el-select v-model="postForm.roleType">
            <el-option label="学员/前端用户" :value="1" />
            <el-option label="管理员/教师/后端用户" :value="2" />
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>

    </el-dialog>

    <el-dialog
      title="角色授权"
      :close-on-click-modal="false"
      :visible.sync="grantVisible"
      width="50%;"
      top="5vh"
    >

      <div style="max-height: 500px;overflow: scroll">
        <el-tree
          ref="tree"
          :data="treeData"
          show-checkbox
          default-expand-all
          check-strictly
          node-key="id"
          highlight-current
          :close-on-click-modal="false"
          :props="defaultProps"
        />

      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="grantVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveGrant">确 定</el-button>
      </span>

    </el-dialog>

    <el-drawer
      size="70%"
      title="用户列表"
      :wrapper-closable="false"
      :visible.sync="userVisible"
      direction="rtl"
    >
      <sub-user-list :role-ids="roleIds" />
    </el-drawer>

  </div>

</template>

<script>
import { fetchTree } from '@/api/sys/menu/menu'
import { saveMenu, listMenu, saveData, apiDetail } from '@/api/sys/role/role'
import DataTable from '@/components/DataTable'
import { Loading } from 'element-ui'
import permission from '@/directive/permission'
import SubUserList from '@/views/admin/sys/user/components/SubUserList'

export default {
  name: 'SysRoleList',
  directives: { permission },
  components: { SubUserList, DataTable },
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
      dialogVisible: false,
      editMode: false,
      postForm: {},

      grantLoading: false,
      grantVisible: false,
      defaultProps: {
        children: 'children',
        label: 'metaTitle'
      },
      treeData: [],
      roleId: '',
      roleIds: [],
      rules: {

        id: [
          { required: true, message: '角色ID不能为空，且只能输入字母！' }
        ],
        roleType: [
          { required: true, message: '角色类型不能为空！' }
        ],
        roleName: [
          { required: true, message: '角色名称不能为空！' }
        ]
      },

      listQuery: {
        current: 1,
        size: 10,
        params: {
        }
      },

      options: {
        multi: true,
        add: {
          enable: true,
          permission: 'sys:role:add'
        },
        edit: {
          enable: true,
          permission: 'sys:role:update'
        },
        delete: {
          enable: true,
          permission: 'sys:role:delete',
          url: '/api/sys/role/delete'
        },
        // 列表请求URL
        listUrl: '/api/sys/role/paging'
      },

      userVisible: false
    }
  },

  created() {
    // 加载树结构
    fetchTree().then(res => {
      this.treeData = res.data
    })
  },

  methods: {

    // 修改角色授权
    changeGrant(id) {
      // 赋值当前角色
      this.roleId = id
      listMenu(id).then(res => {
        this.$refs.tree.setCheckedKeys(res.data)
      })
      this.grantVisible = true
    },

    // 查看用户列表
    showUser(id) {
      this.roleIds = [id]
      this.userVisible = true
    },

    saveGrant() {
      // 获取选择的数据
      const ids = this.$refs.tree.getCheckedKeys()
      const data = { roleId: this.roleId, menuIds: ids }

      // 打开
      const loading = Loading.service({
        text: '授权处理中，请稍后！',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      // 保存菜单权限
      saveMenu(data).then(() => {
        this.grantVisible = false
        this.$message({
          type: 'success',
          message: '角色授权保存成功!'
        })

        loading.close()
      })
    },

    handleAdd() {
      this.postForm = {}
      this.dialogVisible = true
      this.editMode = false
    },

    handleUpdate(id) {
      this.dialogVisible = true
      this.editMode = true
      apiDetail(id).then(res => {
        this.postForm = res.data
      })
    },

    handleSave() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }
        this.postForm.dataScope = "4"
        saveData(this.postForm).then(() => {
          this.$message({
            type: 'success',
            message: '角色修改成功!'
          })
          this.dialogVisible = false
          this.$refs.pagingTable.getList()
        })
      })
    }
  }
}
</script>
