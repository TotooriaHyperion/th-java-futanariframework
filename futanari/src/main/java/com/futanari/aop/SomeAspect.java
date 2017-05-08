package com.futanari.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
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

	@After("execution(public Integer com.futanari.aop.TestAspectTarget.add(Integer,Integer))")
	public void aspectAfter(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAfter:\t\t" + className + "." + methodName);
	}

	@AfterReturning(
			pointcut = "execution(public Integer com.futanari.aop.TestAspectTarget.*(..))",
			returning = "result"
	)
	public void aspectAfterReturning(JoinPoint joinPoint, Object result) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAfterReturning:\t\t" + className + "." + methodName);
		logger.info("result = " + result);
	}

	@AfterThrowing(
			pointcut = "execution(public Integer com.futanari.aop.TestAspectTarget.*(..))",
			throwing = "exception"
	)
	public void aspectAfterException(JoinPoint joinPoint, Exception exception) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAfterException:\t\t" + className + "." + methodName);
		logger.info("exception = " + exception.getMessage());
	}

	@AfterThrowing(
			pointcut = "execution(public Integer com.futanari.aop.TestAspectTarget.*(..))",
			throwing = "exception"
	)
	public void aspectAfterArithmeticException(JoinPoint joinPoint, ArithmeticException exception) {
		Object[] args = joinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = joinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAfterArithmeticException:\t\t" + className + "." + methodName);
		logger.info("exception = " + exception.getMessage(), exception);
	}

	@Around("execution(public Integer com.futanari.aop.TestAspectTarget.*(..))")
	public Object aspectAround(ProceedingJoinPoint proceedingJoinPoint) {
		Object[] args = proceedingJoinPoint.getArgs();
		logger.info(Arrays.toString(args));
		Signature signature = proceedingJoinPoint.getSignature();
		String className = signature.getDeclaringTypeName();
		String methodName = signature.getName();
		logger.info(SomeAspect.class + ".aspectAroundBeforeProcess:\t\t" + className + "." + methodName);
		Object ret;
		try {
			ret = proceedingJoinPoint.proceed(args);
		} catch (Throwable e) {
			logger.info(SomeAspect.class + ".aspectAroundThrowable:\t\t" + className + "." + methodName);
			logger.info("throwable = " + e.getMessage(), e);
			ret = null;
		}
		logger.info(SomeAspect.class + ".aspectAroundAfterProcess:\t\t" + className + "." + methodName);
		return ret;
	}

}
