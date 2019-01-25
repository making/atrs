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
package com.example.atrs.common.web.security;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.atrs.common.security.AtrsLogoutSuccessEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * ユーザーログアウト成功ハンドラ。
 * 
 * @author NTT 電電太郎
 */
@Component
public class AtrsLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	/**
	 * イベント発行Publisher。
	 */
	private final ApplicationEventPublisher eventPublisher;

	public AtrsLogoutSuccessHandler(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@PostConstruct
	public void init() {
		this.setDefaultTargetUrl("/");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		if (authentication != null) {
			// ログアウト成功イベントを発行
			eventPublisher.publishEvent(new AtrsLogoutSuccessEvent(authentication));
		}
		super.handle(request, response, authentication);
	}

}