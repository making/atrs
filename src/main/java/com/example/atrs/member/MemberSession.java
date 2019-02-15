package com.example.atrs.member;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.example.atrs.auth.jwt.JwtCreatedEvent;
import com.example.atrs.auth.jwt.OidcProps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MemberSession implements Serializable {
	private final MembershipSharedService membershipSharedService;
	private final HttpServletRequest request;
	private final String cookieName;
	private Member member;

	public MemberSession(MembershipSharedService membershipSharedService,
			HttpServletRequest request, OidcProps props,
			@Value("${cookie.cookie-name}") String cookieName) {
		this.membershipSharedService = membershipSharedService;
		this.request = request;
		this.cookieName = cookieName;
	}

	@EventListener
	public void foo(JwtCreatedEvent event) {
		this.member = this.membershipSharedService.findMe((String) event.getSource());
	}

	public Member getMember() {
		return this.member;
	}

	public void updateMember(Member member) {
		this.member = member;
	}
}
