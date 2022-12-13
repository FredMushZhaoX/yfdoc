package com.yf.document;

import com.yf.boot.base.api.api.utils.JsonConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.management.MalformedObjectNameException;
import java.sql.SQLException;
import java.util.List;

/**
 * 云帆文档系统
 * @author bool
 * @date 2020-03-04 19:41
 */
@Log4j2
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.yf"})
public class DocumentApplication implements WebMvcConfigurer {


	public static void main(String[] args) throws MalformedObjectNameException, SQLException {
		SpringApplication.run(DocumentApplication.class, args);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		//保留原有converter,把新增fastConverter插入集合头,保证优先级
		converters.add(0, JsonConverter.fastConverter());
	}

}
