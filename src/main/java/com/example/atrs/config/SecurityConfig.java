package com.example.atrs.config;

import org.terasoluna.gfw.security.web.logging.UserIdMDCPutFilter;

import com.example.atrs.app.common.security.AtrsLogoutSuccessHandler;
import com.example.atrs.app.common.security.AtrsUsernamePasswordAuthenticationFilter;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final AtrsLogoutSuccessHandler atrsLogoutSuccessHandler;
	private final AuthenticationFailureHandler authenticationFailureHandler;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final MessageSource messageSource;

	public SecurityConfig(AtrsLogoutSuccessHandler atrsLogoutSuccessHandler,
			AuthenticationFailureHandler authenticationFailureHandler,
			AuthenticationSuccessHandler authenticationSuccessHandler,
			MessageSource messageSource) {
		this.atrsLogoutSuccessHandler = atrsLogoutSuccessHandler;
		this.authenticationFailureHandler = authenticationFailureHandler;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.messageSource = messageSource;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(this.userIdMDCPutFilter(),
				AnonymousAuthenticationFilter.class) //
				.addFilterAt(this.usernamePasswordAuthenticationFilter(),
						DefaultLoginPageGeneratingFilter.class) //
				.formLogin() //
				.loginPage("/auth/login") //
				.and() //
				.logout() //
				.logoutUrl("/auth/dologout") //
				.logoutSuccessHandler(this.atrsLogoutSuccessHandler) //
				.and() //
				.authorizeRequests() //
				.mvcMatchers("/member/update").hasRole("MEMBER");
	}

	private UserIdMDCPutFilter userIdMDCPutFilter() {
		UserIdMDCPutFilter userIdMDCPutFilter = new UserIdMDCPutFilter();
		userIdMDCPutFilter.setRemoveValue(true);
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
