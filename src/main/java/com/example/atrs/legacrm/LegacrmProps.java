package com.example.atrs.legacrm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.security.util.InMemoryResource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "legacrm")
public class LegacrmProps {
	private String url;
	private String username;
	private String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Resource toPolicyConfiguration() {
		return new InMemoryResource(
				String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<xwss:SecurityConfiguration xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\" dumpMessages=\"true\">\n"
						+ "    <xwss:UsernameToken name=\"%s\" password=\"%s\" digestPassword=\"false\" useNonce=\"false\"/>\n"
						+ "</xwss:SecurityConfiguration>", this.username, this.password));
	}

}
