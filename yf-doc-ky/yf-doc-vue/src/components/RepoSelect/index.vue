<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    reserve-keyword
    clearable
    automatic-dropdown
    placeholder="选择或搜索题库"
    :remote-method="fetchData"
    class="filter-item"
    @change="handlerChange"
  >
    <el-option
      v-for="item in dataList"
      :key="item.id"
      :label="item.title"
      :value="item.id"
    />
  </el-select>

</template>

<script>

import { fetchList } from '@/api/repo/repo'

export default {
  name: 'RepoSelect',
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
    this.fetchData()
  },
  methods: {

    fetchData(e) {
      fetchList({ current: 1, size: 100, params: { title: e, isExam: true }}).then(response => {
        this.dataList = response.data.records
      })
    },
    handlerChange(e) {
      const obj = this.dataList.find((item) => {
        return item.id === e
      })

      this.$emit('change', e, obj.title)
      this.$emit('input', e)
    }
  }
}
</script>
