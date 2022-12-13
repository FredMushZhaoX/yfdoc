<template>

  <el-select
    v-model="roleIds"
    style="width: 100%"
    filterable
    remote
    multiple
    reserve-keyword
    clearable
    automatic-dropdown
    placeholder="请选择角色"
    :remote-method="fetchList"
    @change="handlerChange"
  >
    <el-option
      v-for="item in list"
      :key="item.id"
      :label="item.roleName"
      :value="item.id"
    />
  </el-select>

</template>

<script>

import { fetchList } from '@/api/sys/role/role'

export default {
  name: 'RoleSelect',
  props: {
    value: Array
  },
  data() {
    return {
      // 下拉选项值
      list: [],
      roleIds: []
    }
  },

  watch: {
    // 检测查询变化
    value: {
      handler() {
        this.roleIds = this.value
      },
      deep: true
    }
  },
  created() {
    this.roleIds = this.value
    this.fetchList()
  },
  methods: {

    fetchList() {
      fetchList().then(response => {
        this.list = response.data
      })
    },
    handlerChange(e) {
      this.$emit('change', e)
      this.$emit('input', e)
    }
  }
}
</script>
