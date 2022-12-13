<template>

  <el-dialog
    title="选择用户"
    :visible.sync="dialogVisible"
    width="60%"
    :close-on-click-modal="false"
    @close="handleClose"
  >

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @select-changed="handleSelected"
    >
      <template slot="filter-content">

        <el-row>
          <el-col :span="24">
            <depart-tree-select v-model="listQuery.params.deptCode" class="filter-item" style="width: 200px; margin-top: 3px" />
            <el-input v-model="listQuery.params.userName" style="width: 200px" placeholder="用户名/姓名" class="filter-item" />
            <el-button type="primary" class="filter-item" style="float:  right" @click="handleConfirm">{{ selectedLabel }}</el-button>

          </el-col>
        </el-row>

      </template>

      <template slot="data-columns">

        <el-table-column
          label="用户名"
          align="center"
          prop="userName"
        />

        <el-table-column
          label="姓名"
          align="center"
          prop="realName"
        />

      </template>

    </data-table>

  </el-dialog>

</template>

<script>
import DataTable from '@/components/DataTable'
import DepartTreeSelect from '@/components/DepartTreeSelect'

export default {
  name: 'UserSelectDialog',
  components: { DepartTreeSelect, DataTable },
  props: {
    excludes: Array,
    dialogShow: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      selectedLabel: '请勾选用户',
      listQuery: {
        current: 1,
        size: 6,
        params: {
          excludes: []
        }
      },
      // 选定的数据列表
      selectedList: [],
      options: {
        // 可批量操作
        multi: true,
        multiShow: false,
        // 列表请求URL
        listUrl: '/api/sys/user/paging'
      }
    }
  },

  watch: {

    // 检测查询变化
    dialogShow: {
      handler() {
        this.dialogVisible = this.dialogShow
      },
      deep: true
    },

    excludes: {
      handler() {
        this.listQuery.params.excludes = this.excludes
      },
      deep: true
    }
  },

  created() {
    this.dialogVisible = this.dialogShow
  },

  methods: {

    handleSelected(obj) {
      this.selectedList = obj.ids
      this.selectedLabel = '确认' + this.selectedList.length + '项'
    },

    handleClose() {
      this.$emit('update:dialogShow', false)
    },

    handleConfirm() {
      if (this.selectedList === null || this.selectedList.length === 0) {
        this.$message({
          type: 'error',
          message: '请至少勾选一个用户！!'
        })
        return
      }

      this.$emit('update:dialogShow', false)
      this.$emit('select', this.selectedList)
    }

  }
}
</script>

<style scoped>

  /deep/
  .el-dialog__body{
    padding: 0px;
  }
</style>
