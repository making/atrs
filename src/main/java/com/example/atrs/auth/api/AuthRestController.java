/*
 * Copyright 2014-2018 NTT Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.atrs.auth.api;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.example.atrs.auth.AuthLogin;
import com.example.atrs.auth.AuthLoginMapper;
import com.example.atrs.auth.jwt.JwtHandler;
import com.example.atrs.common.exception.AtrsBusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.MDC;
import org.terasoluna.gfw.common.exception.SystemException;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.example.atrs.common.logging.LogMessages.E_AR_A0_L9002;
import static com.example.atrs.member.MemberErrorCode.E_AR_C2_2001;

@RestController
public class AuthRestController {
	private final AuthLoginMapper authLoginMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtHandler jwtHandler;
	private final Clock clock;

	public AuthRestController(AuthLoginMapper authLoginMapper,
			PasswordEncoder passwordEncoder, JwtHandler jwtHandler, Clock clock) {
		this.authLoginMapper = authLoginMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtHandler = jwtHandler;
		this.clock = clock;
	}

	@PostMapping(path = "auth")
	public Object create(@RequestBody AuthLogin authLogin) {
		String hashedPassword = this.passwordEncoder.encode(authLogin.getPassword());
		authLogin.setPassword(hashedPassword);
		authLogin.setLastPassword(hashedPassword);
		authLogin.setLoginFlg(false);
		int count = this.authLoginMapper.insert(authLogin);
		if (count != 1) {
			throw new SystemException(E_AR_A0_L9002.getCode(),
					E_AR_A0_L9002.getMessage(count, 1));
		}
		return Collections.singletonMap("membershipNumber",
				authLogin.getMembershipNumber());
	}

	@PutMapping(path = "auth/password")
	public Object updatePassword(
			@RequestHeader(name = "Authorization") String authorization,
			@RequestBody UpdatePassword updatePassword) throws Exception {
		JWT jwt = JWTParser.parse(authorization.replace("Bearer ", ""));
		this.checkToken((SignedJWT) jwt);

		String membershipNumber = jwt.getJWTClaimsSet().getSubject();
		MDC.put("user", membershipNumber);

		AuthLogin authLogin = this.authLoginMapper.findOne(membershipNumber);
		String currentPassword = authLogin.getPassword();
		if (!this.passwordEncoder.matches(updatePassword.getCurrentPassword(),
				currentPassword)) {
			throw new AtrsBusinessException(E_AR_C2_2001);
		}
		authLogin.setPassword(
				this.passwordEncoder.encode(updatePassword.getNewPassword()));
		authLogin.setLastPassword(currentPassword);
		int count = this.authLoginMapper.update(authLogin);
		if (count != 1) {
			throw new SystemException(E_AR_A0_L9002.getCode(),
					E_AR_A0_L9002.getMessage(count, 1));
		}
		return authLogin;
	}

	void checkToken(SignedJWT jwt) throws Exception {
		if (!this.jwtHandler.verifyToken(jwt)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The given signature was not successfully verified.");
		}
		JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
		Instant expiration = jwtClaimsSet.getExpirationTime().toInstant();
		Instant now = Instant.now(this.clock);
		if (now.isAfter(expiration)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"The given token has been expired.");
		}

		List<String> scope = jwtClaimsSet.getStringListClaim("scope");
		if (!scope.contains("member.me")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(
					"The token provided has insufficient scope [%s] for this request. scope = member.me",
					scope));
		}
	}

	static class UpdatePassword {
		private final String currentPassword;
		private final String newPassword;

		@JsonCreator
		public UpdatePassword(@JsonProperty("currentPassword") String currentPassword,
				@JsonProperty("newPassword") String newPassword) {
			this.currentPassword = currentPassword;
			this.newPassword = newPassword;
		}

		public String getCurrentPassword() {
			return currentPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}
	}
}
