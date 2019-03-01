package com.example.atrs.legacrm;

import com.example.poc.legacrm.customer.GetMemberResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("default") // for debug purpose
public class LegacrmTestController {
	private final LegacrmService legacrmService;

	public LegacrmTestController(LegacrmService legacrmService) {
		this.legacrmService = legacrmService;
	}

	@GetMapping(path = "demo/{id}")
	public GetMemberResponse demo(@PathVariable String id) {
		return this.legacrmService.getMember(id);
	}
}
