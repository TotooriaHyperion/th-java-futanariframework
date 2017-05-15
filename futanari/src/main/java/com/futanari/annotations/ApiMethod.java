package com.futanari.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {
	/**
	 * aip 版本号
	 * 
	 * @return string
	 */
	String version() default "1";

	/**
	 * api 接口代码
	 * 
	 * @return string
	 */
	String value();

	/**
	 * 接口描述
	 * 
	 * @return string
	 */
	String descript();

	/**
	 * 接口参数列表
	 * 
	 * @return ApiParam[]
	 */
	ApiParam[] apiParams() default {};

	/**
	 * 是否需要登录访问
	 * 
	 * @return boolean
	 */
	boolean needLogin() default false;

	boolean privateApi() default false;

	boolean backendApi() default false;

	boolean webApi() default false;
}
