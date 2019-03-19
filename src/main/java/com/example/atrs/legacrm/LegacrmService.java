package com.example.atrs.legacrm;

import java.util.function.Function;

import com.example.poc.legacrm.customer.CreateMemberRequest;
import com.example.poc.legacrm.customer.CreateMemberResponse;
import com.example.poc.legacrm.customer.GetMemberRequest;
import com.example.poc.legacrm.customer.GetMemberResponse;
import com.example.poc.legacrm.customer.UpdateMemberRequest;
import com.example.poc.legacrm.customer.UpdateMemberResponse;

import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class LegacrmService {
	private final WebServiceTemplate webServiceTemplate;

	public LegacrmService(WebServiceTemplateBuilder builder) {
		this.webServiceTemplate = builder.build();
	}

	@NewSpan
	public GetMemberResponse getMember(
			@SpanTag("membershipNumber") String membershipNumber) {
		return (GetMemberResponse) this.webServiceTemplate
				.marshalSendAndReceive(new GetMemberRequest(membershipNumber));
	}

	@NewSpan
	public <T> T getMember(@SpanTag("membershipNumber") String membershipNumber,
			Function<GetMemberResponse, T> converter) {
		return converter.apply(this.getMember(membershipNumber));
	}

	@NewSpan
	public CreateMemberResponse createMember(CreateMemberRequest request) {
		return (CreateMemberResponse) this.webServiceTemplate
				.marshalSendAndReceive(request);
	}

	@NewSpan
	public UpdateMemberResponse updateMember(UpdateMemberRequest request) {
		return (UpdateMemberResponse) this.webServiceTemplate
				.marshalSendAndReceive(request);
	}
}
