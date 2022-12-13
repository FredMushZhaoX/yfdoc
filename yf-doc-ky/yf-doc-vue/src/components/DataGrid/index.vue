<template>

  <div v-loading="listLoading">

    <slot
      name="search-bar"
    />

    <el-row :gutter="20">

      <slot
        v-for="(item,index) in dataList.records"
        name="list-item"
        :data="item"
        :index="index"
      />

    </el-row>

    <empty-view v-if="!dataList.records || dataList.records.length===0" type="said" msg="很好，这里空空如也！" />

    <pagination v-show="dataList.total>0" :total="dataList.total" :page.sync="listQuery.current" :limit.sync="listQuery.size" style="margin-top: 30px" @pagination="getList" />

  </div>

</template>

<script>
import { fetchList } from '@/api/common'
import Pagination from '@/components/Pagination'
import EmptyView from '@/components/EmptyView'

export default {
  name: 'DataGrid',
  components: { EmptyView, Pagination },
  // 组件入参
  props: {
    options: Object,
    // 列表查询参数
    listQuery: {
      type: Object,
      default: () => {
        return {
          current: 1,
          size: 10,
          params: {},
          totalCount: 0,
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
      listLoading: true
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
     * 查询数据列表
     */
    getList() {
      this.listLoading = true
      this.listQuery.t = new Date().getTime()
      fetchList(this.options.listUrl, this.listQuery).then(response => {
        this.dataList = response.data
        this.listLoading = false
        this.listQuery.totalCount = response.data.total
      })
    },

    /**
     * 搜索
     */
    handleFilter() {
      // 重新搜索
      this.getList()
    }

  }
}
</script>

<style scoped>

/deep/
.el-card__body{
  padding: 5px 20px 5px 20px;

}

/deep/
.el-card{
  background: transparent !important;
  border: #DCDFE6 1px solid;
}

/deep/
.filter-container .filter-item{
  margin-left: 15px;
  width:200px;
  vertical-align: middle;
  margin-bottom: 0px;
}

/deep/
.filter-container .filter-item:first-child{
  margin-left: 0px;
}

/deep/
.filter-container {
  padding-bottom: 0px;
}

/deep/
.el-pagination{
  color: #e0e0e0 !important;
}

/deep/
.el-pagination__sizes{
  color: #e0e0e0 !important;
}

/deep/
.el-pagination__jump{
  color: #e0e0e0 !important;
}

/deep/
.el-pagination__total{
  color: #e0e0e0 !important;
}

/deep/
.pagination-container{
  background: transparent !important;
}

</style>

