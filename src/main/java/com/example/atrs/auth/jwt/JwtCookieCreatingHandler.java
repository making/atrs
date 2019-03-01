/*
 * Copyright 2014-2018 NTT Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.atrs.auth.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.example.atrs.auth.AuthLoginUserDetails;
import com.example.atrs.member.Member;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.CookieGenerator;

@Component
public class JwtCookieCreatingHandler {
	private final HttpServletResponse response;
	private final CookieGenerator cookieGenerator;
	private final JwtHandler jwtHandler;
	private final OidcProps props;

	String generateToken(Member member) {
		Instant now = Instant.now();
		Instant exp = now.plus(1, ChronoUnit.HOURS);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer(props.getExternalUrl() + "/oauth/token") //
				.expirationTime(Date.from(exp)) //
				.issueTime(Date.from(now)) //
				.subject(member.getMembershipNumber()) //
				.audience("atrs") //
				.claim("scope", Arrays.asList("openid", "member.me")) //
				.claim("name", member.getMail()) //
				.claim("email", member.getMail()) //
				.claim("email_verified", false) //
				.claim("family_name", member.getKanjiFamilyName()) //
				.claim("given_name", member.getKanjiGivenName()) //
				.claim("grant_type", "password") //
				.claim("azp", "atrs") //
				.claim("client_id", "atrs") //
				.build();
		return this.jwtHandler.sign(claimsSet).serialize();
	}

	public JwtCookieCreatingHandler(HttpServletResponse response,
			CookieGenerator cookieGenerator, JwtHandler jwtHandler, OidcProps props) {
		this.response = response;
		this.cookieGenerator = cookieGenerator;
		this.jwtHandler = jwtHandler;
		this.props = props;
	}

	@EventListener
	public void writeCookie(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		AuthLoginUserDetails userDetails = (AuthLoginUserDetails) authentication
				.getPrincipal();
		String jwt = this.generateToken(userDetails.getMember());
		this.cookieGenerator.addCookie(this.response, jwt);
	}
}
