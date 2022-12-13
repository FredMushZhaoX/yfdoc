<template>

  <div>

    <el-row type="flex" class="header-bg" justify="center">

      <el-col :span="20">

        <el-row>

          <el-col :span="6" class="col-logo">

            <div style="display: flex; flex-direction: row; align-items: center">
              <div v-if="siteData.frontLogo">
                <img :src="siteData.frontLogo" :alt="siteData.siteName" style="height: 40px;">
              </div>
              <div style="font-weight: 700; font-size: 20px; color: #eee; flex-grow: 1; text-align: left; padding-left: 10px">{{ siteData.siteName }}</div>
            </div>

          </el-col>

          <el-col :span="18">

            <el-row>

              <el-col :span="18" style="line-height: 60px">
                <el-button icon="el-icon-s-home" :class="isActive('/web/index')" size="small" round @click="navClick('/web/index')">首页</el-button>
                <el-button icon="el-icon-news" :class="isActive('/web/newslist')" size="small" round @click="navClick('/web/newslist')">新闻消息</el-button>
                <el-button icon="el-icon-document" :class="isActive('/web/doc')" size="small" round @click="navClick('/web/doc')">我的资料</el-button>
                <el-button icon="el-icon-message" :class="isActive('/web/notice')" size="small" round @click="navClick('/web/notice')">系统公告</el-button>
              </el-col>
              <el-col :span="6" class="right-user">
                <el-image :src="avatar" class="top-avatar">
                  <div slot="error">
                    <img src="@/assets/web/avatar.png">
                  </div>
                </el-image>
                <a @click="handleUc">{{ realName }}</a>
                <a @click="logout">退出</a>
              </el-col>

            </el-row>

          </el-col>

        </el-row>

      </el-col>

    </el-row>

  </div>

</template>

<script>
import { mapGetters } from 'vuex'

export default {
  computed: {
    ...mapGetters([
      'avatar',
      'realName',
      'siteData'
    ])
  },
  data() {
    return {
      activeIndex: '/web/index'
    }
  },
  created() {
    const activeMenu = this.$route.meta.activeMenu

    if (activeMenu) {
      this.activeIndex = activeMenu
      return
    }

    const path = this.$route.path.split('/')
    const prefix = path[0] + '/' + path[1] + '/' + path[2]
    console.log(prefix)
    this.activeIndex = prefix
  },

  methods: {

    isActive(url) {
      if (this.activeIndex === url) {
        return 'nav active'
      }
      return 'nav'
    },

    navClick(url) {
      this.activeIndex = url
      this.$router.push({ path: url })
    },

    async logout() {
      const that = this

      this.$confirm('确实要退出吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        that.$store.dispatch('user/logout').then(() => {
          that.$router.push(`/login?redirect=${this.$route.fullPath}`)
        })
      }).catch(() => {

      })
    },

    handleUc() {
      this.$router.push({ name: 'UserCenter' })
    }
  }
}
</script>

<style scoped>
  .header-bg{
    height: 60px;
    background: #01CB94;
  }

.right-user{
  display: flex;
  justify-content: flex-end;
  flex-direction: row;
  line-height: 60px;
  align-items: center;
}

.right-user :nth-child(1), .right-user :nth-child(2){
  margin-right: 10px;
}

.right-user a{
  color: #efefef;
  font-size: 14px;
  font-weight: 500;
}

.right-user a:first-child{
  margin-right: 10px;
}

.right-user a:hover{
  color: #ffd550;
}

.nav{
  color: #fff;
  border: none;
  background: transparent;
  font-weight: 500;
  font-size: 14px;
  padding: 5px 10px 5px 10px;
  margin-right: 20px;
}

  .active{
    color: #000055;
    background: #FFD550;
  }

  .nav:hover {
    color: #000055;
    background: #FFD550;
  }

.col-logo{

  display: flex;
  align-items: center;
  justify-content: flex-start;
  height: 60px;
}

/deep/
.top-avatar{
  text-align: right;
  display: flex;
  align-items: center;
  margin-right: 5px !important;

}

/deep/
.top-avatar div{
  display: flex;
  align-items: center;
  margin-right: -10px !important;
}

/deep/
.top-avatar img{
  width: 30px; height: 30px; border-radius: 15px;
}

</style>
