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

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 運賃種別情報を提供するクラス。
 * 
 * @author NTT 電電太郎
 */
@Component
public class FareTypeProvider {

	/**
	 * 運賃種別コードと運賃種別情報の関係を保持するマップ。
	 */
	private final Map<FareTypeCd, FareType> fareTypeMap = new HashMap<>();

	/**
	 * 運賃種別情報リポジトリ。
	 */
	private final FareTypeMapper fareTypeMapper;

	public FareTypeProvider(FareTypeMapper fareTypeMapper) {
		this.fareTypeMapper = fareTypeMapper;
	}

	/**
	 * 指定運賃種別コードに該当する運賃種別情報を取得。
	 *
	 * @param fareTypeCd 運賃種別コード
	 * @return 運賃種別情報。該当する運賃種別情報がない場合null。
	 */
	public FareType getFareType(FareTypeCd fareTypeCd) {
		Assert.notNull(fareTypeCd);
		return fareTypeMap.get(fareTypeCd);
	}

	/**
	 * 運賃種別情報をロードし、キャッシュする。
	 */
	@PostConstruct
	public void load() {
		List<FareType> fareTypeList = fareTypeMapper.findAll();
		for (FareType fareType : fareTypeList) {
			fareTypeMap.put(fareType.getFareTypeCd(), fareType);
		}
	}

}
