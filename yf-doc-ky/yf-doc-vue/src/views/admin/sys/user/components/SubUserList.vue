<template>

  <div class="app-container">

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
    >

      <template slot="filter-content">

        <role-select v-model="listQuery.params.roleIds" class="filter-item" style="width: 300px;" />
        <depart-tree v-model="listQuery.params.deptCode" class="filter-item" style="width: 300px;" />
        <el-input v-model="listQuery.params.userName" style="width: 200px" placeholder="搜索登录名或姓名" class="filter-item" />

      </template>

      <template slot="data-columns">

        <el-table-column
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          label="登录账号"
          prop="userName"
        />

        <el-table-column
          align="center"
          label="真实姓名"
          prop="realName"
        />

        <el-table-column
          align="center"
          label="归属部门"
          prop="deptCode_dictText"
        />

        <el-table-column
          align="center"
          label="角色"
          prop="roleNames"
        />

        <el-table-column
          align="center"
          label="状态"
        >

          <template slot-scope="scope">
            {{ scope.row.state | stateFilter }}
          </template>
        </el-table-column>

      </template>
    </data-table>

  </div>

</template>

<script>
import DataTable from '@/components/DataTable'
import RoleSelect from '@/components/RoleSelect'
import permission from '@/directive/permission'
import DepartTree from '@/components/DepartTreeSelect'

export default {
  name: 'SubUserList',
  directives: { permission },
  components: { DepartTree, DataTable, RoleSelect },
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
  props: {
    roleIds: Array,
    deptCode: String
  },
  data() {
    return {

      listQuery: {
        current: 1,
        size: 10,
        params: {
          roleId: '',
          deptCode: ''
        }
      },
      options: {
        multi: false,
        // 列表请求URL
        listUrl: '/api/sys/user/paging'
      }
    }
  },

  watch: {
    roleIds: {
      handler(val) {
        this.listQuery.params.roleIds = val
        this.$refs.pagingTable.getList()
      }
    },
    deptCode: {
      handler(val) {
        this.listQuery.params.deptCode = val
        this.$refs.pagingTable.getList()
      }
    }
  },

  created() {
    if (this.roleIds != null && this.roleIds.length > 0) {
      this.listQuery.params.roleIds = this.roleIds
    }

    if (this.deptCode != null && this.deptCode !== '') {
      this.listQuery.params.deptCode = this.deptCode
    }
  },

  methods: {

  }
}
</script>
