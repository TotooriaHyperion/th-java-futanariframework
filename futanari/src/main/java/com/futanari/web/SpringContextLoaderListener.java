package com.futanari.web;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SpringContextLoaderListener extends ContextLoaderListener {
	private static final Logger log = Logger.getLogger(SpringContextLoaderListener.class);
	private static Map<String, String> appConfig = new HashMap<>();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		Enumeration<String> initParam = event.getServletContext().getInitParameterNames();
		if (initParam != null) {
			while (initParam.hasMoreElements()) {
				String key = initParam.nextElement();
				String value = event.getServletContext().getInitParameter(key);
				log.info("init app config key:" + key + ",value:" + value);
				appConfig.put(key, value);
			}
		}
		WebApplicationContext context = (WebApplicationContext) event.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		SpringBeanProxy.setApplicationContext(context);
	}

	/**
	 * 获取应用级别配置
	 *
	 * @param key key
	 * @return String
	 */
	public static String getAppConfig(String key) {
		return appConfig.get(key);
	}
}
