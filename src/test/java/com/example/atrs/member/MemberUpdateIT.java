package com.example.atrs.member;

import java.net.URL;

import com.example.atrs.member.page.MemberUpdateCompletePage;
import com.example.atrs.member.page.MemberUpdateFormPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static com.example.atrs.Atrs.login;
import static com.example.atrs.Atrs.logout;
import static com.example.atrs.Atrs.useHeadlessChrome;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MemberUpdateIT {
	@LocalServerPort
	int port;

	@Before
	public void setup() {
		useHeadlessChrome();
		logout(this.port);
	}

	@Test
	public void updateMember() throws Exception {
		login("0000000001", "aaaaa11111");
		MemberUpdateFormPage memberUpdateFormPage = open(
				new URL("http://localhost:" + this.port + "/member/update?form"),
				MemberUpdateFormPage.class);
		memberUpdateFormPage.header().shouldHave(text("会員情報変更"));
		MemberUpdateCompletePage updateCompletePage = memberUpdateFormPage.copyEmail()
				.update();
		updateCompletePage.alertSuccess().shouldHave(text("会員情報を更新しました。"));
	}
}
