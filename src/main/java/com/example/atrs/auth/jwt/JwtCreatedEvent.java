package com.example.atrs.auth.jwt;

import org.springframework.context.ApplicationEvent;

public class JwtCreatedEvent extends ApplicationEvent {

	public JwtCreatedEvent(Object source) {
		super(source);
	}
}
