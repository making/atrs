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
package com.example.atrs.member;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import com.example.atrs.auth.jwt.JwtHandler;
import com.example.atrs.auth.jwt.OidcProps;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;

/**
 * 会員共通サービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
public class MembershipSharedService {
	private final Logger log = LoggerFactory.getLogger(MembershipSharedService.class);
	private final RestTemplate restTemplate;
	private final MemberProperties memberProps;
	private final JwtHandler jwtHandler;
	private final OidcProps oidcProps;
	private final Clock clock;

	public MembershipSharedService(RestTemplateBuilder builder,
			MemberProperties memberProps, JwtHandler jwtHandler, OidcProps oidcProps,
			Clock clock) {
		this.restTemplate = builder.build();
		this.memberProps = memberProps;
		this.jwtHandler = jwtHandler;
		this.oidcProps = oidcProps;
		this.clock = clock;
	}

	/***
	 * 会員番号に該当する会員情報を取得する。
	 *
	 * @param jwt JWT
	 * @return 会員番号に該当するユーザの会員情報
	 */
	public Member findMe(String jwt) {
		Assert.hasText(jwt);
		RequestEntity<?> requestEntity = RequestEntity
				.get(UriComponentsBuilder.fromHttpUrl(memberProps.getUrl())
						.pathSegment("members", "me").build().toUri())
				.header(AUTHORIZATION, String.format("Bearer %s", jwt)).build();
		try {
			return this.restTemplate.exchange(requestEntity, Member.class).getBody();
		}
		catch (HttpClientErrorException e) {
			if (HttpStatus.UNAUTHORIZED == e.getStatusCode()) {
				HttpHeaders responseHeaders = e.getResponseHeaders();
				if (responseHeaders != null
						&& responseHeaders.containsKey(WWW_AUTHENTICATE)) {
					log.warn("headers={}", responseHeaders.get(WWW_AUTHENTICATE));
				}
			}
			throw e;
		}
	}

	public Member findByMembershipNumber(String membershipNumber) {
		Assert.hasText(membershipNumber);
		Instant now = Instant.now(this.clock);
		Instant exp = now.plusSeconds(60);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.issuer(oidcProps.getExternalUrl() + "/oauth/token") //
				.expirationTime(Date.from(exp)) //
				.issueTime(Date.from(now)) //
				.subject("atrs") //
				.audience("atrs") //
				.claim("scope", Arrays.asList("member.read")) //
				.claim("name", "atrs") //
				.claim("grant_type", "client_credentials") //
				.claim("azp", "atrs") //
				.claim("client_id", "atrs") //
				.build();
		SignedJWT jwt = this.jwtHandler.sign(claimsSet);
		RequestEntity<?> requestEntity = RequestEntity
				.get(UriComponentsBuilder.fromHttpUrl(memberProps.getUrl())
						.pathSegment("members", membershipNumber).build().toUri())
				.header(AUTHORIZATION, String.format("Bearer %s", jwt.serialize()))
				.build();
		try {
			return this.restTemplate.exchange(requestEntity, Member.class).getBody();
		}
		catch (HttpClientErrorException e) {
			if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
				return null;
			}
			throw e;
		}
	}

}
