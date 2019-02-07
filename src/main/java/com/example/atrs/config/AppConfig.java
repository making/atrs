package com.example.atrs.config;

import java.time.Clock;
import java.util.LinkedHashMap;

import com.example.atrs.common.codelist.AirportCodeList;
import org.terasoluna.gfw.common.codelist.JdbcCodeList;
import org.terasoluna.gfw.common.codelist.NumberRangeCodeList;
import org.terasoluna.gfw.common.codelist.SimpleMapCodeList;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
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

	@Bean(name = "CL_AIRPORT")
	public AirportCodeList airportCodeList(
			FlywayMigrationInitializer flywayMigrationInitializer,
			JdbcTemplate jdbcTemplate) {
		AirportCodeList codeList = new AirportCodeList();
		codeList.setJdbcTemplate(jdbcTemplate);
		codeList.setQuerySql(
				"SELECT airport_cd,airport_name,display_order FROM airport order by display_order ASC");
		codeList.setValueColumn("airport_cd");
		codeList.setLabelColumn("airport_name");
		codeList.setOrderColumn("display_order");
		// 区切り行の表示順
		codeList.setAirportNopInsertOrder(100);
		return codeList;
	}

	@Bean(name = "CL_BOARDINGCLASS")
	public JdbcCodeList boardingClassCodeList(
			FlywayMigrationInitializer flywayMigrationInitializer,
			JdbcTemplate jdbcTemplate) {
		JdbcCodeList codeList = new JdbcCodeList();
		codeList.setJdbcTemplate(jdbcTemplate);
		codeList.setQuerySql(
				"SELECT boarding_class_cd,boarding_class_name FROM boarding_class ORDER BY display_order ASC");
		codeList.setValueColumn("boarding_class_cd");
		codeList.setLabelColumn("boarding_class_name");
		return codeList;
	}

	@Bean(name = "CL_FARETYPE")
	public JdbcCodeList fareTypeCodeList(
			FlywayMigrationInitializer flywayMigrationInitializer,
			JdbcTemplate jdbcTemplate) {
		JdbcCodeList codeList = new JdbcCodeList();
		codeList.setJdbcTemplate(jdbcTemplate);
		codeList.setQuerySql(
				"SELECT fare_type_cd, fare_type_name FROM fare_type ORDER BY display_order ASC");
		codeList.setValueColumn("fare_type_cd");
		codeList.setLabelColumn("fare_type_name");
		return codeList;
	}

	@Bean(name = "CL_CREDITYEAR")
	public NumberRangeCodeList creditYearCodeList() {
		NumberRangeCodeList codeList = new NumberRangeCodeList();
		codeList.setFrom(0);
		codeList.setTo(99);
		codeList.setValueFormat("%02d");
		codeList.setLabelFormat("%02d");
		return codeList;
	}

	@Bean(name = "CL_CREDITMONTH")
	public NumberRangeCodeList creditMonthCodeList() {
		NumberRangeCodeList codeList = new NumberRangeCodeList();
		codeList.setFrom(1);
		codeList.setTo(12);
		codeList.setValueFormat("%02d");
		codeList.setLabelFormat("%02d");
		return codeList;
	}

	@Bean(name = "CL_GENDER")
	public SimpleMapCodeList genderCodeList() {
		SimpleMapCodeList codeList = new SimpleMapCodeList();
		codeList.setMap(new LinkedHashMap<String, String>() {
			{
				put("M", "男性");
				put("F", "女性");
			}
		});
		return codeList;
	}

	@Bean(name = "CL_CREDITTYPE")
	public JdbcCodeList creditTypeCodeList(
			FlywayMigrationInitializer flywayMigrationInitializer,
			JdbcTemplate jdbcTemplate) {
		JdbcCodeList codeList = new JdbcCodeList();
		codeList.setJdbcTemplate(jdbcTemplate);
		codeList.setQuerySql(
				"SELECT credit_type_cd, credit_firm FROM credit_type ORDER BY display_order ASC");
		codeList.setValueColumn("credit_type_cd");
		codeList.setLabelColumn("credit_firm");
		return codeList;
	}

	@Bean(name = "CL_FLIGHTTYPE")
	public SimpleMapCodeList flightTypeCodeList() {
		SimpleMapCodeList codeList = new SimpleMapCodeList();
		codeList.setMap(new LinkedHashMap<String, String>() {
			{
				put("RT", "往復");
				put("OW", "片道");
			}
		});
		return codeList;
	}
}
