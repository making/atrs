package com.example.atrs.member;

import java.io.Serializable;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MemberSession implements Serializable {
	private final MemberMapper memberMapper;
	private Member member;

	public MemberSession(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@EventListener
	public void foo(AuthenticationSuccessEvent event) {
		String membershipNumber = event.getAuthentication().getName();
		this.member = this.memberMapper.findOne(membershipNumber);
	}

	public Member getMember() {
		return this.member;
	}

	public void updateMember(Member member) {
		this.member = member;
	}
}
