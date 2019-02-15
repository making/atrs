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

import java.util.Collections;
import java.util.HashMap;

import com.example.atrs.common.logging.LogMessages;
import com.fasterxml.jackson.databind.JsonNode;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.atrs.common.logging.LogMessages.E_AR_A0_L9002;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 会員情報登録を行うService実装クラス。
 *
 * @author NTT 電電花子
 */
@Service
@Transactional
public class MemberService {
	private final MemberMapper memberMapper;
	private final RestTemplate restTemplate;
	private final String issuerUrl;

	public MemberService(MemberMapper memberMapper, RestTemplateBuilder builder,
			@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUrl) {
		this.memberMapper = memberMapper;
		this.restTemplate = builder.build();
		this.issuerUrl = issuerUrl;
	}

	/**
	 * 会員情報を登録する。
	 * <p>
	 * 登録時に発出された会員番号を格納した会員情報インスタンスが返される。
	 * </p>
	 *
	 * @param member 会員情報
	 * @return Member 会員番号が格納された会員情報
	 */
	public Member register(Member member) {
		Assert.notNull(member);
		Assert.notNull(member.getPassword());

		// 会員ログイン情報登録
		RequestEntity<?> requestEntity = RequestEntity
				.post(UriComponentsBuilder.fromHttpUrl(this.issuerUrl).replacePath("auth")
						.build().toUri())
				.body(Collections.singletonMap("password", member.getPassword()));
		ResponseEntity<JsonNode> responseEntity = this.restTemplate
				.exchange(requestEntity, JsonNode.class);

		member.setMembershipNumber(
				responseEntity.getBody().get("membershipNumber").asText());
		// 会員情報登録
		int insertMemberCount = memberMapper.insert(member);
		if (insertMemberCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(insertMemberCount, 1));
		}

		return member;
	}

	/**
	 * 会員情報を更新する。
	 *
	 * @param member 会員情報
	 */
	public void update(Member member, String jwt) {
		Assert.notNull(member);
		// 会員情報更新
		int count = memberMapper.update(member);
		if (count != 1) {
			throw new SystemException(E_AR_A0_L9002.getCode(),
					E_AR_A0_L9002.getMessage(count, 1));
		}
		// 会員ログイン情報更新
		if (StringUtils.hasLength(member.getPassword())) {
			RequestEntity<?> requestEntity = RequestEntity
					.put(UriComponentsBuilder.fromHttpUrl(this.issuerUrl)
							.replacePath("auth/password").build().toUri())
					.header(AUTHORIZATION, String.format("Bearer %s", jwt)) //
					.body(new HashMap<String, String>() {
						{
							put("currentPassword", member.getCurrentPassword());
							put("newPassword", member.getPassword());
						}
					});
			this.restTemplate.exchange(requestEntity, JsonNode.class);
		}
	}

}
