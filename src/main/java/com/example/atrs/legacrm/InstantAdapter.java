package com.example.atrs.legacrm;

import java.time.Instant;
import java.time.ZonedDateTime;

public class InstantAdapter {

	public static Instant parse(String value) {
		return ZonedDateTime.parse(value).toInstant();
	}

	public static String print(Instant value) {
		return value.toString();
	}
}
