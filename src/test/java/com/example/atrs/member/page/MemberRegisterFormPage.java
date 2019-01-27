package com.example.atrs.member.page;

import java.time.LocalDate;

import com.codeborne.selenide.SelenideElement;
import com.example.atrs.common.util.DateTimeUtil;
import com.example.atrs.member.Gender;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MemberRegisterFormPage {
	public SelenideElement header() {
		return $(".panel-heading");
	}

	public SelenideElement registerForm() {
		return $(byId("membership-form"));
	}

	public MemberRegisterFormPage kanjiName(String familyName, String givenName) {
		SelenideElement form = registerForm();
		form.$(byId("kanjiFamilyName")).setValue(familyName);
		form.$(byId("kanjiGivenName")).setValue(givenName);
		return this;
	}

	public MemberRegisterFormPage kanaName(String familyName, String givenName) {
		SelenideElement form = registerForm();
		form.$(byId("kanaFamilyName")).setValue(familyName);
		form.$(byId("kanaGivenName")).setValue(givenName);
		return this;
	}

	public MemberRegisterFormPage gender(Gender gender) {
		SelenideElement form = registerForm();
		form.$("input[name=gender][value=" + gender.name() + "]").click();
		return this;
	}

	public MemberRegisterFormPage dateOfBirth(LocalDate dateOfBirth) {
		SelenideElement form = registerForm();
		form.$(byId("dateOfBirth"))
				.setValue(dateOfBirth.format(DateTimeUtil.DATE_FORMATTER));
		return this;
	}

	public MemberRegisterFormPage tel(String tel1, String tel2, String tel3) {
		SelenideElement form = registerForm();
		form.$(byId("tel1")).setValue(tel1);
		form.$(byId("tel2")).setValue(tel2);
		form.$(byId("tel3")).setValue(tel3);
		return this;
	}

	public MemberRegisterFormPage zipCode(String zipCode1, String zipCode2) {
		SelenideElement form = registerForm();
		form.$(byId("zipCode1")).setValue(zipCode1);
		form.$(byId("zipCode2")).setValue(zipCode2);
		return this;
	}

	public MemberRegisterFormPage address(String address) {
		SelenideElement form = registerForm();
		form.$(byId("address")).setValue(address);
		return this;
	}

	public MemberRegisterFormPage creditCard(String creditCardCode,
			String creditCardNumber, int year, int month) {
		SelenideElement form = registerForm();
		form.$("input[name=creditTypeCd][value=" + creditCardCode + "]").click();
		form.$(byId("creditNo")).setValue(creditCardNumber);
		form.$(byId("creditYear")).selectOptionByValue(String.format("%02d", year));
		form.$(byId("creditMonth")).selectOptionByValue(String.format("%02d", month));
		return this;
	}

	public MemberRegisterFormPage email(String email) {
		SelenideElement form = registerForm();
		form.$(byId("mail")).setValue(email);
		form.$(byId("reEnterMail")).setValue(email);
		return this;
	}

	public MemberRegisterFormPage password(String password) {
		SelenideElement form = registerForm();
		form.$(byId("password")).setValue(password);
		form.$(byId("reEnterPassword")).setValue(password);
		return this;
	}

	public MemberRegisterConfirmPage confirm() {
		registerForm().submit();
		return page(MemberRegisterConfirmPage.class);
	}
}
