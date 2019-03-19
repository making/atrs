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

import com.nimbusds.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 会員情報変更を行うService実装クラス。
 * 
 * @author NTT 電電花子
 */
@Service
@Transactional
public class MemberUpdateService {
	private static Logger log = LoggerFactory.getLogger(MemberUpdateService.class);
	private final RestTemplate restTemplate;
	private final MemberProperties props;

	public MemberUpdateService(RestTemplateBuilder builder, MemberProperties props) {
		this.restTemplate = builder.build();
		this.props = props;
	}

	/**
	 * 会員情報を更新する。
	 *
	 * @param member 会員情報
	 */
	public void updateMember(Member member, JWT jwt) {
		Assert.notNull(member);
		log.info("Update({})", member);
		RequestEntity<Member> requestEntity = RequestEntity
				.put(UriComponentsBuilder.fromHttpUrl(props.getUrl())
						.pathSegment("members", "me").build().toUri()) //
				.header(AUTHORIZATION, "Bearer " + jwt.serialize()) //
				.body(member);
		this.restTemplate.exchange(requestEntity, Member.class);
	}

}
