package com.yf.document;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yf.document.modules.sys.menu.dto.response.RouteRespDTO;
import com.yf.document.modules.sys.menu.service.SysMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuTest {

    @Autowired
    private SysMenuService sysMenuService;

    @Test
    public void testOnline(){

        List<RouteRespDTO> list = sysMenuService.listTree();

        int i=1;
        for(RouteRespDTO item: list){
            System.out.println("UPDATE `sys_menu` SET sort="+i+" WHERE id='"+item.getId()+"';");

            List<RouteRespDTO> children = item.getChildren();

            int j=1;
            if(!CollectionUtils.isEmpty(children)){
                for(RouteRespDTO c: children){
                    System.out.println("UPDATE `sys_menu` SET sort="+j+" WHERE id='"+c.getId()+"';");
                    j++;
                }
            }

            i++;
        }

    }
}
