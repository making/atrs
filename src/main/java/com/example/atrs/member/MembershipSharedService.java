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

import java.text.ParseException;
import java.time.Clock;

import com.example.atrs.auth.LegacrmToAtrs;
import com.example.atrs.auth.jwt.JwtHandler;
import com.example.atrs.auth.jwt.OidcProps;
import com.example.atrs.legacrm.LegacrmService;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 会員共通サービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
public class MembershipSharedService {
	private final Logger log = LoggerFactory.getLogger(MembershipSharedService.class);
	private final LegacrmService legacrmService;
	private final RestTemplate restTemplate;
	private final MemberProperties memberProps;
	private final JwtHandler jwtHandler;
	private final OidcProps oidcProps;
	private final Clock clock;

	public MembershipSharedService(LegacrmService legacrmService,
			RestTemplateBuilder builder, MemberProperties memberProps,
			JwtHandler jwtHandler, OidcProps oidcProps, Clock clock) {
		this.legacrmService = legacrmService;
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
		try {
			String membershipNumber = SignedJWT.parse(jwt).getJWTClaimsSet().getSubject();
			return this.findByMembershipNumber(membershipNumber);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Member findByMembershipNumber(String membershipNumber) {
		Assert.hasText(membershipNumber);
		Member member = this.legacrmService.getMember(membershipNumber,
				LegacrmToAtrs::convert);
		return member;
	}

}
