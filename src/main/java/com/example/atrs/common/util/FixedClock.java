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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 現在日付を固定するための日付生成クラス。
 * 
 * @author NTT 電電太郎
 */
public class FixedClock {

	/**
	 * 日。
	 */
	private int dayOfMonth = 1;

	/**
	 * 時。
	 */
	private int hour;

	/**
	 * 分。
	 */
	private int minute;

	/**
	 * 月。
	 */
	private int month = 1;

	/**
	 * 年。
	 */
	private int year;

	/**
	 * {@inheritDoc}
	 */
	public Clock asClock() {
		Instant fixed = LocalDateTime.of(year, month, dayOfMonth, hour, minute)
				.atZone(ZoneId.systemDefault()).toInstant();
		return Clock.fixed(fixed, ZoneId.systemDefault());
	}

	/**
	 * 日を設定する。
	 *
	 * @param dayOfMonth 日
	 */
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * 時を設定する。
	 *
	 * @param hour 時
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * 分を設定する。
	 *
	 * @param minute 分
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * 月を設定する。
	 *
	 * @param month 月
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * 年を設定する。
	 *
	 * @param year 年
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
