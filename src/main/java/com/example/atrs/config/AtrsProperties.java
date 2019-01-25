package com.example.atrs.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "atrs")
public class AtrsProperties {
	private String dateOfBirthMinDate;
	private int representativeMinAge;
	private int adultPassengerMinAge;
	private int childFareRate;
	private int reserveIntervalTime;
	private int limitDay;

	public String getDateOfBirthMinDate() {
		return dateOfBirthMinDate;
	}

	public void setDateOfBirthMinDate(String dateOfBirthMinDate) {
		this.dateOfBirthMinDate = dateOfBirthMinDate;
	}

	public int getRepresentativeMinAge() {
		return representativeMinAge;
	}

	public void setRepresentativeMinAge(int representativeMinAge) {
		this.representativeMinAge = representativeMinAge;
	}

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

	public int getReserveIntervalTime() {
		return reserveIntervalTime;
	}

	public void setReserveIntervalTime(int reserveIntervalTime) {
		this.reserveIntervalTime = reserveIntervalTime;
	}

	public int getLimitDay() {
		return limitDay;
	}

	public void setLimitDay(int limitDay) {
		this.limitDay = limitDay;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
