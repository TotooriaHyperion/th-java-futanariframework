package com.futanari.learn;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by TotooriaHyperion on 2017/5/5.
 */
// 对IOC容器中所有bean都处理。
public class TestBeansPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		/*
		* beanName：id
		* bean：实例
		* */
		System.out.println("postProcessBeforeInitialization：" + beanName);
		/*
		* 返回处理之后的bean。必须返回，否则永远拿不到对应bean。
		* */
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization：" + beanName);
		return bean;
	}
}
