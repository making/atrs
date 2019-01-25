package com.example.atrs.config;

import java.time.Clock;
import java.util.LinkedHashMap;

import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ImportResource(locations = "classpath:spring/codelist.xml")
public class AppConfig {

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	public ExceptionLogger exceptionLogger() {
		SimpleMappingExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
		exceptionCodeResolver.setExceptionMappings(new LinkedHashMap<String, String>() {
			{
				put("BusinessException", "e.ar.fw.8001");
			}
		});
		exceptionCodeResolver.setDefaultExceptionCode("e.ar.fw.9999");
		ExceptionLogger exceptionLogger = new ExceptionLogger();
		exceptionLogger.setExceptionCodeResolver(exceptionCodeResolver);
		return exceptionLogger;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
