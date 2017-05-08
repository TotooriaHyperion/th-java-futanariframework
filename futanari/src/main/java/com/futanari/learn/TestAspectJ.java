package com.futanari.learn;

import com.futanari.aop.TestAspectTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by TotooriaHyperion on 2017/5/8.
 */
public class TestAspectJ {

	private static final Logger logger = LoggerFactory.getLogger(TestAspectJ.class);

	public static void main(String[] args) {

		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		TestAspectTarget testAspectTarget = (TestAspectTarget) ac.getBean("testAspectTarget");
		logger.info(testAspectTarget.toString());
		try {
			Integer c = testAspectTarget.add(1, 2);
			logger.info(String.valueOf(c));
			c = testAspectTarget.mul(2, 4);
			logger.info(String.valueOf(c));
			c = testAspectTarget.div(2, 0);
			logger.info(String.valueOf(c));
		} catch (Exception e) {
//			logger.info(TestAspectJ.class + ".main:\t\t" + e.getMessage(), e);
		}

	}

}
