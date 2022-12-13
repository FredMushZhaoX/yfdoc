package com.yf.document.config;

import com.yf.document.aspect.mybatis.QueryInterceptor;
import com.yf.document.aspect.mybatis.UpdateInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis过滤器配置
 * 注意：必须按顺序进行配置，否则容易出现业务异常
 *
 * @author bool
 */
@Configuration
@MapperScan("com.yf.document.**.mapper")
public class MybatisConfig {

    /**
     * 数据查询过滤器
     */
    @Bean
    public QueryInterceptor queryInterceptor() {
        return new QueryInterceptor();
    }

    /**
     * 插入数据过滤器
     */
    @Bean
    public UpdateInterceptor updateInterceptor() {
        return new UpdateInterceptor();
    }


}