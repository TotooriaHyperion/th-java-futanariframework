package com.futanari.model;

import java.math.BigDecimal;

/**
 * Created by Totooria Hyperion on 2017/5/5.
 */
public class Car {
	private String type;
	private String factory;
	private BigDecimal price = BigDecimal.valueOf(0d);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "{type:"+type+",factory:"+factory+",price:"+price+"}";
	}
}
