package com.yf.document.modules.sys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.document.modules.sys.dict.dto.SysDicValueDTO;
import com.yf.document.modules.sys.dict.dto.ext.DicValueTreeDTO;
import com.yf.document.modules.sys.dict.entity.SysDicValue;

import java.util.List;
import java.util.Map;

/**
* <p>
* 分类字典值业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-12-01 14:00
*/
public interface SysDicValueService extends IService<SysDicValue> {

    /**
     * 查找分类树
     * @param dicCode
     * @return
     */
    List<DicValueTreeDTO> findTree(String dicCode);

    /**
     * 保存字典项
     * @param reqDTO
     */
    void save(SysDicValueDTO reqDTO);

    /**
     * 查找一个Map列表，显示--值
     * @param dictCode
     * @return
     */
    Map<String,String> findDictMap(String dictCode);
}
