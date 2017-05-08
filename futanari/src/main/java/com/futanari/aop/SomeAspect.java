package com.futanari.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by TotooriaHyperion on 2017/5/8.
 */
@Aspect
@Component
public class SomeAspect {

	private static final Logger logger = LoggerFactory.getLogger(SomeAspect.class);

	@Before("execution(public Integer com.futanari.aop.TestAspectTarget.*(..))")
	public void aspectBefore(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectBefore:\t\t" + className + "." + methodName);
	}

	@Before("execution(public Integer com.futanari.aop.TestAspectTarget.add(Integer,Integer))")
	@After("")
	public void aspectAfter(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAfter:\t\t" + className + "." + methodName);
	}

}
