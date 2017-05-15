package com.futanari.web;

import com.futanari.annotations.ApiMethod;
import com.futanari.annotations.ApiParam;
import com.futanari.annotations.ApiService;
import com.futanari.dto.ApiParamDto;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */
public class SpringBeanProxy {

	private static ApplicationContext applicationContext;

	private static Map<String, Object[]> API_METHOD_MAP = new HashMap<String, Object[]>();

	/**
	 * api类目
	 */
	private static Map<String, String> functionCodeCatalogMap = new LinkedHashMap<String, String>();

	/**
	 * api列表
	 */
	private static Map<String, Map<String, String>> functionCodeListMap = new LinkedHashMap<String, Map<String, String>>();

	/**
	 * api参数
	 */
	private static Map<String, Map<String, ApiParamDto>> functionCodeParamMap = new LinkedHashMap<String, Map<String, ApiParamDto>>();

	public synchronized static void setApplicationContext(ApplicationContext arg0) {

		applicationContext = arg0;

		// 获取所有服务
		Map<String, Object> tempMap = applicationContext.getBeansWithAnnotation(ApiService.class);

		if (tempMap != null && tempMap.size() > 0) {
			for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
				// 获取bean，判断是否拥有ApiService的annotation
				getApiService(entry);
			}
		}
	}

	private static void getApiService(Map.Entry<String, Object> entry) {
		String beanName = entry.getKey();
		Object bean = entry.getValue();
		ApiService beanFc = bean.getClass().getAnnotation(ApiService.class);
		if (beanFc != null) {
			functionCodeCatalogMap.put(beanName, beanFc.descript());
			// 获取ApiService下的ApiMethod
			getApiMethodByService(beanName, bean);
		}
	}

	private static void getApiMethodByService(String beanName, Object bean) {
		Method[] methodArr = bean.getClass().getDeclaredMethods();
		if (methodArr != null && methodArr.length > 0) {
			Map<String, String> methodFunctionCodeMap = new LinkedHashMap<String, String>();
			for (Method method : methodArr) {
				ApiMethod methodFc = method.getAnnotation(ApiMethod.class);
				if (methodFc != null) {
					String methodFunctionCode = methodFc.value();
					API_METHOD_MAP.put(methodFunctionCode, new Object[] { beanName, method, methodFc });
					Map<String, ApiParamDto> paramMap = new LinkedHashMap<String, ApiParamDto>();
					ApiParam[] params = methodFc.apiParams();
					for (ApiParam param : params) {
						ApiParamDto apiParamDto = new ApiParamDto();
						apiParamDto.setDescript(param.descript());
						apiParamDto.setName(param.name());
						apiParamDto.setNull(param.isNull());
						paramMap.put(param.name(), apiParamDto);
					}
//					printMethodEnum(methodFc, methodFunctionCode);

					functionCodeParamMap.put(methodFunctionCode, paramMap);

					methodFunctionCodeMap.put(methodFunctionCode, methodFc.descript());
				}
			}
			functionCodeListMap.put(beanName, methodFunctionCodeMap);
		}
	}

	private static void printMethodEnum(ApiMethod methodFc, String methodFunctionCode) {
		StringBuilder str = new StringBuilder();
		str.append("/**\n");
		str.append(" * ").append(methodFc.descript()).append("\n");
		str.append(" */\n");
		str.append(methodFunctionCode.toUpperCase().replaceAll("\\.", "_") + "(\"" + methodFunctionCode + "\", \"" + methodFc.descript() + "\", ApiServerEnum.xxx,ContentTypeEnum.yyy,"
				+ methodFc.privateApi() + "," + methodFc.backendApi() + "," + methodFc.webApi() + ") {");
		str.append("	@Override");
		str.append("	public Map<String, String> getApiParams() {");
		str.append("		Map<String, String> paramMap = new LinkedHashMap<String, String>();");
		ApiParam[] params = methodFc.apiParams();
		for (ApiParam param : params) {
			str.append("		paramMap.put(\"" + param.name() + "\", \"" + param.descript() + "\");");
		}
		str.append("		return paramMap;");
		str.append("	}");
		str.append("},\n");
		System.out.println(str.toString());
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static Object getBeanByFunctionCode(String functionCode) {
		Object[] objArr = API_METHOD_MAP.get(functionCode);
		if (objArr != null && objArr.length >= 3) {
			String beanName = (String) objArr[0];
			return getBean(beanName);
		}
		return null;
	}

	public static Method getMethodByFunctionCode(String functionCode) {
		Object[] objArr = API_METHOD_MAP.get(functionCode);
		if (objArr != null && objArr.length >= 3) {
			return (Method) objArr[1];
		}
		return null;
	}

	public static ApiMethod getApiMethodByFunctionCode(String functionCode) {
		Object[] objArr = API_METHOD_MAP.get(functionCode);
		if (objArr != null && objArr.length >= 3) {
			return (ApiMethod) objArr[2];
		}
		return null;
	}

	public static Map<String, String> getFunctionListByCatalog(String functionCode) {
		return functionCodeListMap.get(functionCode);
	}

	public static Map<String, ApiParamDto> getParamsByFunctionCode(String functionCode) {
		return functionCodeParamMap.get(functionCode);
	}

	public static Map<String, String> getFunctionCodeCatalogMap() {
		return functionCodeCatalogMap;
	}

}
