package com.example.atrs.auth.jwt;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;

import com.example.atrs.auth.LegacrmToAtrs;
import com.example.atrs.legacrm.LegacrmService;
import com.example.atrs.member.Member;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
	private final JwtHandler jwtConverter;
	private final OidcProps props;
	private final LegacrmService legacrmService;
	private final PasswordEncoder passwordEncoder;
	private final JwtCookieCreatingHandler jwtCookieCreatingHandler;

	public TokenController(JwtHandler jwtConverter, OidcProps props,
			LegacrmService legacrmService, PasswordEncoder passwordEncoder,
			JwtCookieCreatingHandler jwtCookieCreatingHandler) {
		this.jwtConverter = jwtConverter;
		this.props = props;
		this.legacrmService = legacrmService;
		this.passwordEncoder = passwordEncoder;
		this.jwtCookieCreatingHandler = jwtCookieCreatingHandler;
	}

	@PostMapping(path = "oauth/token", params = "grant_type=password")
	public Object issueToken(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		Member member = this.legacrmService.getMember(username, LegacrmToAtrs::convert);
		if (!passwordEncoder.matches(password, member.getPassword())) {
			return Collections.singletonMap("error", "ng!");
		}
		return new LinkedHashMap<String, Object>() {
			{
				put("token_type", "bearer");
				SignedJWT idToken = jwtCookieCreatingHandler.generateIdToken(member);
				SignedJWT accessToken = jwtCookieCreatingHandler
						.generateAccessToken(member);
				put("access_token", accessToken.serialize());
				put("id_token", idToken.serialize());
				try {
					JWTClaimsSet claims = accessToken.getJWTClaimsSet();
					Duration between = Duration.between(Instant.now(),
							claims.getExpirationTime().toInstant());
					put("expires_in", between.toMinutes() * 60);
				}
				catch (ParseException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}
}
