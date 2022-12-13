<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    clearable
    placeholder="选择或搜索课程"
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

import { fetchPaging } from '@/api/course/course'

export default {
  name: 'CourseSelect',
  props: {
    multi: Boolean,
    value: String
  },
  data() {
    return {
      // 下拉选项值
      dataList: [],
      currentValue: []
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

    fetchData() {
      const params = { current: 1, size: 100, params: {}}
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
