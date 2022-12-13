package com.yf.document.modules.sys.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.modules.sys.config.dto.CfgUploadLocalDTO;
import com.yf.document.modules.sys.config.entity.CfgUploadLocal;
import com.yf.document.modules.sys.config.mapper.CfgUploadLocalMapper;
import com.yf.document.modules.sys.config.service.CfgUploadLocalService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* <p>
* 本地文件上传配置业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2021-02-05 11:16
*/
@Service
public class CfgUploadLocalServiceImpl extends ServiceImpl<CfgUploadLocalMapper, CfgUploadLocal> implements CfgUploadLocalService {


    @Override
    public void save(CfgUploadLocalDTO reqDTO) {
        CfgUploadLocal entity = new CfgUploadLocal();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public CfgUploadLocalDTO find() {
        List<CfgUploadLocal> list =  this.list();
        CfgUploadLocalDTO respDTO = new CfgUploadLocalDTO();
        if(!CollectionUtils.isEmpty(list)){
            BeanMapper.copy(list.get(0), respDTO);
        }
        return respDTO;
    }
}
