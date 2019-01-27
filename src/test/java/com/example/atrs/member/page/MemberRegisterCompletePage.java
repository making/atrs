package com.example.atrs.member.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MemberRegisterCompletePage {
	public SelenideElement alertSuccess() {
		return $(".alert-success").$("h2");
	}

	public SelenideElement membershipNumber() {
		return $(".alert-success").$("strong");
	}

}
