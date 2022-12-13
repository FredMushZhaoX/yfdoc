<template>

  <el-dialog
    title="选择课件"
    :visible.sync="dialogVisible"
    width="80%"
    top="5vh"
    :close-on-click-modal="false"
    @close="handleClose"
  >

    <el-row :gutter="20" class="app-container">
      <el-col :span="6">

        <el-card>
          <dic-tree v-model="listQuery.params.catId" dic-code="course_file_catalog" />
        </el-card>

      </el-col>
      <el-col :span="18">

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
              label="课件名称"
              align="center"
              prop="title"
            />

            <el-table-column
              label="课件类型"
              align="center"
              prop="fileType_dictText"
            />

          </template>

        </data-table>

      </el-col>
    </el-row>

  </el-dialog>

</template>

<script>
import DataTable from '@/components/DataTable'
import DicTree from '../DicTree/index'

export default {
  name: 'CourseFileDialog',
  components: { DicTree, DataTable },
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
      selectedLabel: '请勾选课件',

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
        listUrl: '/api/course/file/paging'
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
