<template>

  <div v-loading="listLoading">

    <el-card class="search-area" shadow="hover" style="background: #F6F8FD; border-radius: 0px; padding: 10px 0px 10px 0px">
      <div class="filter-container">

        <slot
          name="search-bar"
        />
      </div>
    </el-card>

    <slot
      v-for="(item,index) in dataList.records"
      name="list-item"
      :data="item"
      :index="index"
    />

    <empty-view v-if="(!dataList.records || dataList.records.length===0) &!listLoading" type="said" msg="很好，这里空空如也！" />

    <pagination v-show="dataList.total>0" :total="dataList.total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

  </div>

</template>

<script>
import { fetchList } from '@/api/common'
import Pagination from '@/components/Pagination'
import EmptyView from '@/components/EmptyView'

export default {
  name: 'DataList',
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
    padding: 5px 20px 5px 20px
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

</style>

