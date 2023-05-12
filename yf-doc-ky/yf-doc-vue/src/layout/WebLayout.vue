<template>
  <el-container class="all-bg">
    <el-header style="padding: 0px; height: 60px">
      <web-header />
    </el-header>
    <el-main style="padding: 0px;">
      <el-row type="flex" justify="center" style="margin-top: 25px;">
        <el-col :span="20">
          <app-main class="main-bg" />
        </el-col>
      </el-row>
    </el-main>
    <el-footer v-if="siteData.copyRight" style="padding: 0px; margin-top: 25px">

      <el-row type="flex" style="height: 60px; color: #A3A8B0; align-items: center; font-size: 14px; background: #333">
        <el-col :span="24" style="text-align: center" v-html="siteData.copyRight" />
      </el-row>
    </el-footer>
  </el-container>

</template>

<script>
import { mapGetters } from 'vuex'
import { AppMain, WebHeader } from './components'

export default {
  name: 'WebLayout',
  components: {
    AppMain,
    WebHeader
  },
  computed: {
    ...mapGetters([
      'siteData'
    ])
  }
}
</script>

<style scoped>

  html{
    line-height: normal;
  }

  /deep/
  .app-container{
    padding: 0px;
  }

  /deep/
  .el-card{
    border-radius: 0px;
  }

  .all-bg{
    background: #F7F9FC url("../assets/web/bg.png") no-repeat;
  }

  .app-main {
    /* 50= navbar  50  */
    min-height: calc(100vh - 145px);
    width: 100%;
    position: relative;
    overflow: hidden;
  }

  /deep/
  .el-pagination{
    text-align: center;
  }

  /deep/
  .el-card__header{
    font-size: 14px;
    font-weight: 700;
  }
  /deep/
  .el-card__body{
    font-size: 14px;
    line-height: 24px;
  }

  /deep/
  .el-tabs__item.is-active {
    color: #ECB507
  }

  /deep/
  .el-tabs__active-bar{
    background-color: #ECB507
  }

  .main-bg{
    padding: 20px;
    background: #fff;
  }

</style>

