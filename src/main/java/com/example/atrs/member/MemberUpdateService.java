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
import com.example.atrs.auth.AuthLoginMapper;
import com.example.atrs.common.exception.AtrsBusinessException;
import com.example.atrs.common.logging.LogMessages;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static com.example.atrs.member.MemberErrorCode.E_AR_C2_2001;

/**
 * 会員情報変更を行うService実装クラス。
 * 
 * @author NTT 電電花子
 */
@Service
@Transactional
public class MemberUpdateService {
	private final AuthLoginMapper authLoginMapper;
	/**
	 * 会員情報リポジトリ。
	 */
	private final MemberMapper memberMapper;

	/**
	 * パスワードをハッシュ化するためのエンコーダ。
	 */
	private final PasswordEncoder passwordEncoder;

	public MemberUpdateService(AuthLoginMapper authLoginMapper, MemberMapper memberMapper,
			PasswordEncoder passwordEncoder) {
		this.authLoginMapper = authLoginMapper;
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 引数に渡されたパスワードがDBに登録されているパスワードと同一かチェックする。
	 *
	 * @param password チェックするパスワード
	 * @param membershipNumber パスワードを確認する会員の会員番号
	 */
	public void checkMemberPassword(String password, String membershipNumber) {

		// パスワードの変更がある場合のみパスワードを比較
		if (StringUtils.hasLength(password)) {

			// 登録パスワードを取得
			Member member = memberMapper.findOne(membershipNumber);
			String currentPassword = member.getAuthLogin().getPassword();

			// パスワード不一致の場合、業務例外をスロー
			if (!passwordEncoder.matches(password, currentPassword)) {
				throw new AtrsBusinessException(E_AR_C2_2001);
			}
		}
	}

	/**
	 * 会員番号に該当する会員情報を取得する。
	 *
	 * @param membershipNumber 会員番号
	 * @return 会員番号に該当するユーザの会員情報
	 */
	public Member findMember(String membershipNumber) {

		Assert.hasText(membershipNumber);

		return memberMapper.findOne(membershipNumber);
	}

	/**
	 * 会員番号に該当するカード会員情報(ログイン時に必要な情報のみ)を取得する。
	 *
	 * @param membershipNumber 会員番号
	 * @return カード会員情報(ログイン時に必要な情報のみ)
	 */
	public AuthLogin findMemberForLogin(String membershipNumber) {

		Assert.hasText(membershipNumber);

		return authLoginMapper.findOne(membershipNumber);
	}

	/**
	 * 会員情報を更新する。
	 *
	 * @param member 会員情報
	 */
	public void updateMember(Member member) {

		Assert.notNull(member);
		AuthLogin authLogin = member.getAuthLogin();
		Assert.notNull(authLogin);

		// 会員情報更新
		int updateMemberCount = memberMapper.update(member);
		if (updateMemberCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(updateMemberCount, 1));
		}

		// パスワードの変更がある場合のみ会員ログイン情報を更新
		if (StringUtils.hasLength(authLogin.getPassword())) {

			// パスワードのハッシュ化
			authLogin.setPassword(passwordEncoder.encode(authLogin.getPassword()));

			// 会員ログイン情報更新
			int updateAuthLoginCount = authLoginMapper.update(authLogin);
			if (updateAuthLoginCount != 1) {
				throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
						LogMessages.E_AR_A0_L9002.getMessage(updateAuthLoginCount, 1));
			}
		}
	}

}
