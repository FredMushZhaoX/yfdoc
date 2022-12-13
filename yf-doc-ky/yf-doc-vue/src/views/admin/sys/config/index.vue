<template>
  <div class="app-container">

    <el-tabs type="border-card">
      <el-tab-pane label="个性配置">
        <base-config />

      </el-tab-pane>
      <el-tab-pane label="存储配置">
        <upload-config />
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import UploadConfig from './upload'
import BaseConfig from './base'

export default {
  components: {  BaseConfig, UploadConfig }
}
</script>
