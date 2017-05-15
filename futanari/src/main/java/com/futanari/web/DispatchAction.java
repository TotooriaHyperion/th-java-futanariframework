package com.futanari.web;

import com.futanari.annotations.ApiMethod;
import com.futanari.annotations.ApiParam;
import com.futanari.constants.Global;
import com.futanari.dto.ApiFinalResponse;
import com.futanari.dto.ApiKeyDto;
import com.futanari.dto.ApiRequest;
import com.futanari.dto.ApiResponse;
import com.futanari.enums.ApiMsgEnum;
import com.futanari.util.JSONUtils;
import com.futanari.util.SimpleTokenUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */
public abstract class DispatchAction {

	private final Log loger = LogFactory.getLog(DispatchAction.class);

	@Value("${api.version}")
	private String apiVersion;

	private static final Map<String, ApiKeyDto> apiKeyMap = new HashMap<String, ApiKeyDto>();
	private static final Map<Long, ApiRequest> currentReqeust = new HashMap<Long, ApiRequest>();
	private static final String defaultApiKey = "futanari_web";

	// 参数完整性校验
	static {
		apiKeyMap.put("futanari_web", new ApiKeyDto("futanari_web", "f2099bdf37e6464c8064a262041497f9", "futanari_web"));
		apiKeyMap.put("futanari_backend", new ApiKeyDto("futanari_backend", "d5b6014824384aa8a0d96a60b0ca7052", "futanari_backend"));
		apiKeyMap.put("user", new ApiKeyDto("user", "a9dcd8995661434994244ce8adfbadcb", "user"));

		apiKeyMap.put("android_client", new ApiKeyDto("android_client", "d5b6014824384aa8a0d96a60b0ca7052", "android_client"));
		apiKeyMap.put("ios_client", new ApiKeyDto("ios_client", "1b4e76ce0346432799a6ce7cbb2765a4", "ios_client"));
	}

	/**
	 * 请求控制分发
	 *
	 * @param apiReq ApiRequest
	 * @return String
	 */
	protected final String doDispatch(ApiRequest apiReq) {
		String retJson = null;
		String functionCode = null;
		try {
			ApiResponse apiRsp = this.checkParam(apiReq);
			if (apiRsp != null) {
				return this.buildApiFinalResponse(apiReq, apiRsp);
			}
			functionCode = apiReq.getFunctionCode();
			// String apiVersion=apiReq.getApiVersion();
			// 此处可以把调用接口的IP,时间记录下来
			this.beforeInvokeService(apiReq);
			Object bean = SpringBeanProxy.getBeanByFunctionCode(functionCode);
			Method method = SpringBeanProxy.getMethodByFunctionCode(functionCode);
			try {
				Object rspObj = method.invoke(bean, new Object[] { apiReq });
				if (rspObj instanceof String) {
					retJson = (String) rspObj;
					this.afterInvokeService(apiReq, retJson);
				}
			} catch (Exception e) {
				retJson = this.buildApiFinalResponse(apiReq, new ApiResponse(ApiMsgEnum.INVOKE_ERROR));
				loger.error("logId:" + SuperDispatcherServlet.getLogIdByThreadId(Thread.currentThread().getId()) + ",method.invoke exception in " + functionCode, e);
			}
		} catch (Exception e) {
			loger.error("logId:" + SuperDispatcherServlet.getLogIdByThreadId(Thread.currentThread().getId()) + ",apiDispatche exception for " + functionCode, e);
		}
		return retJson;
	}

	@SuppressWarnings("unchecked")
	protected String buildApiFinalResponse(ApiRequest apiReq, ApiResponse apiRsp) {
		// 统一输出响应JSON
		ApiFinalResponse finalRsp = new ApiFinalResponse();
		finalRsp.setVersion(apiVersion);
		finalRsp.setFunctionCode(apiReq.getFunctionCode());
		finalRsp.setIsSuccess(apiRsp.getReturnEnum().getIsSuccess());
		finalRsp.setCode(apiRsp.getReturnEnum().getCode());
		finalRsp.setMsg(apiRsp.getReturnEnum().getMsg());
		finalRsp.setCount(apiRsp.getCount());
		finalRsp.setResults(apiRsp.getResults());
		return JSONUtils.objectToJson(finalRsp);
	}

	// 参数检查
	protected ApiResponse checkParam(ApiRequest apiReq) {
		String apiKey = apiReq.getApiKey();
		if (StringUtils.isEmpty(apiKey)) {
			loger.info("checkParam apiKey is null");
			return new ApiResponse(ApiMsgEnum.ForbiddenException);
		}
		String functionCode = apiReq.getFunctionCode();
		if (StringUtils.isEmpty(functionCode)) {
			loger.info("checkParam functionCode is null");
			return new ApiResponse(ApiMsgEnum.ForbiddenException);
		}
		ApiMethod apiMethod = SpringBeanProxy.getApiMethodByFunctionCode(functionCode);
		if (apiMethod == null) {
			loger.info("checkParam not found apiMethod in ApiMethod ");
			return new ApiResponse(ApiMsgEnum.ForbiddenException);
		}
		String inputToken = apiReq.getAccessToken();
		if (StringUtils.isEmpty(inputToken)) {
			loger.info("checkParam inputToken is null ");
			return new ApiResponse(ApiMsgEnum.ForbiddenException);
		}
		if (!Global.DEFAULT_ACCESS_TOKEN.equals(inputToken)) {
			ApiKeyDto keyDto = apiKeyMap.get(apiReq.getApiKey());
			if (keyDto == null) {
				loger.info("checkParam not found apiKey in ApiKeyDto ");
				return new ApiResponse(ApiMsgEnum.ForbiddenException);
			}
			String rightToken = SimpleTokenUtil.buildToken(apiReq, functionCode, keyDto.getApiSecret());
			if (!inputToken.equals(rightToken)) {
				return new ApiResponse(ApiMsgEnum.TOKEN_ERROR);
			}
		}
		if (apiMethod.needLogin()) {
			if (StringUtils.isEmpty(apiReq.get(Global.AUTH_TOKEN))) {
				return new ApiResponse(ApiMsgEnum.UserUnloginException);
			}
		}
		ApiParam[] apiParams = apiMethod.apiParams();
		for (ApiParam parm : apiParams) {
			if (!parm.isNull()) {
				if (StringUtils.isEmpty(apiReq.get(parm.name()))) {
					loger.error("dispatch parmName="+parm.name()+" is null");
					return new ApiResponse(ApiMsgEnum.MissParameterException);
				}
			}
		}
		return null;
	}

	protected List<ApiRequest> buildApiRequest(String data, HttpServletRequest req) {
		List<ApiRequest> apiReqList = new ArrayList<>();
		JsonElement dataJsonEle = JSONUtils.jsonToObject(data, JsonElement.class);
		if (dataJsonEle != null && dataJsonEle.isJsonObject()) {
			JsonObject functionCodeJsonObj = dataJsonEle.getAsJsonObject();
			for (Map.Entry<String, JsonElement> functionCodeEntry : functionCodeJsonObj.entrySet()) {
				ApiRequest apiReq = new ApiRequest();
				apiReq.setApiVersion(this.apiVersion);
				String functionCode = functionCodeEntry.getKey();
				apiReq.setFunctionCode(functionCode);

				JsonElement paramJsonEle = functionCodeEntry.getValue();
				if (paramJsonEle != null && paramJsonEle.isJsonObject()) {
					JsonObject paramJsonObj = paramJsonEle.getAsJsonObject();
					for (Map.Entry<String, JsonElement> paramEntry : paramJsonObj.entrySet()) {
						String paramName = paramEntry.getKey();
						JsonElement paramValueEle = paramEntry.getValue();
						String paramValue = null;
						if (paramValueEle != null && !paramValueEle.isJsonNull() && paramValueEle.isJsonPrimitive()) {
							paramValue = paramValueEle.getAsString();
						}
						if (Global.API_KEY.equals(paramName)) {
							apiReq.setApiKey(paramValue);
						} else if (Global.ACCESS_TOKEN.equals(paramName)) {
							apiReq.setAccessToken(paramValue);
						} else if (Global.REQUEST_IP.equals(paramName)) {
							apiReq.setRequestIp(paramValue);
						} else if (Global.PAGE.equals(paramName) && !StringUtils.isEmpty(paramValue)) {
							apiReq.put(paramName, Integer.valueOf(paramValue));
						} else if (Global.PAGE_SIZE.equals(paramName) && !StringUtils.isEmpty(paramValue)) {
							apiReq.put(paramName, Integer.valueOf(paramValue));
						} else {
							if (paramValueEle == null || paramValueEle.isJsonNull() || paramValueEle.isJsonPrimitive()) {
								apiReq.put(paramName, paramValue);
							} else if (paramValueEle.isJsonObject()) {
								apiReq.put(paramName, paramValueEle.getAsJsonObject());
							} else if (paramValueEle.isJsonArray()) {
								apiReq.put(paramName, paramValueEle.getAsJsonArray());
							}
						}
					}
				}

				if (StringUtils.isEmpty(apiReq.getApiKey())) {
					apiReq.setApiKey(defaultApiKey);
				} else {
					ApiKeyDto keyDto = apiKeyMap.get(apiReq.getApiKey());
					if (keyDto != null) {
						apiReq.setApiSource(keyDto.getApiSource());
						apiReq.put(Global.API_KEY, apiReq.getApiKey());
					}
				}
				if (StringUtils.isEmpty(apiReq.getRequestIp())) {
					apiReq.setRequestIp(WebHelper.getRequestIp(req));
				}
				if (StringUtils.isEmpty(apiReq.getUserAgent())) {
					String agent = req.getHeader("User-Agent");
					apiReq.setUserAgent(agent);
				}
				apiReqList.add(apiReq);
			}
		}
		return apiReqList;
	}
	
	protected String buildJsonItem(String itmeName, String json) {
		StringBuffer buf = new StringBuffer();
		buf.append("\"" + itmeName + "\"").append(":").append(json);
		return buf.toString();
	}

	protected void beforeInvokeService(ApiRequest apiReq) {
		currentReqeust.put(Thread.currentThread().getId(), apiReq);
	}

	protected void afterInvokeService(ApiRequest apiReq, String resp) {
		currentReqeust.remove(Thread.currentThread().getId());
	}

	public static ApiRequest getCurrentReqeust() {
		return currentReqeust.get(Thread.currentThread().getId());
	}

}
