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

import java.util.Locale;

import com.example.atrs.common.logging.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.example.atrs.auth.AuthErrorCode.E_AR_A1_2001;

/**
 * ログインユーザ情報サービス。
 * 
 * @author NTT 電電太郎
 */
@Component
@Transactional
public class AuthLoginUserDetailsService implements UserDetailsService {

	/**
	 * ロガー。
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthLoginUserDetailsService.class);

	/**
	 * カード会員情報リポジトリ。
	 */
	private final AuthLoginMapper authLoginMapper;

	/**
	 * メッセージプロパティ設定。
	 */
	private final MessageSource messageSource;

	public AuthLoginUserDetailsService(MessageSource messageSource,
                                       AuthLoginMapper authLoginMapper) {
		this.messageSource = messageSource;
		this.authLoginMapper = authLoginMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		Assert.hasText(username);

		// 会員情報を取得
		AuthLogin authLogin = authLoginMapper.findOne(username);

		if (authLogin == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(LogMessages.I_AR_A1_L2001.getMessage(username));
			}
			String errorMessage = messageSource.getMessage(E_AR_A1_2001.code(), null,
					Locale.getDefault());
			// 該当する会員情報が存在しない場合、例外をスロー
			throw new UsernameNotFoundException(errorMessage);
		}

		return new AuthLoginUserDetails(authLogin);
	}

}
