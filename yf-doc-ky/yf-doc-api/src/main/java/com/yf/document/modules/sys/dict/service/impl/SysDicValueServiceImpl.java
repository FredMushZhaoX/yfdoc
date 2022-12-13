package com.yf.document.modules.sys.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.boot.base.api.exception.ServiceException;
import com.yf.boot.base.api.utils.BeanMapper;
import com.yf.boot.base.api.utils.StringUtils;
import com.yf.boot.base.aspect.dict.BaseDictService;
import com.yf.document.modules.sys.dict.dto.SysDicValueDTO;
import com.yf.document.modules.sys.dict.dto.ext.DicValueTreeDTO;
import com.yf.document.modules.sys.dict.entity.SysDicValue;
import com.yf.document.modules.sys.dict.mapper.SysDicValueMapper;
import com.yf.document.modules.sys.dict.service.SysDicValueService;
import com.yf.document.utils.CacheKey;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>
* 分类字典值业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-12-01 14:00
*/
@Service
public class SysDicValueServiceImpl extends ServiceImpl<SysDicValueMapper, SysDicValue> implements SysDicValueService
        , BaseDictService {

    /**
     * 0表示顶级分类
     */
    private static final String ROOT_TAG = "0";

    @Override
    public List<DicValueTreeDTO> findTree(String dicCode) {

        QueryWrapper<SysDicValue> wrapper = new QueryWrapper();
        wrapper.lambda().eq(SysDicValue::getDicCode, dicCode);

        //全部列表
        List<SysDicValue> list = this.list(wrapper);
        List<DicValueTreeDTO> dtoList = BeanMapper.mapList(list, DicValueTreeDTO.class);

        //子结构的列表
        Map<String,List<DicValueTreeDTO>> map = new HashMap<>(16);

        for(DicValueTreeDTO item: dtoList){

            //如果存在
            if(map.containsKey(item.getParentId())){
                map.get(item.getParentId()).add(item);
                continue;
            }

            //增加新的结构
            List<DicValueTreeDTO> a = new ArrayList<>();
            a.add(item);
            map.put(item.getParentId(), a);
        }

        //注意，第0级为顶级的
        List<DicValueTreeDTO> topList = map.get(ROOT_TAG);
        if(!CollectionUtils.isEmpty(topList)){
            for(DicValueTreeDTO item: topList){
                this.fillChildren(map, item);
            }
        }

        return topList;
    }


    @CacheEvict(value = CacheKey.DICT, key = "#reqDTO.dicCode + '-' + #reqDTO.value")
    @Override
    public void save(SysDicValueDTO reqDTO) {

        if(!StringUtils.isBlank(reqDTO.getValue())) {

            QueryWrapper<SysDicValue> wrapper = new QueryWrapper<>();
            wrapper.lambda()
                    .eq(SysDicValue::getDicCode, reqDTO.getDicCode())
                    .eq(SysDicValue::getValue, reqDTO.getValue());

            if (!StringUtils.isBlank(reqDTO.getId())) {
                wrapper.lambda().ne(SysDicValue::getId, reqDTO.getId());
            }
            int count = this.count(wrapper);

            if (count > 0) {
                throw new ServiceException("字典值不可以重复！");
            }
        }else{
            // 分类字典ID和值一样
            reqDTO.setId(IdWorker.getIdStr());
            reqDTO.setValue(reqDTO.getId());
        }

        //复制参数
        SysDicValue entity = new SysDicValue();
        BeanMapper.copy(reqDTO, entity);
        this.saveOrUpdate(entity);
    }

    @Override
    public Map<String, String> findDictMap(String dictCode) {
        QueryWrapper<SysDicValue> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysDicValue::getDicCode, dictCode);


        List<SysDicValue> list = this.list(wrapper);

        Map<String, String> map = new HashMap<>(16);

        if(!CollectionUtils.isEmpty(list)){
            for(SysDicValue item: list){
                map.put(item.getTitle(), item.getValue());
            }
        }
        return map;
    }


    /**
     * 递归去做填充数据
     * @param map
     * @param item
     */
    private void fillChildren(Map<String,List<DicValueTreeDTO>> map, DicValueTreeDTO item){

        //设置子类
        if(map.containsKey(item.getId())){

            List<DicValueTreeDTO> children = map.get(item.getId());
            if(!CollectionUtils.isEmpty(children)){
                for(DicValueTreeDTO sub: children){
                    this.fillChildren(map, sub);
                }
            }
            item.setChildren(children);
        }
    }



    @Override
    @Cacheable(value = CacheKey.DICT, key = "#dicCode + '-' + #value")
    public String findDictText(String dicCode, String value){
        String text = baseMapper.findDictText(dicCode, value);
        if(StringUtils.isBlank(text)){
            text = "";
        }
        return text;
    }


    @Override
    public String findTableText(String dicTable, String dicText, String dicCode, String value){
        String text = baseMapper.findTableText(dicTable, dicText, dicCode, value);
        if(StringUtils.isBlank(text)){
            text = "";
        }
        return text;
    }
}
