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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 会員共通サービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
public class MembershipSharedService {

	/**
	 * カード会員情報リポジトリ。
	 */
	private final MemberMapper memberMapper;

	public MembershipSharedService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	/**
	 * 会員であるか判定する。
	 *
	 * @param membershipNumber 会員番号
	 * @return 会員の場合true
	 */
	@Transactional(readOnly = true)
	public boolean isMember(String membershipNumber) {

		Assert.hasText(membershipNumber);

		// 該当する会員情報が存在するか判定
		return (memberMapper.findOne(membershipNumber) != null);
	}

}
