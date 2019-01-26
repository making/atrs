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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * フライト種別の列挙型。
 * 
 * @author NTT 電電太郎
 */
public enum FlightType {

	/**
	 * 往復のフライト種別。
	 */
	RT(FareTypeCd::isRoundTrip),

	/**
	 * 片道のフライト種別。
	 */
	OW(FareTypeCd::isOneWay);

	private final Predicate<FareTypeCd> codeFilter;

	FlightType(Predicate<FareTypeCd> codeFilter) {
		this.codeFilter = codeFilter;
	}

	/**
	 * フライト種別を取得する。
	 * 
	 * @return フライト種別
	 */
	public String getCode() {
		return this.name();
	}

	/**
	 * フライト種別に応じた運賃種別リストを取得する。
	 *
	 * @return 運賃種別リスト
	 */
	public List<FareTypeCd> getFareTypeCdList() {
		return Stream.of(FareTypeCd.values()).filter(this.codeFilter).collect(toList());
	}

}
