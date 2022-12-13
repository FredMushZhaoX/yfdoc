<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    clearable
    placeholder="选择或搜索用户"
    :remote-method="fetchData"
    class="filter-item"
    @change="handlerChange"
  >
    <el-option
      v-for="item in dataList"
      :key="item.id"
      :label="item.realName"
      :value="item.id"
    />
  </el-select>

</template>

<script>

import { fetchPaging } from '@/api/sys/user/user'

export default {
  name: 'UserSelect',
  props: {
    value: String
  },
  data() {
    return {
      // 下拉选项值
      dataList: [],
      currentValue: ''
    }
  },

  watch: {
    // 检测查询变化
    value: {
      handler() {
        this.currentValue = this.value
      }
    },
    examType: {
      handler() {
        this.fetchData()
      }
    }
  },
  created() {
    this.currentValue = this.value
    this.fetchData()
  },
  methods: {

    fetchData(e) {
      const params = { current: 1, size: 100, params: { userName: e }}
      fetchPaging(params).then(response => {
        this.dataList = response.data.records
      })
    },
    handlerChange(e) {
      console.log(e)

      this.$emit('change', e)
      this.$emit('input', e)
    }
  }
}
</script>
