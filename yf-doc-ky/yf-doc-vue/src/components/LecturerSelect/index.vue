<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    clearable
    allow-create
    default-first-option
    placeholder="选择或搜索讲师"
    :remote-method="fetchData"
    class="filter-item"
    @change="handlerChange"
  >
    <el-option
      v-for="item in dataList"
      :key="item.id"
      :label="item.name"
      :value="item.id"
    />
  </el-select>

</template>

<script>

import { fetchList } from '@/api/course/lecturer'

export default {
  name: 'LecturerSelect',
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
    }
  },
  created() {
    this.currentValue = this.value
    this.fetchData('')
  },
  methods: {

    fetchData(v) {
      fetchList({ current: 1, size: 10, params: { name: v }}).then(response => {
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
