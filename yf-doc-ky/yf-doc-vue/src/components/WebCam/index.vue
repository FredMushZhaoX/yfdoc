<template>

  <vue-web-cam
    ref="webcam"
    :device-id="deviceId"
    width="100%"
    height="auto"
    @started="onStarted"
    @stopped="onStopped"
    @error="onError"
    @cameras="onCameras"
    @camera-change="onCameraChange"
  />

</template>

<script>
import { WebCam } from 'vue-web-cam'

export default {
  name: 'MyWebCam',
  components: {
    'vue-web-cam': WebCam
  },
  data() {
    return {
      camera: null,
      deviceId: null,
      devices: []
    }
  },

  computed: {
    device: function() {
      return this.devices.find(n => n.deviceId === this.deviceId)
    }
  },

  watch: {

    camera: function(id) {
      this.deviceId = id
    },
    devices: function() {
      // 默认使用第一个相机
      const arr = this.devices
      if (arr != null && arr.length > 0) {
        this.camera = arr[0].deviceId
        this.deviceId = arr[0].deviceId
      }
    }
  },

  methods: {

    // 截屏回调
    onCapture() {
      console.log('Start Capture')
      const img = this.$refs.webcam.capture()
      const file = this.base64ToFile(img, 'capture.jpg')
      this.$emit('capture', file)
    },

    // base64转文件流
    base64ToFile(urlData, fileName) {
      const arr = urlData.split(',')
      const mime = arr[0].match(/:(.*?);/)[1]
      const bytes = atob(arr[1])
      let n = bytes.length
      const ia = new Uint8Array(n)
      while (n--) {
        ia[n] = bytes.charCodeAt(n)
      }
      return new File([ia], fileName, { type: mime })
    },

    onStarted(stream) {
      console.log('On Started Event', stream)
      this.$emit('started', stream)
    },
    onStopped(stream) {
      console.log('On Stopped Event', stream)
      this.$emit('stopped', stream)
    },
    onError(error) {
      console.log('On Error Event', error)
      this.$emit('error', error)
      this.$message.error('摄像头开启失败，请确认电脑有摄像头且授权网页使用！')
    },
    onCameras(cameras) {
      this.devices = cameras
      console.log('On Cameras Event', cameras)
    },
    onCameraChange(deviceId) {
      this.deviceId = deviceId
      this.camera = deviceId
      console.log('On Camera Change Event', deviceId)
    }

  }
}
</script>

