package com.futanari.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Totooria Hyperion on 2017/5/3.
 */
public class TestLog4j {

	private static final Logger logger = LoggerFactory.getLogger(TestLog4j.class);

	public static void main(String[] args) {
		logger.info("hello");
	}

}
