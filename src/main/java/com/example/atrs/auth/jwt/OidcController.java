package com.example.atrs.auth.jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OidcController {

	private final JwtHandler jwtConverter;

	private final OidcProps props;

	public OidcController(JwtHandler jwtConverter, OidcProps props) {
		this.jwtConverter = jwtConverter;
		this.props = props;
	}

	@GetMapping("oauth/token/.well-known/openid-configuration")
	public Object openIdConfiguration() {
		return new LinkedHashMap<String, Object>() {
			{
				put("jwks_uri", props.getExternalUrl() + "/token_keys");
				put("issuer", props.getExternalUrl() + "/oauth/token");
			}
		};
	}

	@GetMapping("token_keys")
	public Object tokenKeys() {
		return Collections.singletonMap("keys",
				Arrays.asList(this.jwtConverter.getKey()));
	}
}
