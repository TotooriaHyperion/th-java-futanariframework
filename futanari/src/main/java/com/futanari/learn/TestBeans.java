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

		Person a = (Person) context.getBean("person1");

		System.out.println(a);

	}

}
