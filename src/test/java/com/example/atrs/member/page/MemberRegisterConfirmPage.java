package com.example.atrs.member.page;

import com.codeborne.selenide.SelenideElement;
import com.example.atrs.ticket.page.ReservationCompetePage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MemberRegisterConfirmPage {
	public SelenideElement header() {
		return $(".panel-heading");
	}

	public MemberRegisterCompletePage register() {
		$("input[value=登録]").click();
		return page(MemberRegisterCompletePage.class);
	}
}
