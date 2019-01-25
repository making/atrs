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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.example.atrs.ticket.api.FlightSearchOutputDto;
import com.example.atrs.common.web.exception.BadRequestException;
import com.example.atrs.config.AtrsProperties;
import com.example.atrs.config.DefaultProperties;
import com.example.atrs.ticket.BoardingClassCd;
import com.example.atrs.ticket.Flight;
import com.example.atrs.ticket.FlightType;
import com.example.atrs.ticket.InvalidFlightException;
import com.example.atrs.ticket.TicketSharedService;
import org.terasoluna.gfw.common.exception.BusinessException;

import org.springframework.stereotype.Component;

/**
 * 空席照会Helper。
 * 
 * @author NTT 電電次郎
 */
@Component
public class TicketSearchHelper {

	private final Clock clock;

	/**
	 * チケット予約共通サービス。
	 */
	private final TicketSharedService ticketSharedService;

	/**
	 * デフォルトフライト種別。
	 */
	private final FlightType defaultFlightType;

	/**
	 * デフォルト出発空港コード。
	 */
	private final String defaultDepAirportCd;

	/**
	 * デフォルト到着空港コード。
	 */
	private final String defaultArrAirportCd;

	/**
	 * デフォルト搭乗クラスコード。
	 */
	private final BoardingClassCd defaultBoardingClassCd;

	/**
	 * 往路の到着時刻に対し、復路で予約可能となる出発時刻までの時間間隔(分)。
	 */
	private final int reserveIntervalTime;

	public TicketSearchHelper(Clock clock, TicketSharedService ticketSharedService,
			DefaultProperties defaultProps, AtrsProperties atrsProps) {
		this.clock = clock;
		this.ticketSharedService = ticketSharedService;
		this.defaultFlightType = defaultProps.getFlightType();
		this.defaultDepAirportCd = defaultProps.getDepAirportCd();
		this.defaultArrAirportCd = defaultProps.getArrAirportCd();
		this.defaultBoardingClassCd = defaultProps.getBoardingClassCd();
		this.reserveIntervalTime = atrsProps.getReserveIntervalTime();
	}

	/**
	 * デフォルト値を持つ空席照会フォームを作成する。
	 * 
	 * @return 空席照会フォーム
	 */
	public TicketSearchForm createDefaultTicketSearchForm() {

		TicketSearchForm ticketSearchForm = new TicketSearchForm();
		ticketSearchForm.setFlightType(defaultFlightType);
		ticketSearchForm.setDepAirportCd(defaultDepAirportCd);
		ticketSearchForm.setArrAirportCd(defaultArrAirportCd);
		Date now = Date.from(Instant.now(this.clock));
		ticketSearchForm.setOutwardDate(now);
		ticketSearchForm.setHomewardDate(now);
		ticketSearchForm.setBoardingClassCd(defaultBoardingClassCd);

		return ticketSearchForm;
	}

	/**
	 * 空席照会画面(TOP画面)の表示情報を作成する。
	 * 
	 * @return 空席照会画面(TOP画面)の表示情報
	 */
	public FlightSearchOutputDto createFlightSearchOutputDto() {
		Date now = Date.from(Instant.now(this.clock));
		FlightSearchOutputDto outputDto = new FlightSearchOutputDto();
		outputDto.setBeginningPeriod(now);
		outputDto.setEndingPeriod(Date.from(ticketSharedService.getSearchLimitDate()
				.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		outputDto.setReserveIntervalTime(reserveIntervalTime);

		return outputDto;
	}

	/**
	 * フライト情報のリストをチェックする。
	 * <p>
	 * 業務チェックエラーの場合、業務例外をスローする。 他のエラーの場合、不正リクエスト例外をスローする。
	 * </p>
	 * 
	 * @param flightList フライト情報のリスト
	 * @throws BusinessException 業務例外
	 * @throws BadRequestException 不正リクエスト例外
	 */
	public void validateFlightList(List<Flight> flightList)
			throws BusinessException, BadRequestException {

		// フライト情報チェック
		try {
			ticketSharedService.validateFlightList(flightList);
		}
		catch (InvalidFlightException e) {

			// 業務チェックエラー以外のエラーの場合、不正リクエスト例外をスロー
			throw new BadRequestException(e);
		}
	}

}
