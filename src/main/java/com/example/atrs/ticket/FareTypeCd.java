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
package com.example.atrs.ticket;

/**
 * 運賃種別コードの列挙型。
 * 
 * @author NTT 電電太郎
 */
public enum FareTypeCd {

	/**
	 * 片道運賃の運賃種別コード。
	 */
	OW(false),

	/**
	 * 往復運賃の運賃種別コード。
	 */
	RT(true),

	/**
	 * 予約割1の運賃種別コード。
	 */
	RD1(false),

	/**
	 * 予約割7の運賃種別コード。
	 */
	RD7(false),

	/**
	 * 早期割の運賃種別コード。
	 */
	ED(false),

	/**
	 * レディース割の運賃種別コード。
	 */
	LD(false),

	/**
	 * グループ割の運賃種別コード。
	 */
	GD(false),

	/**
	 * 特別片道運賃の運賃種別コード。
	 */
	SOW(false),

	/**
	 * 特別往復運賃の運賃種別コード。
	 */
	SRT(true),

	/**
	 * 特別予約割の運賃種別コード。
	 */
	SRD(false);

	private final boolean roundTrip;

	FareTypeCd(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	/**
	 * 運賃種別が往復の運賃種別かを判定する。
	 * @return 往復の運賃種別の場合true
	 */
	public boolean isRoundTrip() {
		return this.roundTrip;
	}

	/**
	 * 運賃種別が片道の運賃種別かを判定する。
	 * @return 片道の運賃種別の場合true
	 */
	public boolean isOneWay() {
		return !this.roundTrip;
	}

	/**
	 * 運賃種別コードを取得する。
	 *
	 * @return 運賃種別コード
	 */
	public String getCode() {
		return this.name();
	}

}
