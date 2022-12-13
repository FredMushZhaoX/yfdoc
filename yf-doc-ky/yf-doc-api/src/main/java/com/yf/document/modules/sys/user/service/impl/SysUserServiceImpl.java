package com.yf.document.modules.sys.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.ApiError;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.api.enums.CommonState;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.boot.base.api.utils.passwd.PassHandler;
import com.yf.boot.base.api.utils.passwd.PassInfo;
import com.yf.document.ability.Constant;
import com.yf.document.ability.captcha.service.CaptchaService;
import com.yf.document.ability.redis.service.RedisService;
import com.yf.document.ability.shiro.jwt.JwtUtils;
import com.yf.document.config.BaseConfig;
import com.yf.document.modules.sys.depart.service.SysDepartService;
import com.yf.document.modules.sys.log.service.SysLogActiveService;
import com.yf.document.modules.sys.user.dto.SysUserDTO;
import com.yf.document.modules.sys.user.dto.request.*;
import com.yf.document.modules.sys.user.dto.response.SysUserLoginDTO;
import com.yf.document.modules.sys.user.dto.response.UserExportDTO;
import com.yf.document.modules.sys.user.dto.response.UserListRespDTO;
import com.yf.document.modules.sys.user.entity.SysRole;
import com.yf.document.modules.sys.user.entity.SysUser;
import com.yf.document.modules.sys.user.entity.SysUserBind;
import com.yf.document.modules.sys.user.mapper.SysUserMapper;
import com.yf.document.modules.sys.user.service.SysUserBindService;
import com.yf.document.modules.sys.user.service.SysUserRoleService;
import com.yf.document.modules.sys.user.service.SysUserService;
import com.yf.document.modules.sys.user.utils.SignUtils;
import com.yf.document.modules.user.UserUtils;
import com.yf.document.utils.CacheKey;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* <p>
* 语言设置 服务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-13 16:57
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private BaseConfig baseConfig;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaptchaService captchaService;


    @Autowired
    private SysLogActiveService sysLogActiveService;

    @Autowired
    private SysUserBindService sysUserBindService;

    @Autowired
    private SysDepartService sysDepartService;


    private static final String ROLE_STUDENT = "sa";
    private static final String DEFAULT_PASS = "123456";


    @Override
    public IPage<UserListRespDTO> paging(PagingReqDTO<SysUserQueryReqDTO> reqDTO) {

        //创建分页对象
        Page page = reqDTO.toPage();

        //转换结果
        IPage<UserListRespDTO> pageData = baseMapper.paging(page, reqDTO.getParams());
        return pageData;
     }

    @Override
    public SysUserLoginDTO login(SysUserLoginReqDTO reqDTO) {

        // 校验图形验证码
        if(!StringUtils.isBlank(reqDTO.getCaptchaKey())) {
            boolean check = captchaService.checkCaptcha(reqDTO.getCaptchaKey(), reqDTO.getCaptchaValue());
            if (!check) {
                throw new ServiceException("图形验证码不正确或已失效！");
            }
        }

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUserName, reqDTO.getUsername());
        SysUser user = this.getOne(wrapper, false);

        // 校验用户状态&密码
        return this.checkAndLogin(user,  reqDTO.getPassword());
    }

    @Override
    public SysUserLoginDTO mobileLogin(MobileLoginReqDTO reqDTO) {

        // 校验短信
        boolean check = false;

        if(!check){
            throw new ServiceException("短信验证码错误，请确认！");
        }

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getMobile, reqDTO.getMobile());
        SysUser user = this.getOne(wrapper, false);

        // 校验用户状态
        return this.checkAndLogin(user, null);
    }


    /**
     * 用户登录校验
     * @param user
     */
    private SysUserLoginDTO checkAndLogin(SysUser user, String password){

        if(user == null){
            throw new ServiceException(ApiError.ERROR_90010001);
        }

        // 被禁用
        if(user.getState().equals(CommonState.ABNORMAL)){
            throw new ServiceException(ApiError.ERROR_90010005);
        }

        if(!StringUtils.isBlank(password)){
            boolean pass = PassHandler.checkPass(password , user.getSalt(), user.getPassword());
            if(!pass){
                throw new ServiceException(ApiError.ERROR_90010002);
            }
        }

        return this.setToken(user);
    }

    @Cacheable(value = CacheKey.TOKEN, key = "#token")
    @Override
    public SysUserLoginDTO token(String token) {

        // 获得会话
        String username;
        try {
            username = JwtUtils.getUsername(token);
        } catch (Exception e) {
            throw new ServiceException("会话失效，请重新登录！");
        }

        JSONObject json = redisService.getJson(Constant.USER_NAME_KEY+username);
        if(json == null){
            throw new ServiceException(ApiError.ERROR_10010002);
        }


        SysUserLoginDTO respDTO = json.toJavaObject(SysUserLoginDTO.class);

        // T下线的操作
        if(baseConfig.getLoginTick()){
            if(!token.equals(respDTO.getToken())){
                throw new ServiceException("您的账号在其他地方登录了！");
            }
        }

        // 填充积分信息
        SysUser user = this.getById(respDTO.getId());

        if(user == null){
            // 可能是脏的用户数据
            throw new ServiceException(ApiError.ERROR_10010002);
        }

        // 记录活跃
        sysLogActiveService.merge(respDTO.getId());
        respDTO.setPoints(user.getPoints());
        return respDTO;
    }

    @CacheEvict(value = CacheKey.TOKEN, key = "#token")
    @Override
    public void logout(String token) {

        if(baseConfig.getLoginTick()){
            try {
                String username = JwtUtils.getUsername(token);
                String [] keys = new String[]{Constant.USER_NAME_KEY+username};
                redisService.del(keys);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(SysUserDTO reqDTO) {


        SysUser user = this.getById(UserUtils.getUserId());

        // 修改密码
        String pass = reqDTO.getPassword();
        if(!StringUtils.isBlank(pass)){
            PassInfo passInfo = PassHandler.buildPassword(pass);
            user.setPassword(passInfo.getPassword());
            user.setSalt(passInfo.getSalt());
            this.updateById(user);

            // 退出登录
            String [] keys = new String[]{Constant.USER_NAME_KEY+user.getUserName()};
            redisService.del(keys);
        }

        String avatar = reqDTO.getAvatar();
        if(!StringUtils.isBlank(avatar)) {
            user.setAvatar(avatar);
            this.updateById(user);
            this.setToken(user);
        }


    }

    @Override
    public void pass(SysUserPassReqDTO reqDTO) {

        // 获取当前用户
        SysUser user = this.getById(UserUtils.getUserId());

        boolean check = PassHandler.checkPass(reqDTO.getOldPassword(), user.getSalt(), user.getPassword());
        if(!check){
            throw new ServiceException(ApiError.ERROR_90010007);
        }

        PassInfo passInfo = PassHandler.buildPassword(reqDTO.getPassword());
        user.setPassword(passInfo.getPassword());
        user.setSalt(passInfo.getSalt());
        this.updateById(user);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SysUserSaveReqDTO reqDTO) {

        List<String> roles = reqDTO.getRoles();

        if(CollectionUtils.isEmpty(roles)){
            throw new ServiceException(ApiError.ERROR_90010003);
        }


        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUserName, reqDTO.getUserName());
        if(!StringUtils.isBlank(reqDTO.getId())){
            wrapper.lambda().ne(SysUser::getId, reqDTO.getId());
        }

        int count = this.count(wrapper);
        if(count > 0){
            throw new ServiceException("用户名不能重复！");
        }


        // 保存基本信息
        SysUser user = new SysUser();
        BeanMapper.copy(reqDTO, user);

        // 添加模式
        if(StringUtils.isBlank(user.getId())){
            user.setId(IdWorker.getIdStr());
        }

        // 修改密码
        if(!StringUtils.isBlank(reqDTO.getPassword())){
            PassInfo pass = PassHandler.buildPassword(reqDTO.getPassword());
            user.setPassword(pass.getPassword());
            user.setSalt(pass.getSalt());
        }

        // 保存角色信息
        sysUserRoleService.saveRoles(user.getId(), roles);
        this.saveOrUpdate(user);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUserLoginDTO reg(MobileRegReqDTO reqDTO) {


        return this.saveAndLogin(reqDTO.getMobile(),
                reqDTO.getDeptCode(),
                reqDTO.getRealName(),
                null,
                reqDTO.getMobile(),
                "",
                reqDTO.getPassword());
    }


    /**
     * 保存用户并自动登录
     * @param userName
     * @param deptCode
     * @param realName
     * @param mobile
     * @param avatar
     * @param password
     * @return
     */
    private SysUserLoginDTO saveAndLogin(String userName, String deptCode, String realName, String role, String mobile, String avatar, String password){

        // 保存用户
        SysUser user = new SysUser();
        user.setId(IdWorker.getIdStr());
        user.setUserName(userName);
        user.setRealName(realName);
        user.setDeptCode(deptCode);
        user.setMobile(mobile);
        user.setAvatar(avatar);
        PassInfo passInfo = PassHandler.buildPassword(password);
        user.setPassword(passInfo.getPassword());
        user.setSalt(passInfo.getSalt());

        // 保存角色
        List<String> roleList = new ArrayList<>();
        if(!StringUtils.isBlank(role)){
            roleList.add(role);
        }else{
            // 默认学员
            roleList.add(ROLE_STUDENT);
        }


        // 保存角色
        sysUserRoleService.saveRoles(user.getId(), roleList);

        // 保存用户
        this.save(user);
        return this.setToken(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(List<UserExportDTO> reqDTO) {
        for(UserExportDTO item: reqDTO){
            this.importUser(item);
        }
    }

    @Override
    public List<UserExportDTO> listForExport(SysUserQueryReqDTO reqDTO) {
        return baseMapper.listForExport(reqDTO);
    }

    @Override
    public SysUserSaveReqDTO detail(String id) {

        // 基础信息复制
        SysUser user = this.getById(id);
        SysUserSaveReqDTO respDTO = new SysUserSaveReqDTO();
        BeanMapper.copy(user, respDTO);

        // 角色是要
        List<String> roles = sysUserRoleService.listRoleIds(user.getId());
        respDTO.setRoles(roles);
        return respDTO;
    }

    @Override
    public List<String> listByDept(List<String> codes) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(SysUser::getId)
                .in(SysUser::getDeptCode, codes);

        List<SysUser> list = this.list(wrapper);
        List<String> ids = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(SysUser user: list){
                ids.add(user.getId());
            }
        }
        return ids;
    }

    @Override
    public void batchDept(UserDeptReqDTO reqDTO) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SysUser::getId, reqDTO.getUserIds());

        SysUser user = new SysUser();
        user.setDeptCode(reqDTO.getDeptCode());
        this.update(user, wrapper);
    }

    @Override
    public void resetPass(ResetPassReqDTO reqDTO) {

        // 校验短信
        boolean check = false;

        if(!check){
            throw new ServiceException("短信验证码错误，请确认！");
        }

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getMobile, reqDTO.getMobile());
        SysUser user = this.getOne(wrapper, false);

        if(user == null){
            throw new ServiceException("无法找到对应手机号的用户！");
        }

        PassInfo passInfo = PassHandler.buildPassword(reqDTO.getNewPassword());
        user.setPassword(passInfo.getPassword());
        user.setSalt(passInfo.getSalt());
        this.updateById(user);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUserLoginDTO loginByThird(String loginType, String openId, String nickName, String avatar) {

        String userId = sysUserBindService.findBind(loginType, openId);

        // 不存在，创建新的用户
        if(StringUtils.isBlank(userId)){

            // 随机产生数据
            SysUserLoginDTO dto = this.saveAndLogin(RandomStringUtils.randomAlphabetic(16),
                    "A01",
                    nickName,
                    null,
                    RandomStringUtils.randomAlphabetic(16),
                    avatar,
                    RandomStringUtils.randomAlphanumeric(32));

            // 绑定
            SysUserBind bind = new SysUserBind();
            bind.setLoginType(loginType);
            bind.setUserId(dto.getId());
            bind.setOpenId(openId);
            sysUserBindService.save(bind);

            return dto;
        }


        // 校验用户状态&密码
        SysUser user = this.getById(userId);
        return this.checkAndLogin(user, null);
    }


    /**
     * 导入用户
     * @param reqDTO
     */
    private void importUser(UserExportDTO reqDTO){

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUserName, reqDTO.getUserName());

        int count = this.count(wrapper);

        if(count > 0){
            throw new ServiceException("用户名【"+reqDTO.getUserName()+"】已存在，无法导入！");
        }


        // 保存用户
        SysUser user = new SysUser();
        BeanMapper.copy(reqDTO, user);
        user.setId(IdWorker.getIdStr());

        String pass = reqDTO.getPassword();
        if(StringUtils.isBlank(pass)){
            pass = DEFAULT_PASS;
        }
        PassInfo passInfo = PassHandler.buildPassword(pass);
        user.setPassword(passInfo.getPassword());
        user.setSalt(passInfo.getSalt());

        // 保存角色
        List<String> roles = new ArrayList<>();
        if(StringUtils.isBlank(reqDTO.getRoleIds())){
            roles.add(ROLE_STUDENT);
        }else {
            // 角色列表
            String [] roleIds = reqDTO.getRoleIds().split(",");
            roles = Arrays.asList(roleIds);
        }

        sysUserRoleService.saveRoles(user.getId(), roles);
        this.save(user);
    }

    /**
     * 保存会话信息
     * @param user
     * @return
     */
    private SysUserLoginDTO setToken(SysUser user){

        // 获取一个用户登录的信息
        String key = Constant.USER_NAME_KEY+user.getUserName();
        String json = redisService.getString(key);
        if(!StringUtils.isBlank(json)){
            // 删除旧的会话
            redisService.del(key);
        }

        SysUserLoginDTO respDTO = new SysUserLoginDTO();
        BeanMapper.copy(user, respDTO);

        // 根据用户生成Token
        String token = JwtUtils.sign(user.getUserName());
        respDTO.setToken(token);

        // 添加角色信息
        this.fillRoleData(respDTO);

        // 权限表，用于前端控制按钮
        List<String> permissions = sysUserRoleService.findUserPermission(user.getId());
        respDTO.setPermissions(permissions);


        // 保存如Redis
        redisService.set(key, JSON.toJSONString(respDTO));

        return respDTO;

    }


    /**
     * 追加用户角色信息
     * @param respDTO
     */
    private void fillRoleData(SysUserLoginDTO respDTO){

        // 角色是要
        List<SysRole> roleList = sysUserRoleService.listRoles(respDTO.getId());

        // 数据权限1最小：查看自己的数据
        Integer dataScope = 1;
        // 角色类型1最小：学生角色不能进后台
        Integer roleType = 1;
        List<String> roleIds = new ArrayList<>();
        for(SysRole role: roleList){
            // 角色ID
            roleIds.add(role.getId());
            // 替换大的权限
            if(dataScope < role.getDataScope()){
                dataScope = role.getDataScope();
            }
            // 替换大的角色类型
            if(roleType < role.getRoleType()){
                roleType = role.getRoleType();
            }
        }

        respDTO.setRoleType(roleType);
        respDTO.setDataScope(dataScope);
        respDTO.setRoles(roleIds);
    }

    @Override
    public SysUserLoginDTO syncLogin(String userName, String realName, String role, Long timestamp, String departs, String token) {


//        // 30秒有效
//        Calendar cl = Calendar.getInstance();
//        cl.setTimeInMillis(timestamp * 1000L);
//        cl.add(Calendar.SECOND, 30);
//
//        if(cl.getTime().before(new Date())){
//            throw new ServiceException("token时效不正确！");
//        }

        // 签名校验
        boolean check = SignUtils.checkToken(userName, timestamp, token);
        if(!check){
            throw new ServiceException("签名验证失败！");
        }

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUserName, userName);
        SysUser user = this.getOne(wrapper, false);

        // 检查用户并登录
        if(user!=null){
            return this.checkAndLogin(user, null);
        }else{
            String deptCode = sysDepartService.syncDepart(departs);
            return this.saveAndLogin( userName,  deptCode,  realName, role, "", "", RandomStringUtils.randomAlphanumeric(16));
        }
    }
}
