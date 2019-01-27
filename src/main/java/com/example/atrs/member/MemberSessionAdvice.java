package com.example.atrs.member;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MemberSessionAdvice {
	private final MemberSession memberSession;

	public MemberSessionAdvice(MemberSession memberSession) {
		this.memberSession = memberSession;
	}

	@ModelAttribute
	public MemberSession memberSession() {
		return this.memberSession;
	}
}
