package com.futanari.dto;

import java.io.Serializable;

public class ApiKeyDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8342508427349003639L;

	private String apiKey;
	private String apiSecret;
	private String apiSource;

	public ApiKeyDto(String apiKey, String apiSecret, String apiSource) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.apiSource = apiSource;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getApiSource() {
		return apiSource;
	}

	public void setApiSource(String apiSource) {
		this.apiSource = apiSource;
	}

}
