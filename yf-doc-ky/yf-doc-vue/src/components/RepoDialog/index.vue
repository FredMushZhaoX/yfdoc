<template>

  <el-dialog
    title="选择题库"
    :visible.sync="dialogVisible"
    width="60%"
    top="5vh"
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
            <el-input v-model="listQuery.params.title" style="width: 200px" placeholder="搜索课件名称" class="filter-item" />
            <el-button type="primary" class="filter-item" style="float:  right" @click="handleConfirm">{{ selectedLabel }}</el-button>
          </el-col>
        </el-row>

      </template>

      <template slot="data-columns">

        <el-table-column
          label="题库名称"
          align="center"
          prop="title"
        />

        <el-table-column
          label="试题数量"
          align="center"
          prop="quCount"
        />

      </template>

    </data-table>

  </el-dialog>

</template>

<script>
import DataTable from '@/components/DataTable'

export default {
  name: 'RepoDialog',
  components: { DataTable },
  props: {
    excludes: Array,
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {

      dialogVisible: false,
      selectedLabel: '请勾选题库',

      listQuery: {
        current: 1,
        size: 10,
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
        listUrl: '/api/repo/paging'
      }
    }
  },

  watch: {

    // 检测查询变化
    visible: {
      handler() {
        this.dialogVisible = this.visible
        if (this.dialogVisible && this.$refs.pagingTable !== undefined) {
          this.$refs.pagingTable.getList()
        }
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
    this.dialogVisible = this.visible
  },

  methods: {

    handleSelected(obj) {
      this.selectedList = obj.objs
      this.selectedLabel = '确认' + this.selectedList.length + '项'
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.$emit('close', this.selectedList)
    },

    handleConfirm() {
      this.$emit('update:visible', false)
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
