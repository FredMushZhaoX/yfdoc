package com.yf.document.modules.sys.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.utils.DateUtils;
import com.yf.document.modules.sys.log.entity.SysLogActive;
import com.yf.document.modules.sys.log.mapper.SysLogActiveMapper;
import com.yf.document.modules.sys.log.service.SysLogActiveService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* <p>
* 用户活跃记录业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 08:10
*/
@Service
public class SysLogActiveServiceImpl extends ServiceImpl<SysLogActiveMapper, SysLogActive> implements SysLogActiveService {

    @Override
    public void merge(String userId) {

        QueryWrapper<SysLogActive> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(SysLogActive::getUserId, userId)
                .like(SysLogActive::getCreateTime, DateUtils.dateNow("yyyy-MM-dd"));

        int count = this.count(wrapper);

        if(count>0){
            return;
        }

        SysLogActive active = new SysLogActive();
        active.setUserId(userId);
        active.setCreateTime(new Date());
        this.save(active);
    }
}
