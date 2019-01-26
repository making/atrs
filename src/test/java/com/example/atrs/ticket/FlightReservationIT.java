package com.example.atrs.ticket;

import java.net.URL;

import com.example.atrs.ticket.page.FlightConfirmationPage;
import com.example.atrs.ticket.page.FlightReservationPage;
import com.example.atrs.ticket.page.FlightSearchPage;
import com.example.atrs.ticket.page.ReservationCompetePage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static com.example.atrs.Atrs.logout;
import static com.example.atrs.Atrs.useHeadlessChrome;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FlightReservationIT {
	@LocalServerPort
	int port;

	@Before
	public void setup() {
		useHeadlessChrome();
		logout(this.port);
	}

	@Test
	public void searchAndReserve() throws Exception {
		FlightSearchPage flightSearchPage = open(new URL("http://localhost:" + this.port),
				FlightSearchPage.class);
		flightSearchPage.header().shouldHave(text("Airline Ticket Reservation System"));
		flightSearchPage.setDepAirportCd("HND").setArrAirportCd("ITM").searchButton()
				.click();
		flightSearchPage.outwardFlights().shouldHave(sizeGreaterThanOrEqual(1)).get(0)
				.$("input").click();
		flightSearchPage.homewardFlights().shouldHave(sizeGreaterThanOrEqual(4)).get(4)
				.$("input").click();
		flightSearchPage.reserveButton().should(exist).click();
		flightSearchPage.loginForm().should(exist);
		FlightReservationPage flightReservationPage = flightSearchPage.login("0000000002",
				"aaaaa11111");
		flightReservationPage.header().shouldHave(text("予約"));
		FlightConfirmationPage flightConfirmationPage = flightReservationPage.confirm();
		flightConfirmationPage.header().shouldHave(text("予約内容確認"));
		ReservationCompetePage reservationCompetePage = flightConfirmationPage.reserve();
		reservationCompetePage.alertSuccess().shouldHave(text("ご予約を受け付けました。"));
	}
}
