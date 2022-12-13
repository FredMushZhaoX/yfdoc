<template>
  <el-tooltip class="tip-item" effect="dark" :content="msg" placement="top">
    <i class="el-icon-question" style="font-size: 18px" />
  </el-tooltip>
</template>

<script>

export default {
  name: 'ToolTip',
  props: {
    msg: String
  }
}

</script>

<style scoped>
.tip-item{
  color: #409EFF;
  padding-left: 10px;
}

</style>
