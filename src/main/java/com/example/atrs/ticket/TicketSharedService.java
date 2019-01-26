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

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.example.atrs.common.exception.AtrsBusinessException;
import com.example.atrs.common.util.DateTimeUtil;
import com.example.atrs.common.util.FareUtil;
import org.terasoluna.gfw.common.exception.BusinessException;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.example.atrs.ticket.TicketErrorCode.E_AR_B1_2001;

/**
 * チケット予約共通サービス実装クラス。
 * 
 * @author NTT 電電次郎
 */
@Service
public class TicketSharedService {

	/**
	 * 通常時のピーク時期積算比率(%)。
	 */
	private static final int MULTIPLICATION_RATIO_IN_NORMAL_TIME = 100;

	/**
	 * 搭乗クラス情報提供クラス。
	 */
	private final BoardingClassProvider boardingClassProvider;

	private final Clock clock;

	/**
	 * フライト情報リポジトリ。
	 */
	private final FlightRepository flightRepository;

	/**
	 * 予約可能限界日数。
	 */
	private final int limitDay;

	/**
	 * ピーク時期情報提供クラス。
	 */
	private final PeakTimeProvider peakTimeProvider;

	/**
	 * 往路の到着時刻に対し、復路で予約可能となる出発時刻までの時間間隔。
	 */
	private final Duration reserveIntervalTime;

	public TicketSharedService(TicketProperties props, Clock clock,
			BoardingClassProvider boardingClassProvider,
			PeakTimeProvider peakTimeProvider, FlightRepository flightRepository) {
		this.reserveIntervalTime = props.getReserveIntervalTime();
		this.limitDay = props.getLimitDay();
		this.clock = clock;
		this.boardingClassProvider = boardingClassProvider;
		this.peakTimeProvider = peakTimeProvider;
		this.flightRepository = flightRepository;
	}

	/**
	 * 基本運賃を計算する。
	 *
	 * @param basicFareOfRoute 区間の基本運賃
	 * @param boardingClassCd 搭乗クラスコード
	 * @param depDate 搭乗日
	 * @return 基本運賃
	 */
	public int calculateBasicFare(int basicFareOfRoute, BoardingClassCd boardingClassCd,
			Date depDate) {
		Assert.isTrue(basicFareOfRoute >= 0);
		Assert.notNull(boardingClassCd);
		Assert.notNull(depDate);

		// 搭乗クラスの加算料金の取得
		BoardingClass boardingClass = boardingClassProvider
				.getBoardingClass(boardingClassCd);
		int boardingClassFare = boardingClass.getExtraCharge();

		// 搭乗日の料金積算比率の取得
		int multiplicationRatio = getMultiplicationRatio(depDate);

		// 基本運賃の計算
		int basicFare = (int) ((basicFareOfRoute + boardingClassFare)
				* (multiplicationRatio * 0.01));

		return basicFare;
	}

	/**
	 * 運賃を計算する。
	 *
	 * @param basicFare 基本運賃
	 * @param discountRate 割引率
	 * @return 運賃
	 */
	public int calculateFare(int basicFare, int discountRate) {
		Assert.isTrue(basicFare >= 0);
		Assert.isTrue(discountRate >= 0);
		Assert.isTrue(discountRate <= 100);

		// 運賃の計算
		int fare = (int) (basicFare * (1 - (discountRate * 0.01)));

		// 運賃の100円未満を切上げて返却
		return FareUtil.ceilFare(fare);
	}

	/**
	 * フライト情報の存在をチェックする。
	 *
	 * @param flight フライト情報
	 * @return 存在：true 不存在：false
	 */
	public boolean existsFlight(Flight flight) {
		Assert.notNull(flight);
		Assert.notNull(flight.getFlightMaster());
		return flightRepository.exists(flight.getDepartureDate(),
				flight.getFlightMaster().getFlightName(), flight.getBoardingClass(),
				flight.getFareType());
	}

	/**
	 * 照会可能限界日付を取得する。
	 *
	 * @return 照会可能限界日付
	 */
	public LocalDate getSearchLimitDate() {
		// 照会可能限界日付 = システム日付＋予約可能限界日数
		LocalDate today = LocalDate.now(this.clock);
		LocalDate limitDate = today.plusDays(limitDay);
		return limitDate;
	}

	/**
	 * 搭乗日が運賃種別の予約可能時期に含まれるかをチェックする。
	 * <p>
	 * 予約可能となる条件: 予約可能前日数(始) >= 搭乗日前日数 >= 予約可能前日数(終)
	 * </p>
	 *
	 * @param fareType 運賃種別
	 * @param depDate 搭乗日
	 * @return 予約可能の場合trueを返却、予約不可の場合はfalseを返却
	 */
	public boolean isAvailableFareType(FareType fareType, Date depDate) {
		Assert.notNull(fareType);
		Assert.notNull(depDate);

		LocalDate departureDate = DateTimeUtil.toLocalDate(depDate);

		// 予約開始日付
		LocalDate rsrvAvailableStartDate = departureDate
				.minusDays(fareType.getRsrvAvailableStartDayNum());

		// 予約終了日付
		LocalDate rsrvAvailableEndDate = departureDate
				.minusDays(fareType.getRsrvAvailableEndDayNum());

		// 現在日付
		LocalDate today = LocalDate.now(this.clock);

		// 現在日付が予約可能開始日付～予約可能終了日付の間であるかチェック
		return !(today.isBefore(rsrvAvailableStartDate)
				|| today.isAfter(rsrvAvailableEndDate));
	}

	/**
	 * 搭乗日の有効性チェックを実行する。
	 *
	 * @param departureDate 搭乗日
	 * @throws BusinessException 業務例外
	 */
	public void validateDepatureDate(Date departureDate) throws BusinessException {
		Assert.notNull(departureDate);

		LocalDate today = LocalDate.now(this.clock);
		LocalDate limitDate = getSearchLimitDate();

		// 指定された搭乗日が本日から照会可能限界日迄の間にあるかチェック
		LocalDate depDate = DateTimeUtil.toLocalDate(departureDate);
		if (depDate.isBefore(today) || depDate.isAfter(limitDate)) {
			throw new AtrsBusinessException(E_AR_B1_2001);
		}
	}

	/**
	 * 選択フライト情報が予約可能な条件かどうかをチェックする。
	 *
	 * @param flightList 選択フライト情報一覧
	 * @throws BusinessException 業務例外
	 * @throws InvalidFlightException フライト情報不正例外
	 */
	public void validateFlightList(List<Flight> flightList)
			throws BusinessException, InvalidFlightException {

		Assert.notEmpty(flightList);
		Assert.isTrue(flightList.size() <= 2);

		// 往路フライトのチェックを行う
		Flight outwardFlight = flightList.get(0);
		Assert.notNull(outwardFlight);
		if (outwardFlight.getFlightMaster() == null) {
			throw new InvalidFlightException("flightMaster is null :" + outwardFlight);
		}
		if (!existsFlight(outwardFlight)) {
			throw new InvalidFlightException("flight not found :" + outwardFlight);
		}

		// 2件の場合、往復路フライトのチェックを行う
		if (flightList.size() == 2) {

			// 復路フライト
			Flight homewardFlight = flightList.get(1);
			Assert.notNull(homewardFlight);
			if (homewardFlight.getFlightMaster() == null) {
				throw new InvalidFlightException(
						"flightMaster is null :" + homewardFlight);
			}
			if (!existsFlight(homewardFlight)) {
				throw new InvalidFlightException("flight not found :" + homewardFlight);
			}

			// 選択した復路のフライトが搭乗範囲内であることを検証
			validateFlightDepartureDateForRoundTripFlight(outwardFlight, homewardFlight);

			// 復路が往路の逆区間であることを検証
			validateFlightRouteForRoundTripFlight(outwardFlight, homewardFlight);

		}

		// 運賃種別が選択可能なものであることを検証
		validateFlightFareType(flightList);

	}

	/**
	 * 搭乗日の料金積算比率を取得する。
	 *
	 * @param departureDate 搭乗日
	 * @return 搭乗日の料金積算比率
	 */
	private int getMultiplicationRatio(Date departureDate) {

		// 該当するピーク時期情報を取得
		PeakTime peakTime = peakTimeProvider.getPeakTime(departureDate);

		// 該当するピーク時期が存在する場合、積算比率を返却
		if (peakTime != null) {
			return peakTime.getMultiplicationRatio();
		}

		// 該当するピーク時期が存在しない場合は、通常時の積算比率を返却
		return MULTIPLICATION_RATIO_IN_NORMAL_TIME;
	}

	/**
	 * 往路、復路フライトの搭乗日、時刻に関するチェック。
	 *
	 * @param outwardFlight 往路フライト情報
	 * @param homewardFlight 復路フライト情報
	 * @throws BusinessException 業務例外
	 */
	private void validateFlightDepartureDateForRoundTripFlight(Flight outwardFlight,
			Flight homewardFlight) throws BusinessException {

		// 往路のフライトの到着時刻
		LocalDateTime outwardArriveDateTime = DateTimeUtil.toDateTime(
				outwardFlight.getDepartureDate(),
				outwardFlight.getFlightMaster().getArrivalTime());

		// 復路のフライト出発時刻
		LocalDateTime homewardDepartureDateTime = DateTimeUtil.toDateTime(
				homewardFlight.getDepartureDate(),
				homewardFlight.getFlightMaster().getDepartureTime());

		// 選択した復路のフライトが搭乗範囲外の場合、業務例外をスロー
		// (復路のフライトは往路のフライトの到着時刻より指定時間間隔以上経過した
		// 出発時刻から搭乗可能となる)
		if (outwardArriveDateTime.plusMinutes(reserveIntervalTime.toMinutes())
				.isAfter(homewardDepartureDateTime)) {
			throw new AtrsBusinessException(TicketErrorCode.E_AR_B2_2001);
		}
	}

	/**
	 * フライトの運賃種別に関するチェック。
	 *
	 * @param flightList フライト情報リスト
	 * @throws BusinessException 業務例外
	 */
	private void validateFlightFareType(List<Flight> flightList)
			throws BusinessException {

		FareTypeCd owFareTypeCd;
		FareTypeCd hwFareTypeCd;

		switch (flightList.size()) {
		case 1:
			// 片道の場合

			// 往路の運賃種別は片道運賃であることを検証
			owFareTypeCd = flightList.get(0).getFareType().getFareTypeCd();
			if (!owFareTypeCd.isOneWay()) {
				throw new InvalidFlightException(
						"outward flight fareType is invalid :" + owFareTypeCd);
			}
			break;

		case 2:
			// 往復の場合

			// 往路の運賃種別は往復運賃または特別往復運賃であることを検証
			owFareTypeCd = flightList.get(0).getFareType().getFareTypeCd();
			if (!owFareTypeCd.isRoundTrip()) {
				throw new InvalidFlightException(
						"outward flight fareType is invalid :" + owFareTypeCd);
			}

			// 復路の運賃種別が往復運賃または特別往復運賃でない場合、フライト不正例外をスロー
			hwFareTypeCd = flightList.get(1).getFareType().getFareTypeCd();
			if (!owFareTypeCd.isRoundTrip()) {
				throw new InvalidFlightException(
						"homeward flight fareType is invalid :" + hwFareTypeCd);
			}
			break;

		default:
			throw new InvalidFlightException("flightList size must be between 1 and 2");
		}
	}

	/**
	 * 往路、復路フライトの区間に関するチェック。
	 *
	 * @param outwardFlight 往路フライト情報
	 * @param homewardFlight 復路フライト情報
	 * @throws BusinessException 業務例外
	 */
	private void validateFlightRouteForRoundTripFlight(Flight outwardFlight,
			Flight homewardFlight) throws BusinessException {

		// 復路が往路の逆区間であることを確認
		Route outwardRoute = outwardFlight.getFlightMaster().getRoute();
		Route homewardRoute = homewardFlight.getFlightMaster().getRoute();
		if (!outwardRoute.getDepartureAirport().getCode()
				.equals(homewardRoute.getArrivalAirport().getCode())
				|| !outwardRoute.getArrivalAirport().getCode()
						.equals(homewardRoute.getDepartureAirport().getCode())) {

			// 復路が往路の逆区間でない場合、フライト不正例外をスロー
			throw new InvalidFlightException(
					"homeward route is not outward route reverse");
		}
	}
}
