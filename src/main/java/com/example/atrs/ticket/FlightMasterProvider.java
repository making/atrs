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
package com.example.atrs.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * フライト基本情報を提供するクラス。
 * 
 * @author NTT 電電太郎
 */
@Component
public class FlightMasterProvider {

	/**
	 * 便名とフライト基本情報の関係を保持するキャッシュ。
	 */
	private final Map<String, FlightMaster> flightMasterMap = new HashMap<>();

	/**
	 * フライト基本情報リポジトリ。
	 */
	private final FlightMapper flightMapper;

	public FlightMasterProvider(FlightMapper flightMapper,
			FlywayMigrationInitializer flywayMigrationInitializer) {
		this.flightMapper = flightMapper;
	}

	/**
	 * 指定便名に該当するフライト基本情報を取得する。
	 *
	 * @param flightName 便名
	 * @return フライト基本情報。該当するフライト基本情報がない場合はnull。
	 */
	public FlightMaster getFlightMaster(String flightName) {
		Assert.hasText(flightName);
		return this.flightMasterMap.get(flightName);
	}

	/**
	 * フライト基本情報をロードし、キャッシュする。
	 */
	@PostConstruct
	public void load() {
		List<FlightMaster> flightMasterList = flightMapper.findAllFlightMaster();
		for (FlightMaster flightMaster : flightMasterList) {
			flightMasterMap.put(flightMaster.getFlightName(), flightMaster);
		}
	}
}
