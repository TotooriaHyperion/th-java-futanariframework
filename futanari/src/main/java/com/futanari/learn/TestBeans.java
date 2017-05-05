package com.futanari.learn;

import com.futanari.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Totooria Hyperion on 2017/5/5.
 */
public class TestBeans {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("testBeans.xml");

		/*
		* IOC管理Bean的生命周期：
		* 1.通过构造器或是工厂方法创建对象。
		* 2.将需要的属性注入。
		* ----后置处理器前置处理：postProcessBeforeInitialization
		* 3.调用初始化方法。参数：init-method
		* ----后置处理器后置处理：postProcessAfterInitialization
		* 4.使用Bean。
		* 5.调用销毁方法。参数：destory-method
		* */

		Person a = (Person) context.getBean("person1");

		System.out.println(a);

	}

}
