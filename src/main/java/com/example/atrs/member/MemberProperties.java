package com.example.atrs.member;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "atrs")
public class MemberProperties {

	private String dateOfBirthMinDate;

	public String getDateOfBirthMinDate() {
		return dateOfBirthMinDate;
	}

	public void setDateOfBirthMinDate(String dateOfBirthMinDate) {
		this.dateOfBirthMinDate = dateOfBirthMinDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
