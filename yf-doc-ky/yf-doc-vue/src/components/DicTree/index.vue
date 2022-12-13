<!-- 树状选择器 -->
<template>
  <el-tree
    ref="tree"
    :data="treeData"
    default-expand-all
    node-key="id"
    highlight-current
    :close-on-click-modal="false"
    :props="props"
    @node-click="onClickNode"
  />
</template>

<script>
import { fetchTree } from '@/api/sys/dict/value'

export default {
  name: 'DicTree',
  props: {
    // 接收绑定参数
    value: String,
    dicCode: String,
    // 树节点配置选项
    props: {
      type: Object,
      required: false,
      default: () => ({
        parent: 'parentId',
        value: 'id',
        label: 'title',
        children: 'children'
      })
    }
  },
  data() {
    return {
      treeData: []
    }
  },
  created() {
    fetchTree({ dicCode: this.dicCode }).then(res => {
      this.treeData = res.data
    })
  },
  methods: {

    // 单击节点
    onClickNode(node) {
      this.$emit('input', node.id)
    }
  }
}
</script>
