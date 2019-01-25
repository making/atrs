package com.example.atrs.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "atrs")
public class AtrsProperties {
	private int adultPassengerMinAge;

	private int childFareRate;

	private String dateOfBirthMinDate;

	private int limitDay;

	private int representativeMinAge;

	private int reserveIntervalTime;

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

	public String getDateOfBirthMinDate() {
		return dateOfBirthMinDate;
	}

	public void setDateOfBirthMinDate(String dateOfBirthMinDate) {
		this.dateOfBirthMinDate = dateOfBirthMinDate;
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

	public int getReserveIntervalTime() {
		return reserveIntervalTime;
	}

	public void setReserveIntervalTime(int reserveIntervalTime) {
		this.reserveIntervalTime = reserveIntervalTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
