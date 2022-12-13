<template>
  <div>
    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
    >

      <template slot="filter-content">

        <dic-catalog-tree v-model="listQuery.params.docType" dic-code="document_type" width="200" class="filter-item" style="margin-top: 3px" />
        <dic-list-select v-model="listQuery.params.status" dic-code="doc_status" />
        <el-input v-model="listQuery.params.title" placeholder="搜索资料名称" style="width: 200px;" class="filter-item" />
      </template>

      <template slot="data-columns">
        <el-table-column
          label="资料名称"
          prop="title"
        />

        <el-table-column
          label="资料分类"
          prop="docType_dictText"
          align="center"
        />

        <el-table-column
          label="资料来源"
          prop="docFrom"
          align="center"
        />
        <el-table-column
          label="关键字"
          prop="keyWord"
          align="center"
        />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
        />
        <el-table-column
          label="审核状态"
          align="center"
          prop="status_dictText"
        />
        <el-table-column
          label="操作"
          align="center"
          width="150px"
        >
          <template slot-scope="scope">
            <el-button size="mini" title="预览" type="primary" icon="el-icon-view" circle @click="handlePreview(scope.row)" />
            <el-button v-if="scope.row.status !='3'" size="mini" title="审核通过" type="success" icon="el-icon-success" circle @click="approved(scope.row.id)" />
            <el-button v-if="scope.row.status !='4'" size="mini" title="驳回" type="danger" icon="el-icon-error" circle @click="approvedFailed(scope.row.id)" />
          </template>
        </el-table-column>

      </template>

    </data-table>
    <el-dialog title="文件预览" :close-on-click-modal="false" :visible.sync="previewVisible" width="60%" top="4vh" @close="closePreview">
      <div style="max-height: 650px; overflow-x: hidden; overflow-y: scroll">
        <pdf-frame v-if="previewData.fileType==='11'" :src="previewData.viewUrl" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import DicCatalogTree from '@/components/DicTreeSelect'
import DicListSelect from '@/components/DicListSelect'
import PdfFrame from './components/PdfFrame'
import { approved, approvedFailed } from '@/api/document/document'
export default {
  name: 'DocList',
  components: { DicListSelect, DicCatalogTree, DataTable, PdfFrame },
  data() {
    return {

      listQuery: {
        current: 1,
        size: 10,
        params: {
          title: '',
          status: '',
          docType: ''
        }
      },

      options: {

        // 可批量操作
        multi: true,

        add: {
          enable: !this.selectMode,
          permission: 'document:add',
          router: { name: 'AddDoc' }
        },
        edit: {
          enable: true,
          permission: 'document:update',
          router: { name: 'AddDoc', params: { id: '$id' }}
        },
        delete: {
          enable: true,
          permission: 'document:delete',
          url: '/api/doc/doc/delete'
        },
        // 列表请求URL
        listUrl: '/api/doc/doc/paging'

      },

      previewVisible: false,
      previewData: {},
      approvedFailedData: { id: '', failureReason: '' }
    }
  },
  methods: {
    handlePreview(data) {
      this.previewVisible = true
      this.previewData.fileType = '11'
      this.previewData.viewUrl = data.viewUrl
      this.previewData.fileUrl = data.viewUrl
    },

    closePreview() {
      this.previewData = {}
    },
    approved(id) {
      this.$confirm('确定审核通过吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        approved(id).then(res => {
          this.$notify({
            title: '成功',
            message: '审核通过成功！',
            type: 'success',
            duration: 2000
          })
          this.$refs.pagingTable.getList()
        })
      }).catch(() => {

      })
    },

    approvedFailed(id) {
      this.approvedFailedData.id = id
      this.$confirm('确定驳回吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        approvedFailed(this.approvedFailedData).then(res => {
          this.$notify({
            title: '成功',
            message: '驳回成功！',
            type: 'success',
            duration: 2000
          })
          this.$refs.pagingTable.getList()
        })
      }).catch(() => {

      })
    }
  }
}
</script>
