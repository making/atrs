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
package com.example.atrs.auth;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import com.example.atrs.common.logging.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 会員ログインサービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
@Transactional
public class AuthLoginService {

	/**
	 * ロガー。
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthLoginService.class);

	private final Clock clock;

	/**
	 * カード会員情報リポジトリ。
	 */
	private final AuthLoginMapper authLoginMapper;

	public AuthLoginService(Clock clock, AuthLoginMapper authLoginMapper) {
		this.clock = clock;
		this.authLoginMapper = authLoginMapper;
	}

	/**
	 * ログイン時に必要な業務処理を行う。
	 *
	 * @param authLogin 会員情報
	 */
	public void login(AuthLogin authLogin) {

		// パラメータチェック
		Assert.notNull(authLogin);

		// ログインフラグ、ログイン日時を更新
		Instant now = Instant.now(this.clock);
		authLogin.setLoginDateTime(Date.from(now));
		authLogin.setLoginFlg(true);
		int updateCount = authLoginMapper.updateLoginStatus(authLogin);
		if (updateCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(updateCount, 1));
		}

		LOGGER.info(
				LogMessages.I_AR_A1_L0001.getMessage(authLogin.getMembershipNumber()));
	}

}
