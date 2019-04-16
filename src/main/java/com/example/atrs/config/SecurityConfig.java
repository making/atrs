package com.example.atrs.config;

import javax.servlet.Filter;

import com.example.atrs.auth.security.AtrsLogoutSuccessHandler;
import com.example.atrs.auth.security.AtrsUsernamePasswordAuthenticationFilter;
import org.terasoluna.gfw.security.web.logging.UserIdMDCPutFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.CookieGenerator;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final AtrsLogoutSuccessHandler atrsLogoutSuccessHandler;
	private final AuthenticationFailureHandler authenticationFailureHandler;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final MessageSource messageSource;
	private final String authCookieName;

	public SecurityConfig(AtrsLogoutSuccessHandler atrsLogoutSuccessHandler,
			AuthenticationFailureHandler authenticationFailureHandler,
			AuthenticationSuccessHandler authenticationSuccessHandler,
			MessageSource messageSource,
			@Value("${cookie.cookie-name}") String authCookieName) {
		this.atrsLogoutSuccessHandler = atrsLogoutSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.messageSource = messageSource;
		this.authCookieName = authCookieName;
	}

	@Bean
	@ConfigurationProperties(prefix = "cookie")
	public CookieGenerator cookieGenerator() {
		CookieGenerator cookieGenerator = new CookieGenerator();
		return cookieGenerator;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(loggingFilter(), SessionManagementFilter.class) //
				.addFilterAfter(this.userIdMDCPutFilter(),
						AnonymousAuthenticationFilter.class) //
				.addFilterAt(this.usernamePasswordAuthenticationFilter(),
						DefaultLoginPageGeneratingFilter.class) //
				.formLogin() //
				.loginPage("/auth/login") //
				.and() //
				.logout() //
				.logoutUrl("/auth/dologout") //
				.logoutSuccessHandler(this.atrsLogoutSuccessHandler) //
				.deleteCookies(this.authCookieName, "JSESSIONID") //
				.and() //
				.authorizeRequests() //
				.mvcMatchers("/member/update").hasRole("MEMBER") //
				.and() //
				.csrf().ignoringAntMatchers("/auth", "/auth/password", "/oauth/token",
						"/auth/dologin");
	}

	private Filter loggingFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeClientInfo(true);
		filter.setIncludeQueryString(true);
		filter.setBeforeMessagePrefix("[ACCESS START    ] ");
		filter.setBeforeMessageSuffix("");
		filter.setAfterMessagePrefix("[ACCESS FINISH   ] ");
		filter.setAfterMessageSuffix("");
		return filter;
	}

	private UserIdMDCPutFilter userIdMDCPutFilter() {
		UserIdMDCPutFilter userIdMDCPutFilter = new UserIdMDCPutFilter();
		userIdMDCPutFilter.setRemoveValue(false);
		userIdMDCPutFilter.setAttributeName("user");
		return userIdMDCPutFilter;
	}

	private AtrsUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter()
			throws Exception {
		AtrsUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new AtrsUsernamePasswordAuthenticationFilter();
		usernamePasswordAuthenticationFilter
				.setAuthenticationManager(this.authenticationManager());
		usernamePasswordAuthenticationFilter
				.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
		usernamePasswordAuthenticationFilter
				.setAuthenticationFailureHandler(this.authenticationFailureHandler);
		usernamePasswordAuthenticationFilter.setMessageSource(this.messageSource);
		usernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
				new AntPathRequestMatcher("/auth/dologin", "POST"));
		usernamePasswordAuthenticationFilter.setUsernameParameter("membershipNumber");
		usernamePasswordAuthenticationFilter.setPasswordParameter("password");
		return usernamePasswordAuthenticationFilter;
	}
}
