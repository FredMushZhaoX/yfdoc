<template>
  <div class="app-container" v-loading="listLoading">

    <el-alert
      type="error"
      style="margin-bottom: 20px"
    >
      菜单属于系统配置，请在前后端协调下进行修改
    </el-alert>

    <div class="filter-container" v-permission="['sys:menu:add']">
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="formDialog(0)">
        添加
      </el-button>
    </div>

    <el-table
      ref="table"
      :data="tableData"
      row-key="id"
      border
      :default-expand-all.sync="defaultExpand"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :header-cell-style="{'background':'#f2f3f4', 'color':'#555', 'font-weight':'bold', 'line-height':'32px'}"
    >
      <el-table-column label="名称" prop="metaTitle" width="200px" />

      <el-table-column label="图标" width="80px" align="center">
        <template slot-scope="scope">
          <svg-icon  v-if="scope.row.metaIcon" :icon-class="scope.row.metaIcon" />
        </template>
      </el-table-column>

      <el-table-column label="类型" width="150px" align="center">
        <template slot-scope="scope">
          {{ scope.row.menuType | menuTypeFilter }}
        </template>
      </el-table-column>

      <el-table-column label="显示" width="120px" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.menuType != 2">
            <span v-if="scope.row.hidden" style="color: #aaa">隐藏</span>
            <span v-else style="color: #0a84ff">显示</span>
          </div>

        </template>
      </el-table-column>

      <el-table-column label="菜单路由" prop="path" />
      <el-table-column label="权限标识" prop="permissionTag" width="200px" />

      <el-table-column align="center" label="排序" width="150px">
        <template slot-scope="scope">
          <el-button v-permission="['sys:menu:sort']" title="下移" size="mini" icon="el-icon-sort-down" circle @click="handleSort(scope.row.id, 1)" />
          <el-button v-permission="['sys:menu:sort']" title="上移" size="mini" icon="el-icon-sort-up" circle @click="handleSort(scope.row.id, 0)" />
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="150px">
        <template slot-scope="scope">
          <el-button v-permission="['sys:menu:add']" title="添加子菜单" size="mini" icon="el-icon-plus" circle @click="formDialog(scope.row.id)" />
          <el-button v-permission="['sys:menu:update']" title="编辑" size="mini" icon="el-icon-edit" circle @click="formDialog(scope.row.parentId, scope.row.id)" />
          <el-button  v-permission="['sys:menu:delete']" title="删除" size="mini" icon="el-icon-delete" circle @click="handleDelete(scope.row.id)" />
        </template>
      </el-table-column>

    </el-table>

    <el-dialog
      title="菜单管理"
      width="50%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="dialogFormVisible"
    >
      <el-form ref="postForm" :model="postForm" :rules="rules" label-width="120px" label-position="left">

<!--        <el-form-item label="菜单类型" prop="menuType">-->
<!--          <el-radio-group v-model="postForm.menuType">-->
<!--            <el-radio :label="1">菜单</el-radio>-->
<!--            <el-radio :label="2">功能</el-radio>-->
<!--            <el-radio :label="3">路由</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->

        <el-form-item label="菜单标题" prop="metaTitle">
          <el-input v-model="postForm.metaTitle" />
        </el-form-item>

        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="postForm.name" placeholder="应为英文，用于前端程序编码"  @keyup.native="postForm.name=postForm.name.replace(/[^a-zA-Z]/g,'')" />
        </el-form-item>

        <el-form-item label="视图组件" v-if="postForm.parentId!==0">
          <el-input v-model="postForm.component" placeholder="位于前端view/目录下的相对文件路径" />
        </el-form-item>

        <el-form-item label="访问路径">
          <el-input v-model="postForm.path" placeholder="页面菜单路径，如：/admin/user" />
        </el-form-item>

        <el-form-item label="权限标识">
          <el-input v-model="postForm.permissionTag" placeholder="用于后端权限控制，如：sys:user:add" />
        </el-form-item>

        <el-form-item label="菜单图标">
          <div style="display: flex; align-items: center;">
            <svg-icon v-if="postForm.metaIcon" :icon-class="postForm.metaIcon" style="width: 20px; height: 20px; margin-right: 10px" />
            <el-button size="mini" @click="iconDialog = true">选择图标</el-button>
            <el-button type="danger" size="mini" @click="clearIcon">清除</el-button>
          </div>

        </el-form-item>

        <el-form-item label="可视状态">
          <el-switch
            v-model="postForm.hidden"
            active-text="隐藏"
            inactive-text="显示"
          />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>

    <icon-dialog :visible.sync="iconDialog" @select="iconSelect" /></div>
</template>

<script>

import { fetchTree, fetchDetail, saveData, deleteData, sortData } from '@/api/sys/menu/menu'
import IconDialog from '@/components/IconDialog'
import permission from '@/directive/permission'

export default {
  name: 'SysMenu',
  directives: { permission },
  components: { IconDialog },
  data() {
    return {
      iconDialog: false,
      defaultExpand: true,
      postForm: {},
      rules: {
        menuType: [
          { required: true, message: '菜单类型不能为空！' }
        ],
        metaTitle: [
          { required: true, message: '菜单标题不能为空！' }
        ],
        name: [
          { required: true, message: '菜单名称不能为空！' }
        ]
      },
      dialogFormVisible: false,
      tableData: [],
      listLoading: true,
      listQuery: {
        current: 1,
        size: 10,
        params: {

        }
      }
    }
  },
  watch: {

    // 检测查询变化
    listQuery: {
      handler() {
        this.getList()
      },
      deep: true
    }
  },
  created() {
    this.getList()
  },
  methods: {

    /**
       * 编辑窗口
       * @param parentId
       * @param currentId
       */
    formDialog(parentId, currentId) {

      // 清空表单
      this.postForm = {}

      if(parentId === 0 ){
        this.postForm.component = 'Layout'
      }

      // 覆盖上级分类
      this.postForm.refType = this.listQuery.refType
      this.postForm.parentId = parentId

      // 如果是修改
      if (currentId != null) {
        fetchDetail(currentId).then(response => {
          this.postForm = response.data
        })
      }

      this.dialogFormVisible = true
    },

    getList() {
      this.listLoading = true
      fetchTree().then(response => {
        this.tableData = response.data
        this.listLoading = false
      })
    },

    handleSort(id, sort) {
      sortData(id, sort).then(() => {
        this.$notify({
          title: '成功',
          message: '排序成功！',
          type: 'success',
          duration: 2000
        })

        this.getList()
      })
    },

    handleSave() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        saveData(this.postForm).then(() => {
          this.$notify({
            title: '成功',
            message: '菜单保存成功！',
            type: 'success',
            duration: 2000
          })

          this.dialogFormVisible = false
          this.getList()
        })
      })
    },

    handleDelete(id) {
      // 删除
      this.$confirm('确实要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = []
        ids.push(id)

        deleteData(ids).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })

          this.getList()
        }).catch(err => {
          console.log(err)
        })
      })
    },
    iconSelect(icon) {
      this.postForm.metaIcon = icon
    },
    clearIcon() {
      this.postForm.metaIcon = ''
    }
  }
}
</script>
