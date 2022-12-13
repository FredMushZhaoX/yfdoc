<template>

  <el-dialog
    title="选择图标"
    :visible.sync="dialogVisible"
    width="60%"
    top="5vh"
    :close-on-click-modal="false"
    @close="handleClose"
  >

    <div style="display: flex; flex-wrap: wrap;  justify-content: space-around">

      <div v-for="item in iconList" class="icon-item" @click="handleCheck(item)">
        <svg-icon :icon-class="item" />
      </div>
    </div>

  </el-dialog>

</template>

<script>

export default {
  name: 'IconDialog',
  components: { },
  props: {
    value: String,
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      iconList: [

        '404', 'guide', 'review', 'admin', 'hot', 'role', 'agreement', 'icon', 'search', 'bug', 'international', 'catalog', 'language', 'shopping', 'cert', 'learn', 'size', 'certificate', 'link', 'skill', 'chart', 'list', 'star', 'clipboard', 'live-list', 'statis', 'component', 'live-stat', 'stats-dots', 'configure', 'live', 'stats2', 'course', 'lock', 'study', 'dashboard', 'log', 'study1', 'dict', 'map', 'support', 'documentation', 'message', 'tab', 'drag', 'money', 'table', 'edit', 'nested', 'test', 'education', 'notice', 'theme', 'email', 'notify', 'topic', 'example', 'paper', 'training', 'excel', 'password', 'tree-table', 'exit-fullscreen', 'pdf', 'tree', 'eye-open', 'people', 'user', 'eye', 'peoples', 'water', 'file', 'qq', 'wechat', 'fire', 'query', 'zip', 'form', 'repo', 'fullscreen', 'results'

      ],
      selected: '',
      dialogVisible: false
    }
  },

  watch: {
    // 检测查询变化
    visible: {
      handler() {
        this.dialogVisible = this.visible
      },
      deep: true
    }
  },

  created() {
    this.dialogVisible = this.visible
  },

  methods: {

    handleClose() {
      this.$emit('update:visible', false)
      this.$emit('close', this.selected)
    },

    handleConfirm() {
      this.$emit('update:visible', false)
      this.$emit('select', this.selected)
    },

    handleCheck(item) {
      this.selected = item
      this.handleConfirm()
    }

  }
}
</script>

<style scoped>

  /deep/
  .el-dialog__body{
    padding: 0px;
  }

  .icon-item{

    width: 50px;
    height: 50px;
    padding: 10px;
    margin: 10px;
    cursor: pointer;
    color: #606266
  }

  .icon-item .svg-icon{
    width: 30px; height: 30px;
  }

  .icon-item :hover{
    color: #5cb6ff;
  }

</style>
