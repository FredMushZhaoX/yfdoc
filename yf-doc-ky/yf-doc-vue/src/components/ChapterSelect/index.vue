<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    reserve-keyword
    clearable
    automatic-dropdown
    placeholder="选择章节"
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

import { fetchList } from '@/api/repo/chapter'

export default {
  name: 'ChapterSelect',
  props: {
    repoId: String,
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
    repoId: {
      handler(val) {
        this.fetchData(val)
      }
    }
  },
  created() {
    if (this.repoId) {
      this.fetchData(this.repoId)
    }
  },
  methods: {

    fetchData(val) {
      fetchList({ repoId: val }).then(response => {
        this.dataList = response.data
      })
    },
    handlerChange(e) {
      this.$emit('change', e)
      this.$emit('input', e)
    }
  }
}
</script>
