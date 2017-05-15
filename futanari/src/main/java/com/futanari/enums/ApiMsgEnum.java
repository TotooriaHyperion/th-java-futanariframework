package com.futanari.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ApiMsgEnum {
	// 这一部分是系统级错误代码（从0000开始到0999）//
	/*
	 * 消息定义规范：
	 * user:01000--01999
	 */

	/**
	 * 服务器内部出现异常
	 */
	INTERNAL_ERROR(Boolean.FALSE, "9999", "服务器内部出现异常:{0}"),

	/**
	 * 操作成功
	 */
	SUCCESS(Boolean.TRUE, "0000", "成功"),
	/**
	 * 操作失败
	 */
	FAIL(Boolean.FALSE, "0001", "失败"),

	/**
	 * 签名错误
	 */
	TOKEN_ERROR(Boolean.FALSE, "0002", "签名错误"),
	/**
	 * 调用错误
	 */
	INVOKE_ERROR(Boolean.FALSE, "0003", "调用错误"),
	/**
	 * 未知错误
	 */
	UNKNOW_ERROR(Boolean.FALSE, "0004", "未知错误"),
	/**
	 * 只支持post请求
	 */
	POST_ONLY(Boolean.FALSE, "0005", "只支持post请求"),
	/**
	 * 缺少参数
	 */
	MissParameterException(Boolean.FALSE, "0006", "缺少参数"),
	/**
	 * 数据格式错误
	 */
	DataFormatException(Boolean.FALSE, "0007", "数据格式错误"),
	/**
	 * 请求被禁止/提交数据有误
	 */
	ForbiddenException(Boolean.FALSE, "0008", "提交数据有误"),

	/**
	 * 请求错误
	 */
	BadRequestException(Boolean.FALSE, "0009", "请求错误"),

	// 这一部分是用户体系错误代码（从1000开始到1999）////////////////
	/**
	 * 用户名不能为空
	 */
	UserNameIsNullException(Boolean.FALSE, "1000", "用户名不能为空"),
	/**
	 * 密码不能为空
	 */
	UserPasswordIsNullException(Boolean.FALSE, "1001", "密码不能为空"),
	/**
	 * 登录失败次数过多
	 */
	ExcessiveAttemptsException(Boolean.FALSE, "1002", "登录失败次数过多"),
	/**
	 * 用户名或密码错误
	 */
	UserNameOrPasswordException(Boolean.FALSE, "1003", "用户名或密码错误"),
	/**
	 * 用户未激活
	 */
	UserInactiveException(Boolean.FALSE, "1004", "账号未激活"),
	/**
	 * 用户被锁定
	 */
	UserLockedException(Boolean.FALSE, "1005", "账号被锁定"),
	/**
	 * 用户名已经被注册
	 */
	UserNameExistedException(Boolean.FALSE, "1007", "用户名已被使用"),
	/**
	 * 邮箱已经被注册
	 */
	EmailExistedException(Boolean.FALSE, "1008", "邮箱已被使用"),
	/**
	 * 手机号码已经被注册
	 */
	PhoneExistedException(Boolean.FALSE, "1009", "手机号码已被使用"),
	/**
	 * 用户未登录
	 */
	UserUnloginException(Boolean.FALSE, "1010", "用户未登录"),
	/**
	 * 当前密码错误
	 */
	UserPasswordException(Boolean.FALSE, "1011", "当前密码错误"),
	/**
	 * 用户类型错误
	 */
	UserTypeException(Boolean.FALSE, "1012", "用户类型错误"),
	/**
	 * 新密码与确认密码不一致
	 */
	PasswordMismatchException(Boolean.FALSE, "1013", "新密码与确认密码不一致"),
	/**
	 * 验证码错误
	 */
	ValidateCodeNull(Boolean.FALSE, "1105", "验证码不能为空"),
	/**
	 * 验证码错误
	 */
	ValidateCodeException(Boolean.FALSE, "1014", "验证码错误"),
	/**
	 * 登录密码至少6位最多16位
	 */
	UserPasswordInvlidException(Boolean.FALSE, "1015", "登录密码至少6位最多16位"),
	/**
	 * 当前手机号不正确
	 */
	UserCurrentPhoneException(Boolean.FALSE, "1016", "当前手机号不正确"),
	/**
	 * 获取接口调用凭证错误
	 */
	GetAccessTokenException(Boolean.FALSE, "1017", "获取接口调用凭证错误"),
	/**
	 * 获取用户信息错误
	 */
	GetUserInfoException(Boolean.FALSE, "1018", "获取用户信息错误"),
	/**
	 * 手机号码不存在
	 */
	PhoneDosentExistsException(Boolean.FALSE, "1019", "手机号码不存在"),
	/**
	 * 邮箱格式不对
	 */
	EmailFormatException(Boolean.FALSE, "1020", "邮箱格式错误"),
	/**
	 * QQ格式不对
	 */
	QQFormatException(Boolean.FALSE, "1021", "QQ格式错误"),
	/**
	 * 当前邮箱不正确
	 */
	UserCurrentEmailException(Boolean.FALSE, "1022", "当前邮箱不正确"),
	/**
	 * 用户不存在
	 */
	UserNotExistedException(Boolean.FALSE, "1023", "用户不存在"),
	/**
	 * 邮箱不存在
	 */
	EmailDosentExistsException(Boolean.FALSE, "1024", "邮箱不存在"),
	/**
	 * 验证码发送失败
	 */
	ValidateCodeSendException(Boolean.FALSE, "1025", "验证码发送失败"),
	/**
	 * 验证码已失效
	 */
	ValidateCodeInvalid(Boolean.FALSE, "1026", "验证码已失效"),

	;
	
	public Boolean isSuccess;
	public String code;
	public String msg;

	ApiMsgEnum(Boolean isSuccess, String code, String msg) {
		this.isSuccess = isSuccess;
		this.code = code;
		this.msg = msg;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static Map<String, String> getAll() {
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		ApiMsgEnum[] enumArr = ApiMsgEnum.values();
		for (ApiMsgEnum aEnum : enumArr) {
			retMap.put(aEnum.getCode(), aEnum.getMsg());
		}
		return retMap;
	}

	public static ApiMsgEnum getByCode(String code) {
		ApiMsgEnum[] enumArr = ApiMsgEnum.values();
		for (ApiMsgEnum aEnum : enumArr) {
			if (aEnum.getCode().equals(code)) {
				return aEnum;
			}
		}
		return null;
	}
}
