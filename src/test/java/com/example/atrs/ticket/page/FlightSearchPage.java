package com.example.atrs.ticket.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FlightSearchPage {

	public SelenideElement header() {
		return $("h1");
	}

	public FlightSearchPage setDepAirportCd(String depAirportCd) {
		$(byId("depAirportCd")).selectOptionByValue(depAirportCd);
		return this;
	}

	public FlightSearchPage setArrAirportCd(String arrAirportCd) {
		$(byId("arrAirportCd")).selectOptionByValue(arrAirportCd);
		return this;
	}

	public SelenideElement searchButton() {
		return $(byId("flights-search-button"));
	}

	public ElementsCollection outwardFlights() {
		return $(byId("outward-flights-table")).$("tbody").findAll("tr");
	}

	public ElementsCollection homewardFlights() {
		return $(byId("homeward-flights-table")).$("tbody").findAll("tr");
	}

	public SelenideElement reserveButton() {
		return $(byId("reserve-flights-button"));
	}

	public SelenideElement loginForm() {
		return $(byId("login-form"));
	}

	public FlightReservationPage login(String membershipNumber, String password) {
		SelenideElement loginForm = this.loginForm();
		loginForm.$("input[name=membershipNumber]").setValue(membershipNumber);
		loginForm.$("input[name=password]").setValue(password);
		loginForm.submit();
		return page(FlightReservationPage.class);
	}

}
