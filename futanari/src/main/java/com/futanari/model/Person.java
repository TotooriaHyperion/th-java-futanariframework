package com.futanari.model;

import java.util.List;

/**
 * Created by Totooria Hyperion on 2017/5/5.
 */
public class Person {
	private String name;
	private Integer age;
	private List<Car> cars;

	Person() {
		System.out.println("构造器");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("setName");
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		System.out.println("setAge");
		this.age = age;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		System.out.println("setCars");
		this.cars = cars;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("{name:").append(this.name)
				.append(",age:").append(String.valueOf(this.age))
				.append(",cars:").append(this.cars).append("}");
		return str.toString();
	}

	public void init() {
		System.out.println("init");
	}

	public void destroy() {
		System.out.println("destroy");
	}
}
