package com.example.atrs.ticket.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FlightReservationPage {
	public SelenideElement header() {
		return $("h2");
	}

	public SelenideElement customerForm() {
		return $(byId("customerinfo-form"));
	}

	public FlightConfirmationPage confirm() {
		customerForm().$("input[name=confirm]").click();
		return page(FlightConfirmationPage.class);
	}
}
