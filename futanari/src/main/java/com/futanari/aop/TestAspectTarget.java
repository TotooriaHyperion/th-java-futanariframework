package com.futanari.aop;

import org.springframework.stereotype.Component;

/**
 * Created by TotooriaHyperion on 2017/5/8.
 */
@Component
public class TestAspectTarget {

	public Integer add(Integer a, Integer b) {
		if (null == a || null == b) {
			return null;
		}
		return a + b;
	}

	public Integer mul(Integer a, Integer b) {
		if (null == a || null == b) {
			return null;
		}
		return a * b;
	}

	public Integer div(Integer a, Integer b) {
		if (null == a || null == b) {
			return null;
		}
		return a / b;
	}

}
