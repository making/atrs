package com.example.atrs.legacrm;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.support.SaajUtils;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.ClientHttpRequestConnection;

@Component
public class TraceClientInterceptor implements ClientInterceptor {
	private final Tracer tracer;

	public TraceClientInterceptor(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public boolean handleRequest(MessageContext messageContext)
			throws WebServiceClientException {
		TransportContext context = TransportContextHolder.getTransportContext();
		ClientHttpRequestConnection connection = (ClientHttpRequestConnection) context
				.getConnection();
		Span span = this.tracer.nextSpan();
		span.kind(Span.Kind.CLIENT);
		String name = "";
		try {
			name = SaajUtils
					.getFirstBodyElement(((SaajSoapMessage) messageContext.getRequest())
							.getSaajMessage().getSOAPPart().getEnvelope().getBody())
					.getLocalName();
		}
		catch (SOAPException ignored) {
		}
		span.name("soap:" + name);
		TraceContext traceContext = span.context();
		try {
			connection.addRequestHeader("X-B3-SpanId", traceContext.spanIdString());
			connection.addRequestHeader("X-B3-TraceId", traceContext.traceIdString());
			if (traceContext.parentId() != null) {
				connection.addRequestHeader("X-B3-ParentSpanId",
						traceContext.parentIdString());
			}
			connection.addRequestHeader("X-B3-Sampled",
					String.valueOf(traceContext.sampled()));
		}
		catch (IOException ignored) {
		}
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext)
			throws WebServiceClientException {
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext)
			throws WebServiceClientException {
		return true;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex)
			throws WebServiceClientException {

	}
}
