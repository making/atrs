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

import com.example.atrs.common.logging.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 会員ログアウトサービス実装クラス。
 * 
 * @author NTT 電電太郎
 */
@Service
@Transactional
public class AuthLogoutService {

	/**
	 * ロガー。
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthLogoutService.class);

	/**
	 * カード会員情報リポジトリ。
	 */
	private final AuthLoginMapper authLoginMapper;

	public AuthLogoutService(AuthLoginMapper authLoginMapper) {
		this.authLoginMapper = authLoginMapper;
	}

	/**
	 * ログアウト時に必要な業務処理を行う。
	 *
	 * @param authLogin 会員情報
	 */
	public void logout(AuthLogin authLogin) {

		Assert.notNull(authLogin);

		// ログインフラグを更新
		authLogin.setLoginFlg(false);
		int updateCount = authLoginMapper.updateLogoutStatus(authLogin);
		if (updateCount != 1) {
			throw new SystemException(LogMessages.E_AR_A0_L9002.getCode(),
					LogMessages.E_AR_A0_L9002.getMessage(updateCount, 1));
		}

		LOGGER.info(
				LogMessages.I_AR_A2_L0001.getMessage(authLogin.getMembershipNumber()));
	}
}
