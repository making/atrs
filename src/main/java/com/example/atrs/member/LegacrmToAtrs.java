package com.example.atrs.member;

import java.util.Date;

import com.example.poc.legacrm.customer.CreateMemberRequest;
import com.example.poc.legacrm.customer.GetMemberResponse;
import com.example.poc.legacrm.customer.UpdateMemberRequest;

import org.springframework.beans.BeanUtils;

public class LegacrmToAtrs {

	public static Member convert(com.example.poc.legacrm.customer.Member legacrmMember) {
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

	public static Member convert(GetMemberResponse response) {
		com.example.poc.legacrm.customer.Member legacrmMember = response.getMember();
		return convert(legacrmMember);
	}

	public static com.example.poc.legacrm.customer.Member reverse(Member member) {
		com.example.poc.legacrm.customer.Member legacrmMember = new com.example.poc.legacrm.customer.Member();
		com.example.poc.legacrm.customer.AuthLogin legacrmAuthLogin = new com.example.poc.legacrm.customer.AuthLogin();
		AuthLogin authLogin = member.getAuthLogin();
		if (authLogin != null) {
			BeanUtils.copyProperties(authLogin, legacrmAuthLogin);
			legacrmAuthLogin.setLoginDateTime(authLogin.getLoginDateTime().toInstant());
		}
		BeanUtils.copyProperties(member, legacrmMember);
		legacrmMember.setAuthLogin(legacrmAuthLogin);
		legacrmMember.setGender(com.example.poc.legacrm.customer.Gender
				.fromValue(member.getGender().name()));
		CreditType creditType = member.getCreditType();
		legacrmMember.setCreditType(new com.example.poc.legacrm.customer.CreditType(
				creditType.getCreditFirm(), creditType.getCreditTypeCd()));
		return legacrmMember;
	}

	public static CreateMemberRequest reverseForCreate(Member member) {
		CreateMemberRequest request = new CreateMemberRequest();
		request.setMember(reverse(member));
		request.setRawPassword(member.getPassword());
		return request;
	}

	public static UpdateMemberRequest reverseForUpdate(Member member,
			String currentPassword) {
		UpdateMemberRequest request = new UpdateMemberRequest();
		request.setMember(reverse(member));
		request.setRawPassword(member.getPassword());
		request.setCurrentPassword(currentPassword);
		return request;
	}
}
