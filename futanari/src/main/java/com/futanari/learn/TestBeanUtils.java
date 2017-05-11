package com.futanari.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by TotooriaHyperion on 2017/5/9.
 */
public class TestBeanUtils {

	private static final Logger logger = LoggerFactory.getLogger(TestBeanUtils.class);

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

		User1a u1 = new User1a();
		u1.setName("Xiaoyin");
		u1.setAge(15);
		User2a u2 = new User2a();
		BeanUtils.copyProperties(u1,u2);
		logger.info(u1.toString());
		logger.info(u2.toString());

		HashMap<String,Object> hashMap = new HashMap<>();
		BeanUtils.copyProperties(u2,hashMap);
		logger.info(u2.toString());
		logger.info(hashMap.toString());

		hashMap.put("name","Qiongmei");
		hashMap.put("age",14);
		BeanUtils.copyProperties(hashMap,u2);
		logger.info(hashMap.toString());
		logger.info(u2.toString());

	}

}

class User1a {
	private String name;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User1a{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}

class User2a {
	private String name;
	private Integer age;
	private String gender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User2a{" +
				"name='" + name + '\'' +
				", age=" + age +
				", gender='" + gender + '\'' +
				'}';
	}
}