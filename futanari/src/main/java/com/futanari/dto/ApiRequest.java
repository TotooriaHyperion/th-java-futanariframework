package com.futanari.dto;

//import com.oxygen.enums.ContentTypeEnum;
import com.futanari.util.DateTimeUtil;
import com.futanari.util.MapUtil;
import com.futanari.util.SimpleStringUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * http 接口请求对象
 * 
 */
public class ApiRequest extends HashMap<String, Object> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3518461079812863846L;

	private String apiVersion;

	private String apiKey;

	private String accessToken;

	private String functionCode;

	private String requestIp;

	private Date requestTime;

	private boolean oldVersion;

	private boolean justNeedResult;

	private String apiSource;

	private String userAgent;

	private String deviceNo;

	private String modelName;

//	private ContentTypeEnum contentTypeEnum;

	public ApiRequest() {

	}

	public ApiRequest(ApiRequest oldApiReq) {
		this.apiVersion = oldApiReq.getApiVersion();
		this.apiKey = oldApiReq.getApiKey();
		this.accessToken = oldApiReq.getAccessToken();
		this.functionCode = oldApiReq.getFunctionCode();
		this.requestIp = oldApiReq.getRequestIp();
		this.requestTime = oldApiReq.getRequestTime();
		this.oldVersion = oldApiReq.isOldVersion();
		this.justNeedResult = oldApiReq.isJustNeedResult();
		this.apiSource = oldApiReq.getApiSource();
//		this.contentTypeEnum = oldApiReq.getContentTypeEnum();
		this.userAgent = oldApiReq.getUserAgent();
		this.deviceNo = oldApiReq.getDeviceNo();
		this.modelName = oldApiReq.getModelName();
	}
	
	public Boolean getBoolean(String key) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return Boolean.valueOf(value.toString());
		}
		return null;
	}

	public Integer getInt(String key) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return Integer.valueOf(value.toString());
		}
		return null;
	}

	public Long getLong(String key) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return Long.valueOf(value.toString());
		}
		return null;
	}

	public Date getDate(String key, String pattern) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return DateTimeUtil.parseDateTime(value.toString(), pattern);
		}
		return null;
	}

	public Double getDouble(String key) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return Double.valueOf(value.toString());
		}
		return null;
	}

	public BigDecimal getBigDecimal(String key) {
		Object value = this.get(key);
		if (value != null && !"".equals(value)) {
			return BigDecimal.valueOf(Double.valueOf(value.toString()));
		}
		return null;
	}

	public String getString(String key) {
		Object value = this.get(key);
		if (!StringUtils.isEmpty(value)) {
			return value.toString();
		}
		return null;
	}

	/**
	 * 通过(,)分割数组
	 * 
	 * @param key
	 * @return
	 */
	public String[] getStringArray(String key) {
		Object value = this.get(key);
		if (!StringUtils.isEmpty(value)) {
			String temp = value.toString();
			return temp.split(",");
		}
		return null;
	}

	public List<String> getStringList(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			List<String> list = new ArrayList<String>(temp.length);
			for (int i = 0; i < temp.length; i++) {
				list.add(temp[i]);
			}
			return list;
		}
		return null;
	}

	/**
	 * 通过(,)分割数组
	 * 
	 * @param key
	 * @return
	 */
	public Integer[] getIntArray(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			Integer[] array = new Integer[temp.length];
			for (int i = 0; i < temp.length; i++) {
				array[i] = Integer.valueOf(temp[i]);
			}
			return array;
		}
		return null;
	}

	public List<Integer> getIntList(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			List<Integer> list = new ArrayList<Integer>(temp.length);
			for (int i = 0; i < temp.length; i++) {
				list.add(Integer.valueOf(temp[i]));
			}
			return list;
		}
		return null;
	}

	public List<Long> getLongList(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			List<Long> list = new ArrayList<Long>(temp.length);
			for (int i = 0; i < temp.length; i++) {
				list.add(Long.valueOf(temp[i]));
			}
			return list;
		}
		return null;
	}

	public Set<Integer> getIntSet(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			Set<Integer> set = new HashSet<Integer>(temp.length);
			for (int i = 0; i < temp.length; i++) {
				set.add(Integer.valueOf(temp[i]));
			}
			return set;
		}
		return null;
	}

	public Set<Long> getLongSet(String key) {
		String temp[] = this.getStringArray(key);
		if (temp != null && temp.length > 0) {
			Set<Long> set = new HashSet<Long>(temp.length);
			for (int i = 0; i < temp.length; i++) {
				set.add(Long.valueOf(temp[i]));
			}
			return set;
		}
		return null;
	}

	public String getSafeHtml(String key) {
		Object value = this.get(key);
		if (!StringUtils.isEmpty(value)) {
			return SimpleStringUtil.replaceHtml(value.toString());
		}
		return null;
	}
	
    public  boolean isNumeric(String key){  
    	Object value = this.get(key);
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
        return pattern.matcher(value.toString()).matches();     
    }  

	/**
	 * 将map的值拷贝到对象，对象为普通bean map key 对应bean的属性
	 * 
	 * @param classz
	 * @return
	 */
	public Object getBean(Class<?> classz) {
		return MapUtil.mapToObject(this, classz);
	}

//	public ContentTypeEnum getContentTypeEnum() {
//		return contentTypeEnum;
//	}
//
//	public void setContentTypeEnum(ContentTypeEnum contentTypeEnum) {
//		this.contentTypeEnum = contentTypeEnum;
//	}

	public String getApiSource() {
		return apiSource;
	}

	public void setApiSource(String apiSource) {
		this.apiSource = apiSource;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public boolean isJustNeedResult() {
		return justNeedResult;
	}

	public void setJustNeedResult(boolean justNeedResult) {
		this.justNeedResult = justNeedResult;
	}

	public boolean isOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(boolean oldVersion) {
		this.oldVersion = oldVersion;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
