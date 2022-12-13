<template>

  <div class="login-container">

    <el-row :gutter="20" class="top">
      <el-col :span="18" :offset="3">
        <div style="display: flex; flex-direction: row; align-items: center">
          <div v-if="siteData.frontLogo"><img :src="siteData.frontLogo"></div>
          <div style="font-weight: 700; font-size: 26px; color: #eee; flex-grow: 1; text-align: left; padding-left: 10px">{{siteData.siteName}}</div>
        </div>

      </el-col>

    </el-row>

    <el-row :gutter="20" class="content">
      <el-col :span="18" :offset="3">
          <app-main />
      </el-col>

    </el-row>


    <el-row :gutter="20" class="footer">
      <el-col :span="18" :offset="3">
        <div v-html="siteData.copyRight"></div>
      </el-col>
    </el-row>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'
import AppMain from '@/layout/components/AppMain'

export default {
  name: 'LoginLayout',
  components: { AppMain },
  computed: {
    ...mapGetters([
      'siteData'
    ])
  }
}
</script>

<style scoped>
@import "~@/styles/login.css";

 /deep/
 .app-main {
   min-height: 84vh;
   width: 100%;
 }
</style>
