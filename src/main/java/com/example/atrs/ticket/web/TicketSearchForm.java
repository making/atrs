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

import javax.validation.constraints.NotNull;

import com.example.atrs.ticket.BoardingClassCd;
import com.example.atrs.ticket.FlightType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 空席照会フォーム。
 * 
 * @author NTT 電電次郎
 */
public class TicketSearchForm implements Serializable {

	/**
	 * serialVersionUID。
	 */
	private static final long serialVersionUID = 2340727779297558393L;

	/**
	 * 到着空港コード。
	 */
	@NotNull
	@ExistInCodeList(codeListId = "CL_AIRPORT")
	private String arrAirportCd;

	/**
	 * 搭乗クラスコード。
	 */
	@NotNull
	private BoardingClassCd boardingClassCd;

	/**
	 * 出発空港コード。
	 */
	@NotNull
	@ExistInCodeList(codeListId = "CL_AIRPORT")
	private String depAirportCd;

	/**
	 * フライト種別。
	 */
	@NotNull
	private FlightType flightType;

	/**
	 * 復路搭乗日。
	 */
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date homewardDate;

	/**
	 * 往路搭乗日。
	 */
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date outwardDate;

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
	 * フライト種別を取得する。
	 *
	 * @return フライト種別
	 */
	public FlightType getFlightType() {
		return flightType;
	}

	/**
	 * フライト種別を設定する。
	 *
	 * @param flightType フライト種別
	 */
	public void setFlightType(FlightType flightType) {
		this.flightType = flightType;
	}

	/**
	 * 復路搭乗日を取得する。
	 * 
	 * @return 復路搭乗日
	 */
	public Date getHomewardDate() {
		return homewardDate;
	}

	/**
	 * 復路搭乗日を設定する。
	 * 
	 * @param homewardDate 復路搭乗日
	 */
	public void setHomewardDate(Date homewardDate) {
		this.homewardDate = homewardDate;
	}

	/**
	 * 往路搭乗日を取得する。
	 *
	 * @return 往路搭乗日
	 */
	public Date getOutwardDate() {
		return outwardDate;
	}

	/**
	 * 往路搭乗日を設定する。
	 *
	 * @param outwardDate 往路搭乗日
	 */
	public void setOutwardDate(Date outwardDate) {
		this.outwardDate = outwardDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}
