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

import com.example.atrs.auth.LegacrmToAtrs;
import com.example.atrs.legacrm.LegacrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.ws.client.WebServiceClientException;

/**
 * 会員共通サービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
public class MembershipSharedService {
	private final Logger log = LoggerFactory.getLogger(MembershipSharedService.class);
	private final LegacrmService legacrmService;

	public MembershipSharedService(LegacrmService legacrmService) {
		this.legacrmService = legacrmService;
	}

	public Member findByMembershipNumber(String membershipNumber) {
		Assert.hasText(membershipNumber);
		try {
			return this.legacrmService.getMember(membershipNumber,
					LegacrmToAtrs::convert);
		}
		catch (WebServiceClientException e) {
			return null;
		}
	}

}
