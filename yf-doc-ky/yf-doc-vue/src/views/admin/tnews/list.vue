<template>
  <div>
    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
    >

      <template slot="filter-content">

        <dic-list-select v-model="listQuery.params.newsStatus" dic-code="news_status" width="200" class="filter-item" style="margin-top: 3px" />
        <el-input v-model="listQuery.params.title" placeholder="搜索新闻标题" style="width: 200px;" class="filter-item" />
      </template>

      <template slot="data-columns">
        <el-table-column
          label="标题"
          prop="title"
        />

        <el-table-column
          label="新闻类别"
          prop="newsType_dictText"
          align="center"
        />

        <el-table-column
          label="缩略图"
          prop="imgUrl"
          align="center"
        >
          <template slot-scope="scope">
            <el-image
              style="width: 50px; height: 50px"
              :src="scope.row.imgUrl"
              :preview-src-list="[scope.row.imgUrl]"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="发布状态"
          prop="newsStatus_dictText"
          align="center"
        />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
        />

        <el-table-column
          label="操作"
          align="center"
          width="120px"
        >
          <template slot-scope="scope">
            <el-link v-if="scope.row.newsStatus === '0'" type="primary" icon="el-icon-upload2" style="margin-left: 10px" @click="fbNews(scope.row.id)">发布新闻</el-link>
            <el-link v-if="scope.row.newsStatus === '1'" type="primary" icon="el-icon-upload2" style="margin-left: 10px" @click="qxFbNews(scope.row.id)">取消发布</el-link>

          </template>
        </el-table-column>

      </template>

    </data-table>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import DicCatalogTree from '@/components/DicTreeSelect'
import DicListSelect from '@/components/DicListSelect'
import { fbNews, qxFbNews } from '@/api/tnews/tnews'
export default {
  name: 'ListNews',
  components: { DicListSelect, DicCatalogTree, DataTable },
  data() {
    return {

      listQuery: {
        current: 1,
        size: 10,
        params: {
          title: ''
        }
      },

      options: {

        // 可批量操作
        multi: true,

        add: {
          enable: !this.selectMode,
          permission: 'news:add',
          router: { name: 'addNews' }
        },
        edit: {
          enable: true,
          permission: 'news:update',
          router: { name: 'addNews', params: { id: '$id' }}
        },
        delete: {
          enable: true,
          permission: 'news:delete',
          url: '/api/news/news/delete'
        },
        // 列表请求URL
        listUrl: '/api/news/news/paging'

      }

    }
  },
  methods: {
    fbNews: function(id) {
      this.$confirm('确认要发布吗？')
        .then(_ => {
          fbNews(id).then(() => {
            this.$notify({
              title: '成功',
              message: '发布成功！',
              type: 'success',
              duration: 2000
            })
            this.$refs.pagingTable.getList()
          })
        }).catch(_ => {

        })
    },
    qxFbNews: function(id) {
      this.$confirm('确认要取消发布吗？')
        .then(_ => {
          qxFbNews(id).then(() => {
            this.$notify({
              title: '成功',
              message: '取消发布成功！',
              type: 'success',
              duration: 2000
            })
            this.$refs.pagingTable.getList()
          })
        }).catch(_ => {

        })
    }
  }
}
</script>
