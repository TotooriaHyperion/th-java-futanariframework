package com.futanari.learn;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by TotooriaHyperion on 2017/5/9.
 */
public class TestApacheBeanUtils {

	private static final Logger logger = LoggerFactory.getLogger(TestApacheBeanUtils.class);

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

		User1 u1 = new User1();
		u1.setName("Xiaoyin");
		u1.setAge(15);
		User2 u2 = new User2();
		BeanUtils.copyProperties(u2,u1);
		logger.info(u1.toString());
		logger.info(u2.toString());

		HashMap<String,Object> hashMap = new HashMap<>();
		BeanUtils.copyProperties(hashMap,u2);
		logger.info(u2.toString());
		logger.info(hashMap.toString());

		hashMap.put("name","Qiongmei");
		hashMap.put("age",14);
		BeanUtils.copyProperties(u2,hashMap);
		logger.info(hashMap.toString());
		logger.info(u2.toString());

//		hashMap.put("name","Linghua");
//		hashMap.put("age",13);
//		BeanUtils.populate(u2,hashMap);
//		logger.info(hashMap.toString());
//		logger.info(u2.toString());

	}

}

class User1 {
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
		return "User1{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}

class User2 {
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
		return "User2{" +
				"name='" + name + '\'' +
				", age=" + age +
				", gender='" + gender + '\'' +
				'}';
	}
}