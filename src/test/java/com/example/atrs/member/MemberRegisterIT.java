package com.example.atrs.member;

import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

import com.example.atrs.member.page.MemberRegisterCompletePage;
import com.example.atrs.member.page.MemberRegisterConfirmPage;
import com.example.atrs.member.page.MemberRegisterFormPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.example.atrs.Atrs.login;
import static com.example.atrs.Atrs.logout;
import static com.example.atrs.Atrs.useHeadlessChrome;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MemberRegisterIT {
	@LocalServerPort
	int port;

	@Before
	public void setup() {
		useHeadlessChrome();
		logout(this.port);
	}

	@Test
	public void register() throws Exception {
		String email = UUID.randomUUID().toString().replace("-", "") + "@example.com";
		MemberRegisterFormPage memberRegisterFormPage = open(
				new URL("http://localhost:" + this.port + "/member/register?form"),
				MemberRegisterFormPage.class);
		memberRegisterFormPage.header().shouldHave(text("新規会員登録"));
		MemberRegisterConfirmPage memberRegisterConfirmPage = memberRegisterFormPage
				.kanjiName("山田", "太郎") //
				.kanaName("ヤマダ", "タロウ") //
				.gender(Gender.M) //
				.dateOfBirth(LocalDate.of(2000, 1, 1)) //
				.tel("123", "456", "7890") //
				.zipCode("100", "8011") //
				.address("東京都千代田区千代田１−１") //
				.creditCard("VIS", "4012888888881881", 22, 12) //
				.email(email) //
				.password("password") //
				.confirm();
		memberRegisterConfirmPage.header().shouldHave(text("登録内容"));
		MemberRegisterCompletePage memberRegisterCompletePage = memberRegisterConfirmPage
				.register();
		memberRegisterCompletePage.alertSuccess().shouldHave(text("会員登録しました。"));

		String membershipNumber = memberRegisterCompletePage.membershipNumber().text();
		login(membershipNumber, "password");
		$(byId("header")).$(".dropdown-toggle").$("span")
				.shouldHave(text("ようこそ 山田 太郎 さま"));
	}
}
