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

import com.example.atrs.common.logging.LogMessages;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 会員情報登録を行うService実装クラス。
 * 
 * @author NTT 電電花子
 */
@Service
@Transactional
public class MemberRegisterService {

	/**
	 * 会員情報リポジトリ。
	 */
	private final MemberRepository memberRepository;

	/**
	 * パスワードをハッシュ化するためのエンコーダ。
	 */
	private final PasswordEncoder passwordEncoder;

	public MemberRegisterService(MemberRepository memberRepository,
			PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
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

		MemberLogin memberLogin = member.getMemberLogin();
		Assert.notNull(memberLogin);

		// パスワードをエンコード
		String hashedPassword = passwordEncoder
				.encode(member.getMemberLogin().getPassword());

		memberLogin.setPassword(hashedPassword);
		memberLogin.setLastPassword(hashedPassword);
		memberLogin.setLoginFlg(false);

		// 会員情報登録
		// (MyBatis3の機能(SelectKey)によりパラメータの会員情報に会員番号が格納される)
		int insertMemberCount = memberRepository.insert(member);
		if (insertMemberCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(insertMemberCount, 1));
		}

		// 会員ログイン情報登録
		int insertMemberLoginCount = memberRepository.insertMemberLogin(member);
		if (insertMemberLoginCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(insertMemberLoginCount, 1));
		}

		return member;
	}

}
