package com.yf.document.modules.sys.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.document.modules.sys.log.dto.SysLogDTO;
import com.yf.document.modules.sys.log.entity.SysLog;
import com.yf.document.modules.sys.log.mapper.SysLogMapper;
import com.yf.document.modules.sys.log.service.SysLogService;
import com.yf.document.modules.sys.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* <p>
* 语言设置 服务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-04-28 10:23
*/
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {


    @Autowired
    private SysUserService sysUserService;

    @Override
    public IPage<SysLogDTO> paging(PagingReqDTO<SysLogDTO> reqDTO) {

        //创建分页对象
        IPage<SysLog> query = reqDTO.toPage();

        //查询条件
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();

        SysLogDTO params = reqDTO.getParams();

        if(params!=null){
            if(!StringUtils.isBlank(params.getTitle())){
                wrapper.lambda().eq(SysLog::getTitle, params.getTitle());
            }

            if(!StringUtils.isBlank(params.getUserName())){
                wrapper.lambda().eq(SysLog::getUserName, params.getUserName());
            }
        }

        wrapper.lambda().orderByDesc(SysLog::getCreateTime);

        //获得数据
        IPage<SysLog> page = this.page(query, wrapper);
        //转换结果
        IPage<SysLogDTO> pageData = JSON.parseObject(JSON.toJSONString(page), new TypeReference<Page<SysLogDTO>>(){});
        return pageData;
     }

//    @Override
//    public void saveLog(String title, String ip, String userId, String jsonData) {
//
//        new Thread(() -> {
//            SysLog log = new SysLog();
//            log.setData(jsonData);
//            log.setIp(ip);
//            log.setTitle(title);
//            log.setUserId(userId);
//            log.setCreateTime(new Date());
//            save(log);
//        }).run();
//    }
//
//    @Override
//    public String getUserId(String token) {
//        SysUserLoginDTO dto = sysUserService.token(token);
//
//        if(dto!=null){
//            return dto.getId();
//        }
//
//        return "";
//    }
}
