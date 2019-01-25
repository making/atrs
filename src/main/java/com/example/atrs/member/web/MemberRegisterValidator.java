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

import com.example.atrs.member.MemberRegisterErrorCode;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * 会員情報登録フォームのバリデータ。
 * 
 * @author NTT 電電花子
 */
@Component
public class MemberRegisterValidator implements Validator {

	/**
	 * 会員情報バリデータ。
	 */
	private final MemberValidator memberValidator;

	public MemberRegisterValidator(MemberValidator memberValidator) {
		this.memberValidator = memberValidator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return MemberRegisterForm.class.isAssignableFrom(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(Object target, Errors errors) {

		MemberRegisterForm form = (MemberRegisterForm) target;

		// パスワードチェック
		if (!errors.hasFieldErrors("password")
				&& !errors.hasFieldErrors("reEnterPassword")) {

			String password = form.getPassword();
			String reEnterPassword = form.getReEnterPassword();
			if (!password.equals(reEnterPassword)) {

				// パスワードと再入力パスワードが異なる場合エラー
				errors.reject(MemberRegisterErrorCode.E_AR_C1_5001.code());
			}
		}

		// 共通チェック
		ValidationUtils.invokeValidator(memberValidator, form, errors);

	}
}