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
package com.example.atrs.member.web;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import com.example.atrs.config.AtrsProperties;
import com.example.atrs.member.Member;
import com.github.dozermapper.core.Mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.example.atrs.common.util.DateTimeUtil.DATE_FORMATTER;

/**
 * 会員情報Helper。
 * 
 * @author NTT 電電花子
 */
@Component
public class MemberHelper {

	/**
	 * Beanマッパー。
	 */
	private final Mapper beanMapper;

	private final Clock clock;

	/**
	 * 会員登録可能な最小生年月日。
	 */
	private final String dateOfBirthMinDate;

	public MemberHelper(Mapper beanMapper, Clock clock, AtrsProperties props) {
		this.beanMapper = beanMapper;
		this.clock = clock;
		this.dateOfBirthMinDate = props.getDateOfBirthMinDate();
	}

	/**
	 * 会員情報フォームをカード会員情報に変換する。
	 * 
	 * @param memberForm 会員情報フォーム
	 * @return カード会員情報
	 */
	public Member toMember(IMemberForm memberForm) {

		// MemberFormからmemberへ詰め替える
		Member member = beanMapper.map(memberForm, Member.class);

		// 電話番号
		String tel = String.format("%s-%s-%s", memberForm.getTel1(), memberForm.getTel2(),
				memberForm.getTel3());
		member.setTel(tel);

		// 郵便番号
		String zipCode = memberForm.getZipCode1() + memberForm.getZipCode2();
		member.setZipCode(zipCode);

		// クレジットカード期限
		String creditTerm = String.format("%s/%s", memberForm.getCreditMonth(),
				memberForm.getCreditYear());
		member.setCreditTerm(creditTerm);

		return member;

	}

	/**
	 * カード会員情報を会員情報フォームに変換する。
	 * 
	 * @param member カード会員情報
	 * @return 会員情報フォーム
	 */
	public MemberUpdateForm toMemberUpdateForm(Member member) {

		MemberUpdateForm memberUpdateForm = beanMapper.map(member,
				MemberUpdateForm.class);

		// 電話番号
		String[] tel = member.getTel().split("-");
		if (tel.length == 3) {
			memberUpdateForm.setTel1(tel[0]);
			memberUpdateForm.setTel2(tel[1]);
			memberUpdateForm.setTel3(tel[2]);
		}

		// 郵便番号
		if (StringUtils.hasLength(member.getZipCode())
				&& member.getZipCode().length() >= 7) {
			memberUpdateForm.setZipCode1(member.getZipCode().substring(0, 3));
			memberUpdateForm.setZipCode2(member.getZipCode().substring(3, 7));
		}

		// クレジットカード期限
		String[] creditTerm = member.getCreditTerm().split("/");
		if (creditTerm.length == 2) {
			memberUpdateForm.setCreditMonth(creditTerm[0]);
			memberUpdateForm.setCreditYear(creditTerm[1]);
		}

		return memberUpdateForm;
	}

	/**
	 * 会員登録可能な最小生年月日を取得する。
	 * 
	 * @return 会員登録可能な最小生年月日
	 */
	public String getDateOfBirthMinDate() {
		return dateOfBirthMinDate;
	}

	/**
	 * 会員登録可能な最大生年月日を取得する。
	 * 
	 * @return 会員登録可能な最大生年月日
	 */
	public String getDateOfBirthMaxDate() {
		return Instant.now(this.clock).atZone(ZoneId.systemDefault())
				.format(DATE_FORMATTER);
	}

}
