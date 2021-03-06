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
package com.example.atrs.common.web.logging;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * アクセスログを出力するフィルタ。
 * 
 * @author NTT 電電太郎
 */
public class AccessLogFilter extends OncePerRequestFilter {

	/**
	 * ロガー。
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogFilter.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String logMessage = getLogMessage(request);

		LOGGER.info("[ACCESS START    ] {}", logMessage);
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			LOGGER.info("[ACCESS FINISH   ] Status:{}\t{}", response.getStatus(),
					logMessage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request)
			throws ServletException {

		String uri = request.getRequestURI();
		if (uri.startsWith(request.getContextPath() + "/resources/")) {
			return true;
		}

		return false;
	}

	/**
	 * ログメッセージを取得する。
	 *
	 * @param request リクエスト
	 * @return ログメッセージ
	 */
	private String getLogMessage(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("Method:").append(request.getMethod()).append("\t");
		sb.append("URL:").append(request.getRequestURL().toString());
		String queryString = request.getQueryString();
		if (queryString != null) {
			sb.append("?").append(queryString);
		}
		sb.append("\t");
		HttpSession session = request.getSession(false);
		if (session != null) {
			sb.append("SessionID:").append(session.getId()).append("\t");
		}
		sb.append("RemoteAddress:").append(request.getRemoteAddr());
		return sb.toString();
	}
}
