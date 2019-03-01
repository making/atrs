package com.example.atrs.legacrm;

import java.util.function.Function;

import com.example.poc.legacrm.customer.GetMemberRequest;
import com.example.poc.legacrm.customer.GetMemberResponse;

import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class LegacrmService {
	private final WebServiceTemplate webServiceTemplate;

	public LegacrmService(WebServiceTemplateBuilder builder) {
		this.webServiceTemplate = builder.build();
	}

	public GetMemberResponse getMember(String membershipNumber) {
		return (GetMemberResponse) this.webServiceTemplate
				.marshalSendAndReceive(new GetMemberRequest(membershipNumber));
	}

	public <T> T getMember(String membershipNumber,
			Function<GetMemberResponse, T> converter) {
		return converter.apply(this.getMember(membershipNumber));
	}
}
