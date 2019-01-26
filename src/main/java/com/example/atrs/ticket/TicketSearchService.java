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

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.atrs.common.exception.AtrsBusinessException;
import com.example.atrs.common.util.DateTimeUtil;
import org.terasoluna.gfw.common.exception.BusinessException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.example.atrs.ticket.TicketErrorCode.E_AR_B1_2002;

/**
 * 空席照会サービス実装クラス。
 * 
 * @author NTT 電電次郎
 */
@Service
@Transactional
public class TicketSearchService {
	/**
	 * 搭乗クラス情報提供クラス。
	 */
	private final BoardingClassProvider boardingClassProvider;

	private final Clock clock;

	/**
	 * 運賃種別情報提供クラス。
	 */
	private final FareTypeProvider fareTypeProvider;

	/**
	 * フライト基本情報提供クラス。
	 */
	private final FlightMasterProvider flightMasterProvider;

	/**
	 * フライト情報リポジトリ。
	 */
	private final FlightRepository flightRepository;

	/**
	 * 区間情報提供クラス。
	 */
	private final RouteProvider routeProvider;

	/**
	 * チケット予約共通サービス。
	 */
	private final TicketSharedService ticketSharedService;

	public TicketSearchService(Clock clock, FlightRepository flightRepository,
							   RouteProvider routeProvider, FareTypeProvider fareTypeProvider,
							   FlightMasterProvider flightMasterProvider,
							   BoardingClassProvider boardingClassProvider,
							   TicketSharedService ticketSharedService) {
		this.clock = clock;
		this.flightRepository = flightRepository;
		this.routeProvider = routeProvider;
		this.fareTypeProvider = fareTypeProvider;
		this.flightMasterProvider = flightMasterProvider;
		this.boardingClassProvider = boardingClassProvider;
		this.ticketSharedService = ticketSharedService;
	}

	/**
	 * 空席照会を行う。
	 * <p>
	 * 検索条件に合致する便と、その便の運賃種別ごとの運賃・空席数を返却する。 便は出発時刻の昇順でソートされる。
	 * </p>
	 *
	 * @param searchCriteria 空席照会の検索条件
	 * @return 空席照会の検索結果
	 * @throws BusinessException 該当する空席情報が存在しない場合の業務例外
	 */
	public List<FlightVacantInfoDto> searchFlight(TicketSearchCriteriaDto searchCriteria)
			throws BusinessException {

		// 引数チェック
		Assert.notNull(searchCriteria);

		Date depDate = searchCriteria.getDepDate();
		BoardingClassCd boardingClassCd = searchCriteria.getBoardingClassCd();
		String depAirportCd = searchCriteria.getDepartureAirportCd();
		String arrAirportCd = searchCriteria.getArrivalAirportCd();
		FlightType flightType = searchCriteria.getFlightType();

		Assert.notNull(depDate);
		Assert.notNull(boardingClassCd);
		Assert.hasText(depAirportCd);
		Assert.hasText(arrAirportCd);
		Assert.notNull(flightType);

		// 搭乗日が照会可能な範囲かチェック
		ticketSharedService.validateDepatureDate(depDate);

		// 指定された出発空港・到着空港に該当する区間が存在するかどうかチェック
		Route route = routeProvider.getRouteByAirportCd(depAirportCd, arrAirportCd);
		if (route == null) {
			throw new AtrsBusinessException(E_AR_B1_2002);
		}

		// システム日付が搭乗日から何日前かを計算
		LocalDateTime today = LocalDate.now(this.clock).atTime(0, 0);
		LocalDateTime depLocalDate = DateTimeUtil.toLocalDate(depDate).atTime(0, 0);

		int beforeDayNum = (int) Duration.between(today, depLocalDate).toDays();

		// フライト種別に応じて運賃種別コードを空席照会条件Dtoに設定
		List<FareTypeCd> fareTypeList = flightType.getFareTypeCdList();

		VacantSeatSearchCriteriaDto criteria = new VacantSeatSearchCriteriaDto(depDate,
				route, boardingClassCd, beforeDayNum, fareTypeList);

		// リポジトリから照会結果を取得
		List<Flight> flightList = flightRepository
				.findByVacantSeatSearchCriteria(criteria);

		// 照会結果件数をチェック
		if (flightList.isEmpty()) {
			throw new FlightNotFoundException();
		}

		// 取得したフライトに関連するエンティティを設定
		for (Flight flight : flightList) {
			FareTypeCd fareTypeCd = flight.getFareType().getFareTypeCd();
			flight.setFareType(fareTypeProvider.getFareType(fareTypeCd));
			flight.setFlightMaster(flightMasterProvider
					.getFlightMaster(flight.getFlightMaster().getFlightName()));
			flight.setBoardingClass(boardingClassProvider
					.getBoardingClass(flight.getBoardingClass().getBoardingClassCd()));
		}

		// 基本運賃の計算
		int basicFare = ticketSharedService.calculateBasicFare(route.getBasicFare(),
				boardingClassCd, depDate);

		// 照会結果のリストを作成
		List<FlightVacantInfoDto> flightVacantInfoList = createFlightVacantInfoList(
				flightList, basicFare);

		return flightVacantInfoList;
	}

	/**
	 * 空席状況情報を作成する。
	 *
	 * @param flight フライト情報
	 * @return 空席照会結果
	 */
	private FlightVacantInfoDto createFlightVacantInfo(Flight flight) {

		FlightVacantInfoDto vacantInfo = new FlightVacantInfoDto();

		FlightMaster flightMaster = flight.getFlightMaster();
		vacantInfo.setFlightName(flightMaster.getFlightName());
		Route route = flightMaster.getRoute();
		vacantInfo.setDepAirportName(route.getDepartureAirport().getName());
		vacantInfo.setArrAirportName(route.getArrivalAirport().getName());
		String depTime = DateTimeUtil.toFormatTimeString(flightMaster.getDepartureTime());
		vacantInfo.setDepTime(depTime);
		String arrTime = DateTimeUtil.toFormatTimeString(flightMaster.getArrivalTime());
		vacantInfo.setArrTime(arrTime);
		vacantInfo.setDepDate(DateTimeUtil.toFormatDateString(flight.getDepartureDate()));
		vacantInfo.setBoardingClassCd(flight.getBoardingClass().getBoardingClassCd());

		return vacantInfo;
	}

	/**
	 * フライトリストから空席状況一覧リストを作成する。
	 *
	 * @param flightList フライトリスト
	 * @param basicFare 基本運賃
	 * @return 空席状況一覧リスト
	 */
	private List<FlightVacantInfoDto> createFlightVacantInfoList(List<Flight> flightList,
			int basicFare) {

		// 空席状況一覧Map
		Map<String, FlightVacantInfoDto> vacantInfoMap = new LinkedHashMap<>();

		DecimalFormat fareFormatter = new DecimalFormat("###,###");

		for (Flight flight : flightList) {

			FlightMaster flightMaster = flight.getFlightMaster();
			String departureTime = flightMaster.getDepartureTime();
			FlightVacantInfoDto vacantInfo = vacantInfoMap.get(departureTime);
			if (vacantInfo == null) {
				vacantInfo = createFlightVacantInfo(flight);
				vacantInfoMap.put(departureTime, vacantInfo);
			}

			// 運賃種別情報を設定
			FareType fareType = flight.getFareType();
			int fare = ticketSharedService.calculateFare(basicFare,
					fareType.getDiscountRate());
			FareTypeVacantInfoDto fareTypeVacantInfo = new FareTypeVacantInfoDto(
					fareType.getFareTypeName(), fareFormatter.format(fare),
					flight.getVacantNum());

			vacantInfo.addFareTypeVacantInfo(fareType.getFareTypeCd(),
					fareTypeVacantInfo);
		}

		// リストに変換して返却
		return new ArrayList<>(vacantInfoMap.values());
	}

}
