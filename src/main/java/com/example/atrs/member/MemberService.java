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

import com.example.atrs.legacrm.LegacrmService;
import com.example.poc.legacrm.customer.CreateMemberResponse;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 会員情報登録を行うService実装クラス。
 *
 * @author NTT 電電花子
 */
@Service
public class MemberService {
	private final LegacrmService legacrmService;

	public MemberService(LegacrmService legacrmService) {
		this.legacrmService = legacrmService;
	}

	public Member findByMembershipNumber(String membershipNumber) {
		Assert.hasText(membershipNumber);
		Member member = this.legacrmService.getMember(membershipNumber,
				LegacrmToAtrs::convert);
		return member;
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
		CreateMemberResponse response = this.legacrmService
				.createMember(LegacrmToAtrs.reverseForCreate(member));
		return LegacrmToAtrs.convert(response.getMember());
	}

	/**
	 * 会員情報を更新する。
	 *
	 * @param member 会員情報
	 */
	public void update(Member member, String currentPassword) {
		Assert.notNull(member);
		this.legacrmService
				.updateMember(LegacrmToAtrs.reverseForUpdate(member, currentPassword));
	}

}
