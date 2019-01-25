package com.example.atrs.ticket;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default")
public class DefaultProperties {
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
