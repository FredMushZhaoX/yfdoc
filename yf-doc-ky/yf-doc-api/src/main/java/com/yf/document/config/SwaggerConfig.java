package com.yf.document.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Swagger配置
 * @author bool
 * @date 2020/8/19 20:53
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {


    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("公共模块接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/common/**"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    @Bean
    public Docket certApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("新闻模块接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/news/**"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    @Bean
    public Docket courseApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("文档模块接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/doc/**"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }




    @Bean
    public Docket sysApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("系统模块接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/sys/**"))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("云帆文档系统接口")
                .description("云帆文档系统接口")
                .contact(new Contact("Pan", "https://doc.yfhl.net", "329930563@qq.com"))
                .version("3.0.0")
                .build();
    }


    /**
     * 授权头部
     * @return
     */
    @Bean
    SecurityScheme securityScheme() {
        return new ApiKey("token", "token", "header");
    }

}
