package com.example.atrs.member.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MemberUpdateCompletePage {
	public SelenideElement alertSuccess() {
		return $(".alert-success").$("li");
	}

}
