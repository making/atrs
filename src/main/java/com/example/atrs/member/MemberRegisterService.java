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

import com.example.atrs.auth.AuthLogin;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 会員情報登録を行うService実装クラス。
 * 
 * @author NTT 電電花子
 */
@Service
@Transactional
public class MemberRegisterService {
	private final RestTemplate restTemplate;
	private final MemberProperties props;

	public MemberRegisterService(RestTemplateBuilder builder, MemberProperties props) {
		this.restTemplate = builder.build();
		this.props = props;
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
		AuthLogin authLogin = member.getAuthLogin();
		Assert.notNull(authLogin);
		RequestEntity<Member> requestEntity = RequestEntity.post(UriComponentsBuilder
				.fromHttpUrl(props.getUrl()).pathSegment("members", "me").build().toUri())
				.body(member);
		ResponseEntity<Member> exchange = this.restTemplate.exchange(requestEntity,
				Member.class);
		return exchange.getBody();
	}

}
