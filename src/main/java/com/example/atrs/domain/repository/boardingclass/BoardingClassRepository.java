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
package com.example.atrs.domain.repository.boardingclass;

import org.apache.ibatis.annotations.Mapper;

import com.example.atrs.domain.model.BoardingClass;

import java.util.List;

/**
 * 搭乗クラステーブルにアクセスするリポジトリインターフェース。
 * 
 * @author NTT 電電太郎
 */
@Mapper
public interface BoardingClassRepository {
    /**
     * 全ての搭乗クラスを取得する。
     * 
     * @return 搭乗クラスリスト
     */
    List<BoardingClass> findAll();
}