package com.example.atrs.config;

import java.util.regex.Pattern;

import org.terasoluna.gfw.web.codelist.CodeListInterceptor;
import org.terasoluna.gfw.web.logging.TraceLoggingInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(codeListInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/resources/**");
		registry.addInterceptor(new TraceLoggingInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/resources/**");
	}

	@Bean
	public CodeListInterceptor codeListInterceptor() {
		CodeListInterceptor codeListInterceptor = new CodeListInterceptor();
		codeListInterceptor.setCodeListIdPattern(Pattern.compile("CL_.+"));
		return codeListInterceptor;
	}
}
