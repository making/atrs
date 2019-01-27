package com.example.atrs.auth;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthLoginMapper {
	/**
	 * 会員番号に該当するカード会員情報(ログイン時に必要な情報のみ)を取得する。
	 *
	 * @param membershipNumber 会員番号
	 * @return カード会員情報(ログイン時に必要な情報のみ)
	 */
	AuthLogin findOne(String membershipNumber);

	/**
	 * 会員ログイン情報を登録する。
	 *
	 * @param authLogin 会員ログイン情報を含む会員情報
	 * @return 登録件数
	 */
	int insert(AuthLogin authLogin);

	/**
	 * 会員ログイン情報を更新する。
	 *
	 * @param authLogin 会員ログイン情報を含む会員情報
	 * @return 更新件数
	 */
	int update(AuthLogin authLogin);

	/**
	 * ログイン時に会員ログイン情報を更新する。
	 *
	 * @param authLogin カード会員情報
	 * @return 更新件数
	 */
	int updateLoginStatus(AuthLogin authLogin);

	/**
	 * ログアウト時に会員ログイン情報を更新する。
	 *
	 * @param authLogin カード会員情報
	 * @return 更新件数
	 */
	int updateLogoutStatus(AuthLogin authLogin);
}
