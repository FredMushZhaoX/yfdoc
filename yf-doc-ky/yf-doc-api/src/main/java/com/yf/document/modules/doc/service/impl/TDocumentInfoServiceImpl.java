package com.yf.document.modules.doc.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.api.dto.PagingReqDTO;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.document.ability.doc.service.OfficeService;
import com.yf.document.modules.doc.dto.TDocumentInfoDTO;
import com.yf.document.modules.doc.entity.TDocumentInfo;
import com.yf.document.modules.doc.mapper.TDocumentInfoMapper;
import com.yf.document.modules.doc.service.TDocumentInfoService;
import com.yf.document.modules.sys.config.dto.CfgBaseDTO;
import com.yf.document.modules.sys.config.dto.CfgUploadLocalDTO;
import com.yf.document.modules.sys.config.enums.UploadType;
import com.yf.document.modules.sys.config.service.CfgBaseService;
import com.yf.document.modules.sys.config.service.CfgUploadLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

/**
* <p>
* 文档信息表业务实现类
* </p>
*
* @author Panjp
* @since 2021-08-23 20:30
*/
@Service
public class TDocumentInfoServiceImpl extends ServiceImpl<TDocumentInfoMapper, TDocumentInfo> implements TDocumentInfoService {
    @Autowired
    private OfficeService officeService;
    @Autowired
    private CfgBaseService cfgBaseService;
    @Autowired
    private CfgUploadLocalService cfgUploadLocalService;


    @Override
    public IPage<TDocumentInfoDTO> paging(PagingReqDTO<TDocumentInfoDTO> reqDTO) {
        IPage<TDocumentInfoDTO> pageData = baseMapper.paging(reqDTO.toPage(), reqDTO.getParams());

        return pageData;
    }


    @Override
    public void save(TDocumentInfoDTO reqDTO){
        //复制参数
        TDocumentInfo entity = new TDocumentInfo();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);

        // 转换文档
        try {
            this.convert(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("在转码时出现问题："+e.getMessage());
        }

    }

    @Override
    public void updateStatus(TDocumentInfoDTO reqDTO){
        TDocumentInfo entity = new TDocumentInfo();
        BeanMapper.copy(reqDTO, entity);
        baseMapper.updateStatus(entity);
    }

    @Override
    public void delete(List<String> ids){
        //批量删除
        this.removeByIds(ids);
    }

    @Override
    public TDocumentInfoDTO detail(String id){
        TDocumentInfo entity = this.getById(id);
        TDocumentInfoDTO dto = new TDocumentInfoDTO();
        BeanMapper.copy(entity, dto);
        return dto;
    }

    @Override
    public List<TDocumentInfoDTO> list(TDocumentInfoDTO reqDTO){

        //分页查询并转换
        QueryWrapper<TDocumentInfo> wrapper = new QueryWrapper<>();

        //转换并返回
        List<TDocumentInfo> list = this.list(wrapper);

        //转换数据
        List<TDocumentInfoDTO> dtoList = BeanMapper.mapList(list, TDocumentInfoDTO.class);

        return dtoList;
    }
    /**
     * 转换Office文档为pdf格式
     * @param entity
     * @throws Exception
     */
    private void convert(TDocumentInfo entity) throws Exception{

        // 配置信息
        CfgBaseDTO cfg = cfgBaseService.findSimple();

        // 只转换Office类型
        String fileType = entity.getFileType();

        // Office类型文档转换
        this.convertDoc(cfg.getUploadType(), entity);
        return;

    }

    /**
     * 转换文档类型数据
     * @param uploadType
     * @param entity
     * @throws ClientException
     */
    private void convertDoc(Integer uploadType, TDocumentInfo entity) throws ClientException, IOException {

        // 本地上传
        if(UploadType.LOCAL.equals(uploadType)){
            CfgUploadLocalDTO conf = cfgUploadLocalService.find();
            // 相对文件路径
            String url = entity.getFileUrl();
            // 变成物理路径
            String path = url.replace(conf.getUrl(), conf.getLocalDir());
            // 目标地址
            String dist = path+".pdf";
            // 返回物理路径
            String rest = officeService.convert(path, dist);
            // 再变回访问路径
            String result = rest.replace(conf.getLocalDir(), conf.getUrl());
            entity.setViewUrl(result);
            this.updateById(entity);
            return;
        }
    }

}
