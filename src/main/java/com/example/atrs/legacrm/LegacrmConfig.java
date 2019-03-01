package com.example.atrs.legacrm;

import org.springframework.boot.webservices.client.WebServiceTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;

@Configuration
public class LegacrmConfig {
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setContextPath("com.example.poc.legacrm.customer");
		return jaxb2Marshaller;
	}

	@Bean
	public WebServiceTemplateCustomizer webServiceTemplateCustomizer(
			Jaxb2Marshaller jaxb2Marshaller, LegacrmProps props,
			TraceClientInterceptor traceClientInterceptor) {
		return webServiceTemplate -> {
			webServiceTemplate.setMarshaller(jaxb2Marshaller);
			webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
			webServiceTemplate.setDefaultUri(props.getUrl() + "/ws");
			XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
			securityInterceptor.setPolicyConfiguration(props.toPolicyConfiguration());
			securityInterceptor.setCallbackHandler(callbacks -> {
			});
			try {
				securityInterceptor.afterPropertiesSet();
			}
			catch (Exception e) {
				throw new IllegalStateException(e);
			}
			webServiceTemplate.setInterceptors(new ClientInterceptor[] {
					securityInterceptor, traceClientInterceptor });
		};
	}
}
