package com.example.atrs.auth.jwt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.annotation.PostConstruct;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtHandler {

	private static final Logger logger = LoggerFactory.getLogger(JwtHandler.class);

	private JWSSigner signer;

	private JWSVerifier verifier;

	private String verifierKey;

	private String signingKey;

	private JSONObject tokenKey;

	public void setKeyPair(KeyPair keyPair) {
		PrivateKey privateKey = keyPair.getPrivate();
		signer = new RSASSASigner(privateKey);
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		verifier = new RSASSAVerifier(publicKey);
		verifierKey = "-----BEGIN PUBLIC KEY-----\n"
				+ Base64Utils.encodeToString(publicKey.getEncoded())
				+ "\n-----END PUBLIC KEY-----";
	}

	public void setSigningKey(String key) throws Exception {
		this.signingKey = key;
		key = key.trim();

		key = key.replace("-----BEGIN RSA PRIVATE KEY-----\n", "")
				.replace("-----END RSA PRIVATE KEY-----", "").trim().replace("\n", "");
		byte[] encoded = Base64Utils.decodeFromString(key);
		DerInputStream derInputStream = new DerInputStream(encoded);
		DerValue[] seq = derInputStream.getSequence(0);

		BigInteger modulus = seq[1].getBigInteger();
		BigInteger publicExp = seq[2].getBigInteger();
		BigInteger privateExp = seq[3].getBigInteger();
		BigInteger prime1 = seq[4].getBigInteger();
		BigInteger prime2 = seq[5].getBigInteger();
		BigInteger exp1 = seq[6].getBigInteger();
		BigInteger exp2 = seq[7].getBigInteger();
		BigInteger crtCoef = seq[8].getBigInteger();

		RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp,
				privateExp, prime1, prime2, exp1, exp2, crtCoef);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(keySpec);
		this.signer = new RSASSASigner(privateKey);
	}

	public String getSigningKey() {
		return signingKey;
	}

	public void setVerifierKey(String key) {
		this.verifierKey = key;
	}

	public String getVerifierKey() {
		return verifierKey;
	}

	public SignedJWT sign(JWTClaimsSet claimsSet) {
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256) //
				.keyID(getKey().getAsString("kid"))//
				.type(JOSEObjectType.JWT) //
				.build();
		SignedJWT signedJWT = new SignedJWT(header, claimsSet);
		try {
			signedJWT.sign(signer);
		}
		catch (JOSEException e) {
			throw new IllegalStateException(e);
		}
		return signedJWT;
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		if (this.verifier != null) {
			return;
		}

		String key = this.verifierKey.replace("-----BEGIN PUBLIC KEY-----\n", "")
				.replace("-----END PUBLIC KEY-----", "").trim().replace("\n", "");

		byte[] decode = Base64Utils.decodeFromString(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
		JWSVerifier verifier = new RSASSAVerifier(publicKey);
		this.tokenKey = new RSAKey.Builder(publicKey).build().toJSONObject();
		this.tokenKey.put("kid", "initial");
		this.tokenKey.put("alg", JWSAlgorithm.RS256.getName());
		this.tokenKey.put("use", "sig");
		this.tokenKey.put("value", verifierKey);
		this.verifier = verifier;

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject("test").build();
		SignedJWT jwt = sign(claimsSet);

		if (!this.verifyToken(jwt)) {
			throw new IllegalStateException(
					"The pair of verifierKey and signingKey is wrong.");
		}
	}

	public JSONObject getKey() {
		return this.tokenKey;
	}

	public boolean verifyToken(SignedJWT jwt) throws JOSEException {
		return jwt.verify(this.verifier);
	}
}
