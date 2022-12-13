<template>
  <div class="app-container">

    <el-row>

      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>项目介绍</span>
          </div>
          <h1><a id="user-content-介绍" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E4%BB%8B%E7%BB%8D" />介绍</h1>
          <p>一款多角色在线文档管理系统，系统集成了用户管理、角色管理、部门管理、文档管理、新闻管理、问答管理、通告管理、文档全文检索。 支持常用的office文档，视频文件、PDF文档在线预览，下载，笔记，评论。</p>
          <h1><a id="user-content-技术栈" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E6%8A%80%E6%9C%AF%E6%A0%88" />技术栈</h1>
          <p>SpringBoot / Redis / Shiro / Vue / MySQL</p>
          <h1><a id="user-content-产品功能" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E4%BA%A7%E5%93%81%E5%8A%9F%E8%83%BD" />产品功能</h1>
          <h2><a id="user-content-系统完善完善的权限控制和用户系统" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E7%B3%BB%E7%BB%9F%E5%AE%8C%E5%96%84%E5%AE%8C%E5%96%84%E7%9A%84%E6%9D%83%E9%99%90%E6%8E%A7%E5%88%B6%E5%92%8C%E7%94%A8%E6%88%B7%E7%B3%BB%E7%BB%9F" />系统完善：完善的权限控制和用户系统</h2>
          <p>权限控制：基于Shiro和JWT开发的权限控制功能。<br>用户系统：用户管理、部门管理、角色管理等。</p>
          <h2><a id="user-content-多角色多角色支持" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E5%A4%9A%E8%A7%92%E8%89%B2%E5%A4%9A%E8%A7%92%E8%89%B2%E6%94%AF%E6%8C%81" />多角色：多角色支持</h2>
          <p>用户端：全文检索文档，新闻消息、知识问答、我的资料、系统公告、个人中心等。<br>
            1、全文检索可以通过检索文档内容查找相关资料。<br>
            2、查看管理发布的新闻消息详情。 <br>
            3、可以回复相关问答，也可以发布自己的问答，管理员审核通过之后可以进行回复。<br>
            4、可以上传自己的资料信息，管理员审核通过之后可以被检索。 管理端：文档管理、新闻管理、知识问答管理、系统公告管理、系统设置。</p>
          <h1><a id="user-content-环境要求" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E7%8E%AF%E5%A2%83%E8%A6%81%E6%B1%82" />环境要求</h1>
          <p>JDK 1.8+&nbsp;<a href="https://gitee.com/link?target=https%3A%2F%2Fcdn.yfhl.net%2Fjava-win%2Fjdk-8u181-windows-x64.exe">点此下载</a><br>Mysql5.7+&nbsp;<a href="https://gitee.com/link?target=https%3A%2F%2Fcdn.yfhl.net%2Fjava-win%2Fmysql-installer-community-5.7.31.0.msi">点此下载</a><br>LibreOffice&nbsp;<a href="https://gitee.com/link?target=https%3A%2F%2Fyf-commons-files.oss-cn-beijing.aliyuncs.com%2Fjava-win%2FLibreOffice_7.2.4_Win_x64.msi">点此下载</a></p>
          <h1><a id="user-content-快速运行" class="anchor" href="https://gitee.com/itiyan/yfdoc#%E5%BF%AB%E9%80%9F%E8%BF%90%E8%A1%8C" />快速运行</h1>
          <p>1、自行安装MySQL数据库（版本最好大于5.7），将<code>安装资源中</code>的<code>yf_doc_ky.sql</code>导入到安装好的数据库<br>2、安装Java环境，要求JDK版本大于1.7<br>3、安装LibreOffice 用于文件转换在线预览,注意安装目录不能有中文 4、请修改外置配置文件：application-local.yml 改成您自己的MySQL配置，修改LibreOffice安装目录<br>5、Windows通过start.bat运行，Linux运行start.sh运行<br>6、如果无意外，可通过：<a href="https://gitee.com/link?target=http%3A%2F%2Flocalhost%3A8101">http://localhost:8101</a>&nbsp;访问到项目了<br>7、管理员账号密码：admin/admin 学员账号：student/student</p>
          <p>注意：运行的时候运行包所在目录不能包含中文目录，会导致LibreOffice启动失败。 （快速运行的jar包在官方QQ群(537163131)文件的开发部署文档.zip中下载）</p>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>项目演示</span>
          </div>
          <h1>商业版本</h1>
          访问地址：<a href="https://doc.yfhl.net/">https://doc.yfhl.net/</a>  <br>
          <img src="/h5.png" width="200px">
          <img src="/xcx.jpeg" width="200px">
          <h1>其它支持</h1>
          <p>QQ交流群：537163131<br>邮箱：<a href="mailto:626264481@qq.com">626264481@qq.com</a><br>手机：18710213152<br>网站：<a href="https://www.jeedocm.com/">https://www.jeedocm.com/</a></p>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>

export default {
  components: { },
  data() {
    return {

    }
  },
  computed: {

  },

  created() {

  },
  methods: {

  }
}
</script>

<style scoped>
.box-card {
  width: 95%;
}
</style>
