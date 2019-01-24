package com.example.atrs.config;

import java.io.IOException;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.terasoluna.gfw.common.date.jodatime.DefaultJodaTimeDateFactory;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ImportResource(locations = "classpath:spring/codelist.xml")
public class AppConfig {

	@Bean
	public JodaTimeDateFactory dateFactory() {
		return new DefaultJodaTimeDateFactory();
	}

	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(
			ResourcePatternResolver resolver) throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		factoryBean.setMappingFiles(
				resolver.getResources("classpath:/dozer/**/*-mapping.xml"));
		return factoryBean;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
