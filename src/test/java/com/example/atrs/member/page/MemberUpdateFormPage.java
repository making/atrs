package com.example.atrs.member.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MemberUpdateFormPage {
	public SelenideElement header() {
		return $(".panel-heading");
	}

	public SelenideElement updateForm() {
		return $(byId("membership-form"));
	}

	public MemberUpdateFormPage copyEmail() {
		SelenideElement form = updateForm();
		String mail = form.$(byId("mail")).val();
		form.$(byId("reEnterMail")).setValue(mail);
		return this;
	}

	public MemberUpdateCompletePage update() {
		updateForm().submit();
		return page(MemberUpdateCompletePage.class);
	}
}
