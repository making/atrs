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
package com.example.atrs.app.c0;

import java.time.Clock;
import java.time.LocalDate;

import com.example.atrs.config.AtrsProperties;
import com.example.atrs.domain.common.util.DateTimeUtil;
import com.example.atrs.domain.common.validate.ValidationUtil;
import com.example.atrs.domain.service.c0.MemberErrorCode;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.example.atrs.domain.common.util.DateTimeUtil.DATE_FORMATTER;

/**
 * 会員情報フォームの入力チェックを行うバリデータ。
 * 
 * @author NTT 電電花子
 */
@Component
public class MemberValidator implements Validator {

	/**
	 * 日付、時刻取得インターフェース。
	 */
	private final Clock clock;

	/**
	 * 会員登録可能な最小生年月日。
	 */
	private final String dateOfBirthMinDate;

	public MemberValidator(Clock clock, AtrsProperties props) {
		this.clock = clock;
		this.dateOfBirthMinDate = props.getDateOfBirthMinDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return IMemberForm.class.isAssignableFrom(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(Object target, Errors errors) {

		IMemberForm form = (IMemberForm) target;

		// メールアドレスチェック
		if (!errors.hasFieldErrors("mail") && !errors.hasFieldErrors("reEnterMail")) {
			String mail = form.getMail();
			String reEnterMail = form.getReEnterMail();
			if (!mail.equals(reEnterMail)) {
				// メールアドレスと再入力したメールアドレスが一致しない場合にエラー
				errors.reject(MemberErrorCode.E_AR_C0_5001.code());
			}
		}

		// 電話番号チェック
		if (!errors.hasFieldErrors("tel1") && !errors.hasFieldErrors("tel2")) {

			if (!ValidationUtil.isValidTelNum(form.getTel1(), form.getTel2())) {
				// 市外局番と市内局番の合計桁数が6～7桁でなければエラー
				errors.reject(MemberErrorCode.E_AR_C0_5002.code());
			}
		}

		// 生年月日チェック
		if (!errors.hasFieldErrors("dateOfBirth")) {
			LocalDate dateOfBirthMin = LocalDate.parse(dateOfBirthMinDate,
					DATE_FORMATTER);
			LocalDate dateOfBirthMax = LocalDate.now(this.clock);
			LocalDate dateOfBirth = DateTimeUtil.toLocalDate(form.getDateOfBirth());

			if (dateOfBirth.isBefore(dateOfBirthMin)
					&& dateOfBirth.isAfter(dateOfBirthMax)) {
				// 生年月日の入力許容範囲(1900年1月1日から現在まで)でなければエラー
				errors.reject(MemberErrorCode.E_AR_C0_5003.code(), new Object[] {
						dateOfBirthMinDate, dateOfBirthMax.format(DATE_FORMATTER) }, "");
			}
		}

	}
}
