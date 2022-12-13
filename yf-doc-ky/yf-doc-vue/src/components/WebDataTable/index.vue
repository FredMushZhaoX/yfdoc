<template>
  <div class="app-container">

    <div class="filter-container">

      <slot name="filter-content" />

      <el-row>
        <el-col>
          <el-button v-if="options.addRoute" type="primary" icon="el-icon-plus" @click="handleAdd">添加</el-button>

          <el-button v-if="options.add && options.add.enable" v-permission="[options.add.permission]" size="small" type="primary" icon="el-icon-plus" @click="optAdd">添加</el-button>
          <el-button v-if="options.edit && options.edit.enable" v-permission="[options.edit.permission]" size="small" type="warning" icon="el-icon-edit" :disabled="selectedIds.length!==1 " @click="optEdit">修改</el-button>
          <el-button v-if="options.delete && options.delete.enable" v-permission="[options.delete.permission]" size="small" type="danger" icon="el-icon-delete" :disabled="selectedIds.length===0 " @click="optDelete">删除</el-button>

          <el-dropdown
            v-if="options.multiActions && options.multiActions.length>0 && selectedIds.length>0"
            size="small"
            style="margin-left: 10px"
            split-button
            @command="optAction"
          >
            批量操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item in options.multiActions" :command="item">{{ item.label }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

        </el-col>
      </el-row>

    </div>

    <el-table
      ref="table"
      v-loading="listLoading"
      :data="dataList.records"
      :row-key="getRowKeys"
      border
      fit
      highlight-current-row
      :header-cell-style="{'background':'#01CB94', 'color':'#fff', 'border-color':'#b7e1cf','font-weight':'bold', 'line-height':'32px'}"
      @sort-change="tableSortChange"
      @selection-change="handleSelection"
    >

      <el-table-column
        v-if="options.multi"
        :reserve-selection="true"
        align="center"
        type="selection"
        width="55"
      />

      <slot name="data-columns" />

    </el-table>

    <pagination v-show="dataList.total>0" :total="dataList.total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />
  </div>
</template>

<script>
import { fetchList, deleteData, changeState } from '@/api/common'
import Pagination from '@/components/Pagination'
import permission from '@/directive/permission/index.js' // 权限判断指令

export default {
  name: 'PagingTable',
  directives: { permission },
  components: { Pagination },
  // 组件入参
  props: {
    options: {
      type: Object,
      default: () => {
        return {
          keyId: 'id',
          add: {
            enable: false,
            permission: '',
            router: {}
          },
          edit: {
            enable: false,
            permission: '',
            router: {}
          },
          delete: {
            enable: false,
            permission: '',
            url: ''
          },
          // 批量操作
          multiActions: [],
          // 列表请求URL
          listUrl: '/api/',
          // 删除请求URL
          deleteUrl: '',
          // 启用禁用
          stateUrl: '',
          // 可批量操作
          multi: false
        }
      }
    },

    // 列表查询参数
    listQuery: {
      type: Object,
      default: () => {
        return {
          current: 1,
          size: 10,
          params: {},
          t: 0
        }
      }
    }
  },
  data() {
    return {
      // 接口数据返回
      dataList: {
        total: 0
      },
      // 数据加载标识
      listLoading: true,
      // 选定和批量操作
      selectedIds: [],
      selectedObjs: [],
      // 显示已中多少项
      selectedLabel: '',
      // 显示批量操作
      multiShow: false,
      // 批量操作的标识
      multiNow: ''
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

    // 获取选定的key
    getRowKeys(row) {
      if (this.options.keyId != null && this.options.keyId !== '') {
        return row[this.options.keyId]
      }

      return row['id']
    },

    // 添加
    optAdd() {
      if (!this.options.add || !this.options.add.enable) {
        return
      }
      // 跳转的
      if (this.options.add.router) {
        this.$router.push(this.options.add.router)
      }
      this.$emit('add')
    },
    optEdit() {
      if (!this.options.edit || !this.options.edit.enable) {
        return
      }

      // 跳转的
      if (this.options.edit.router) {
        const router = this.options.edit.router
        const params = router.params
        params.id = this.selectedIds[0]
        router.params = params
        this.$router.push(router)
      }
      this.$emit('edit', this.selectedIds[0], this.selectedObjs[0])
    },

    optDelete() {
      if (!this.options.delete || !this.options.delete.enable) {
        return
      }

      if (this.options.delete.url) {
        // 删除
        this.$confirm('确实要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteData(this.options.delete.url, this.selectedIds).then(() => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getList()
          })
        })
      }
      this.$emit('delete', this.selectedIds)
    },

    // 批处理回调
    optAction(item) {
      console.log('执行', item)

      if (item.value === 'enable') {
        // 修改状态
        changeState(item.url, this.selectedIds, 0).then(response => {
          if (response.code === 0) {
            this.$message({
              type: 'success',
              message: '状态修改成功!'
            })

            // 重新搜索
            this.getList()
          }
        })
        return
      }

      if (item.value === 'disable') {
        // 修改状态
        changeState(item.url, this.selectedIds, 1).then(response => {
          if (response.code === 0) {
            this.$message({
              type: 'success',
              message: '状态修改成功!'
            })

            // 重新搜索
            this.getList()
          }
        })
        return
      }

      // 向外回调的操作
      this.$emit('multi-actions', { opt: item.value, ids: this.selectedIds, objs: this.selectedObjs })
    },

    /**
     * 添加数据跳转
     */
    handleAdd() {
      if (this.options.addRoute) {
        this.$router.push({ name: this.options.addRoute, params: {}})
        return
      }
      console.log('未设置添加数据跳转路由！')
    },

    /**
     * 查询数据列表
     */
    getList() {
      this.listLoading = true
      this.listQuery.t = new Date().getTime()
      fetchList(this.options.listUrl, this.listQuery).then(response => {
        this.dataList = response.data
        this.listLoading = false
      })
    },

    /**
     * 搜索
     */
    handleFilter() {
      // 重新搜索
      this.getList()
    },

    /**
     * 批量操作回调
     */
    handleOption(v) {
      this.multiNow = ''

      // 内部消化的操作
      if (v === 'delete') {
        this.handleDelete()
        return
      }

      if (v === 'enable') {
        this.handleState(0)
        return
      }

      if (v === 'disable') {
        this.handleState(1)
        return
      }

      // 向外回调的操作
      this.$emit('multi-actions', { opt: v, ids: this.selectedIds })
    },

    /**
     * 修改状态，启用禁用
     */
    handleState(state) {
      // 修改状态
      changeState(this.options.stateUrl, this.selectedIds, state).then(response => {
        if (response.code === 0) {
          this.$message({
            type: 'success',
            message: '状态修改成功!'
          })

          // 重新搜索
          this.getList()
        }
      })
    },

    /**
     * 删除数据
     */
    handleDelete() {
      if (this.selectedIds.length === 0) {
        this.$message({
          message: '请至少选择一条数据！',
          type: 'warning'
        })
        return
      }

      // 删除
      this.$confirm('确实要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData(this.options.deleteUrl, this.selectedIds).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getList()
        })
      })
    },

    /**
     * 列表多选操作
     * @param val
     */
    handleSelection(val) {
      const ids = []
      val.forEach(row => {
        ids.push(row.id)
      })

      this.selectedObjs = val
      this.selectedIds = ids
      this.multiShow = ids.length > 0
      this.selectedLabel = '已选' + ids.length + '项'

      this.$emit('select-changed', { ids: this.selectedIds, objs: this.selectedObjs })
    },

    tableSortChange(column) {
      this.listQuery.pageNo = 1
      if (column.order === 'descending') {
        this.listQuery.orders = [{ column: column.prop, asc: false }]
      } else {
        this.listQuery.orders = [{ column: column.prop, asc: true }]
      }
      this.getList()
    },

    // 清理选择的
    clearSelection() {
      this.$refs.table.clearSelection()
    }

  }
}
</script>

<style scoped>

  /deep/
  .filter-container .filter-item{
    margin-left: 10px;
  }

  /deep/
  .filter-container .filter-item:first-child{
    margin-left: 0px;
  }
</style>
