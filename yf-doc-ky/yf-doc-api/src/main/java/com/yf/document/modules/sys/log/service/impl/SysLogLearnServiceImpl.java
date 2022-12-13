package com.yf.document.modules.sys.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.utils.DateUtils;
import com.yf.document.modules.sys.log.entity.SysLogLearn;
import com.yf.document.modules.sys.log.mapper.SysLogLearnMapper;
import com.yf.document.modules.sys.log.service.SysLogLearnService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* <p>
* 学习记录业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2021-06-04 09:53
*/
@Service
public class SysLogLearnServiceImpl extends ServiceImpl<SysLogLearnMapper, SysLogLearn> implements SysLogLearnService {

    @Override
    public void merge(String userId, String courseId, String fileId, Integer learnMin) {

        QueryWrapper<SysLogLearn> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(SysLogLearn::getUserId, userId)
                .eq(SysLogLearn::getCourseId, courseId)
                .eq(SysLogLearn::getFileId, fileId)
                .like(SysLogLearn::getCreateTime, DateUtils.dateNow("yyyy-MM-dd"));

        SysLogLearn log = this.getOne(wrapper, false);

        if(log == null){
            log = new SysLogLearn();
            log.setCourseId(courseId);
            log.setFileId(fileId);
            log.setUserId(userId);
            log.setLearnMin(learnMin);
            log.setCreateTime(new Date());
            this.save(log);
        }else{
            log.setLearnMin(log.getLearnMin()+learnMin);
            this.updateById(log);
        }
    }
}
