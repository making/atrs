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
package com.example.atrs.ticket.web;

import java.io.Serializable;
import java.util.Date;

import com.example.atrs.ticket.BoardingClassCd;
import com.example.atrs.ticket.FareTypeCd;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 選択フライト情報DTOクラス。
 * 
 * @author NTT 電電次郎
 */
public class SelectFlightDto implements Serializable {

	/**
	 * serialVersionUID。
	 */
	private static final long serialVersionUID = 3204510528862553976L;

	/**
	 * 到着空港コード。
	 */
	private String arrAirportCd;

	/**
	 * 到着時刻。
	 */
	private String arrivalTime;

	/**
	 * 搭乗クラスコード。
	 */
	private BoardingClassCd boardingClassCd;

	/**
	 * 出発空港コード。
	 */
	private String depAirportCd;

	/**
	 * 搭乗日。
	 */
	private Date departureDate;

	/**
	 * 出発時刻。
	 */
	private String departureTime;

	/**
	 * 運賃。
	 */
	private Integer fare;

	/**
	 * 運賃種別コード。
	 */
	private FareTypeCd fareTypeCd;

	/**
	 * 便名。
	 */
	private String flightName;

	/**
	 * 路線種別。
	 */
	private LineType lineType;

	/**
	 * 到着空港コードを取得する。
	 *
	 * @return 到着空港コード
	 */
	public String getArrAirportCd() {
		return arrAirportCd;
	}

	/**
	 * 到着空港コードを設定する。
	 *
	 * @param arrAirportCd 到着空港コード
	 */
	public void setArrAirportCd(String arrAirportCd) {
		this.arrAirportCd = arrAirportCd;
	}

	/**
	 * 到着時刻を取得する。
	 *
	 * @return 到着時刻
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * 到着時刻を設定する。
	 *
	 * @param arrivalTime 到着時刻
	 */
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * 搭乗クラスコードを取得する。
	 *
	 * @return 搭乗クラスコード
	 */
	public BoardingClassCd getBoardingClassCd() {
		return boardingClassCd;
	}

	/**
	 * 搭乗クラスコードを設定する。
	 *
	 * @param boardingClassCd 搭乗クラスコード
	 */
	public void setBoardingClassCd(BoardingClassCd boardingClassCd) {
		this.boardingClassCd = boardingClassCd;
	}

	/**
	 * 出発空港コードを取得する。
	 *
	 * @return 出発空港コード
	 */
	public String getDepAirportCd() {
		return depAirportCd;
	}

	/**
	 * 出発空港コードを設定する。
	 *
	 * @param depAirportCd 出発空港コード
	 */
	public void setDepAirportCd(String depAirportCd) {
		this.depAirportCd = depAirportCd;
	}

	/**
	 * 搭乗日を取得する。
	 *
	 * @return 搭乗日
	 */
	public Date getDepartureDate() {
		return departureDate;
	}

	/**
	 * 搭乗日を設定する。
	 *
	 * @param departureDate 搭乗日
	 */
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	/**
	 * 出発時刻を取得する。
	 *
	 * @return 出発時刻
	 */
	public String getDepartureTime() {
		return departureTime;
	}

	/**
	 * 出発時刻を設定する。
	 *
	 * @param departureTime 出発時刻
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * 運賃を取得する。
	 *
	 * @return 運賃
	 */
	public Integer getFare() {
		return fare;
	}

	/**
	 * 運賃を設定する。
	 *
	 * @param fare 運賃
	 */
	public void setFare(Integer fare) {
		this.fare = fare;
	}

	/**
	 * 運賃種別コードを取得する。
	 *
	 * @return 運賃種別コード
	 */
	public FareTypeCd getFareTypeCd() {
		return fareTypeCd;
	}

	/**
	 * 運賃種別コードを設定する。
	 *
	 * @param fareTypeCd 運賃種別コード
	 */
	public void setFareTypeCd(FareTypeCd fareTypeCd) {
		this.fareTypeCd = fareTypeCd;
	}

	/**
	 * 便名を取得する。
	 *
	 * @return 便名
	 */
	public String getFlightName() {
		return flightName;
	}

	/**
	 * 便名を設定する。
	 *
	 * @param flightName 便名
	 */
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	/**
	 * 路線種別を取得する。
	 *
	 * @return 路線種別
	 */
	public LineType getLineType() {
		return lineType;
	}

	/**
	 * 路線種別を設定する。
	 *
	 * @param lineType 路線種別
	 */
	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
