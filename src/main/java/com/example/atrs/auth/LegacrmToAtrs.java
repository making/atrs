package com.example.atrs.auth;

import java.util.Date;

import com.example.atrs.member.CreditType;
import com.example.atrs.member.Gender;
import com.example.atrs.member.Member;
import com.example.poc.legacrm.customer.GetMemberResponse;

import org.springframework.beans.BeanUtils;

public class LegacrmToAtrs {
	public static Member convert(GetMemberResponse response) {
		com.example.poc.legacrm.customer.Member legacrmMember = response.getMember();
		com.example.poc.legacrm.customer.AuthLogin legacrmAuthLogin = legacrmMember
				.getAuthLogin();
		AuthLogin authLogin = new AuthLogin();
		BeanUtils.copyProperties(legacrmAuthLogin, authLogin);
		authLogin.setLoginDateTime(Date.from(legacrmAuthLogin.getLoginDateTime()));
		Member member = new Member();
		BeanUtils.copyProperties(legacrmMember, member);
		member.setAuthLogin(authLogin);
		member.setGender(Gender.valueOf(legacrmMember.getGender().name()));
		member.setCreditType(new CreditType() {
			{
				com.example.poc.legacrm.customer.CreditType legacrmCreditType = legacrmMember
						.getCreditType();
				setCreditFirm(legacrmCreditType.getCreditFirm());
				setCreditTypeCd(legacrmCreditType.getCreditTypeCd());
			}
		});
		return member;
	}
}
