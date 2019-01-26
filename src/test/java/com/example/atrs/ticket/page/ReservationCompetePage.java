package com.example.atrs.ticket.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ReservationCompetePage {
	public SelenideElement alertSuccess() {
		return $(".alert-success").$("h2");
	}
}
