package com.yf.document.modules.sys.menu.dto.response;

import com.yf.boot.base.api.utils.StringUtils;
import com.yf.document.modules.sys.menu.dto.SysMenuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bool
 */
@Data
@ApiModel(value="路由响应类", description="路由响应类")
public class RouteRespDTO extends SysMenuDTO {

    @ApiModelProperty(value = "子路由表")
    private List<RouteRespDTO> children;

    @ApiModelProperty(value = "路由属性")
    private Map<String,Object> meta;

    /**
     * 获取属性
     * @return
     */
    public Map<String,Object> getMeta(){
        Map<String,Object> meta = new HashMap<>(16);
        if(!StringUtils.isBlank(this.getMetaTitle())){
            meta.put("title", this.getMetaTitle());
        }

        if(!StringUtils.isBlank(this.getMetaIcon())){
            meta.put("icon", this.getMetaIcon());
        }

        if(this.getMetaNoCache()!=null){
            meta.put("noCache", this.getMetaNoCache());
        }

        if(!StringUtils.isBlank(this.getMetaActiveMenu())){
            meta.put("activeMenu", this.getMetaActiveMenu());
        }
        return meta;
    }



}
