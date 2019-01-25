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
package com.example.atrs.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日時に関するユーティリティクラス。
 * 
 * @author NTT 電電太郎
 */
public class DateTimeUtil {

	/**
	 * 日付(文字列)のパースに使用するフォーマッタ。
	 */
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern("uuuu/MM/dd");

	/**
	 * 時間(文字列)のパースに使用するフォーマッタ。
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
			.ofPattern("HHmm");

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * DateTimeへ変換する。
	 *
	 * @param date 日付オブジェクト
	 * @param timeString 時刻文字列(HHmm)
	 * @return 引数で指定された日付および時刻を保持するDateTimeオブジェクト
	 */
	public static LocalDateTime toDateTime(Date date, String timeString) {
		return toLocalDate(date).atTime(LocalTime.parse(timeString, TIME_FORMATTER));
	}

	/**
	 * 整形日付文字列(yyyy/MM/dd)へ変換する。
	 *
	 * @param date 日付オブジェクト
	 * @return 日付文字列(yyyy/MM/dd)
	 */
	public static String toFormatDateString(Date date) {
		if (date == null) {
			return "";
		}
		return toLocalDate(date).format(DATE_FORMATTER);
	}

	/**
	 * 整形時刻文字列(HH:mm)へ変換する。
	 * 
	 * @param timeString 時刻文字列(HHmm)
	 * @return 時刻文字列(HH:mm)
	 */
	public static String toFormatTimeString(String timeString) {
		String result = timeString;
		if (timeString != null && 2 < timeString.length()) {
			result = String.format("%s:%s", timeString.substring(0, 2),
					timeString.substring(2));
		}
		return result;
	}

}
