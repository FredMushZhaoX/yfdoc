<template>

  <el-select
    v-model="currentValue"
    filterable
    remote
    :disabled="disabled"
    reserve-keyword
    clearable
    automatic-dropdown
    :placeholder="title"
    :remote-method="fetchData"
    class="filter-item"
    @change="handlerChange"
  >
    <el-option
      v-for="item in dataList"
      :key="item.value"
      :label="item.title"
      :value="item.value"
    />
  </el-select>

</template>

<script>

import { fetchTree } from '@/api/sys/dict/value'

export default {
  name: 'DicListSelect',
  props: {
    dicCode: String,
    title: String,
    value: String,

    disabled: Boolean
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
      fetchTree({ dicCode: this.dicCode }).then(response => {
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
