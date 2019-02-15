package com.example.atrs.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.terasoluna.gfw.security.web.logging.UserIdMDCPutFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(loggingFilter(), SessionManagementFilter.class) //
				.addFilterAfter(this.userIdMDCPutFilter(),
						AnonymousAuthenticationFilter.class) //
				.authorizeRequests() //
				.mvcMatchers(POST, "/members/me").permitAll() //
				.mvcMatchers("/members/me").hasAuthority("SCOPE_member.me") //
				.mvcMatchers(GET, "/members/{id}").hasAuthority("SCOPE_member.read") //
				.anyRequest().denyAll() //
				.and() //
				.csrf().disable() //
				.oauth2ResourceServer().jwt() //
		;
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
		UserIdMDCPutFilter userIdMDCPutFilter = new UserIdMDCPutFilter() {
			@Override
			protected String getMDCValue(HttpServletRequest request,
					HttpServletResponse response) {
				Authentication authentication = SecurityContextHolder.getContext()
						.getAuthentication();
				if (authentication != null) {
					Object principal = authentication.getPrincipal();
					if (principal instanceof UserDetails) {
						return ((UserDetails) principal).getUsername();
					}
					return authentication.getName();
				}
				return null;
			}
		};
		userIdMDCPutFilter.setRemoveValue(true);
		userIdMDCPutFilter.setAttributeName("user");
		return userIdMDCPutFilter;
	}
}
