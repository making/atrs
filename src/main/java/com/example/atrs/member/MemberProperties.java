package com.example.atrs.member;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "member")
public class MemberProperties {
	private String url;

	private String dateOfBirthMinDate;

	public String getDateOfBirthMinDate() {
		return dateOfBirthMinDate;
	}

	public void setDateOfBirthMinDate(String dateOfBirthMinDate) {
		this.dateOfBirthMinDate = dateOfBirthMinDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
