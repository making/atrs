package com.example.atrs;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Atrs {

	public static void useHeadlessChrome() {
		Configuration.browser = Browsers.CHROME;
		Configuration.headless = true;
	}

	public static SelenideElement logoutForm() {
		return $("form[name=logoutForm]");
	}

	public static boolean isLoggedIn() {
		return logoutForm().is(exist);
	}

	public static void logout(int port) {
		try {
			open(new URL("http://localhost:" + port));
			if (isLoggedIn()) {
				logoutForm().submit();
			}
		}
		catch (MalformedURLException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void login(String membershipNumber, String password) {
		if (!isLoggedIn()) {
			$($(byId("header")), byText("ログイン")).click();
			SelenideElement loginForm = $(byId("login-form"));
			loginForm.should(exist);
			loginForm.$(byId("membershipNumber")).setValue(membershipNumber);
			loginForm.$(byId("password")).setValue(password);
			loginForm.submit();
		}
	}
}
