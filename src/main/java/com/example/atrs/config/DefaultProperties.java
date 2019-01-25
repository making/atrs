package com.example.atrs.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.atrs.ticket.BoardingClassCd;
import com.example.atrs.ticket.FlightType;

@Component
@ConfigurationProperties(prefix = "default")
public class DefaultProperties {
	private FlightType flightType;
	private String depAirportCd;
	private String arrAirportCd;
	private BoardingClassCd boardingClassCd;

	public FlightType getFlightType() {
		return flightType;
	}

	public void setFlightType(FlightType flightType) {
		this.flightType = flightType;
	}

	public String getDepAirportCd() {
		return depAirportCd;
	}

	public void setDepAirportCd(String depAirportCd) {
		this.depAirportCd = depAirportCd;
	}

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
