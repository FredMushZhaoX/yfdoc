<template>
  <div class="empty-box">
    <img v-if="type === 'said'" src="@/assets/web/images/empty_said.png">
    <img v-if="type === 'empty'" src="@/assets/web/images/empty.png">
    <span>{{ msg }}</span>
  </div>
</template>

<script>

export default {
  name: 'EmptyView',
  props: {
    type: String,
    msg: String
  }
}

</script>

<style scoped>
.empty-box{
  height: 40vh;
  font-size: 14px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  line-height: 50px;
  color: #bfbfbf;

}

.empty-box img{
  width: 80px;
}

.empty-box span{
  font-weight: 700;
  font-size: 16px;
}
</style>
