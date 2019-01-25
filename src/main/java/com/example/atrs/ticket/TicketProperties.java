package com.example.atrs.ticket;

import java.time.Duration;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ticket")
public class TicketProperties {
	private int adultPassengerMinAge;

	private int childFareRate;

	private int limitDay;

	private int representativeMinAge;

	private Duration reserveIntervalTime;

	private DefaultProperties defaults;

	public int getAdultPassengerMinAge() {
		return adultPassengerMinAge;
	}

	public void setAdultPassengerMinAge(int adultPassengerMinAge) {
		this.adultPassengerMinAge = adultPassengerMinAge;
	}

	public int getChildFareRate() {
		return childFareRate;
	}

	public void setChildFareRate(int childFareRate) {
		this.childFareRate = childFareRate;
	}

	public int getLimitDay() {
		return limitDay;
	}

	public void setLimitDay(int limitDay) {
		this.limitDay = limitDay;
	}

	public int getRepresentativeMinAge() {
		return representativeMinAge;
	}

	public void setRepresentativeMinAge(int representativeMinAge) {
		this.representativeMinAge = representativeMinAge;
	}

	public Duration getReserveIntervalTime() {
		return reserveIntervalTime;
	}

	public void setReserveIntervalTime(Duration reserveIntervalTime) {
		this.reserveIntervalTime = reserveIntervalTime;
	}

	public DefaultProperties getDefaults() {
		return defaults;
	}

	public void setDefaults(DefaultProperties defaults) {
		this.defaults = defaults;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static class DefaultProperties {
		private String arrAirportCd;

		private BoardingClassCd boardingClassCd;

		private String depAirportCd;

		private FlightType flightType;

		public String getArrAirportCd() {
			return arrAirportCd;
		}

		public void setArrAirportCd(String arrAirportCd) {
			this.arrAirportCd = arrAirportCd;
		}

		public BoardingClassCd getBoardingClassCd() {
			return boardingClassCd;
		}

		public void setBoardingClassCd(BoardingClassCd boardingClassCd) {
			this.boardingClassCd = boardingClassCd;
		}

		public String getDepAirportCd() {
			return depAirportCd;
		}

		public void setDepAirportCd(String depAirportCd) {
			this.depAirportCd = depAirportCd;
		}

		public FlightType getFlightType() {
			return flightType;
		}

		public void setFlightType(FlightType flightType) {
			this.flightType = flightType;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
