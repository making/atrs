package com.example.atrs.ticket.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FlightConfirmationPage {
	public SelenideElement header() {
		return $("h2");
	}

	public ReservationCompetePage reserve() {
		$("input[value=予約確定]").click();
		return page(ReservationCompetePage.class);
	}
}
