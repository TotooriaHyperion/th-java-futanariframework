package com.futanari;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by user on 2017/4/17.
 */
public class TestBigDecimal {

	private static FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");

	public static void main(String[] args) {

		BigDecimal a = BigDecimal.valueOf(2005);
		BigDecimal a1 = a.divide(BigDecimal.valueOf(1000),BigDecimal.ROUND_FLOOR);
		System.out.println(a1);
		BigDecimal a2 = a.divide(BigDecimal.valueOf(1000),BigDecimal.ROUND_CEILING);
		System.out.println(a2);
		BigDecimal a3 = a.divide(BigDecimal.valueOf(1000),BigDecimal.ROUND_UP);
		System.out.println(a3);

		Double b = 2500D;
		Double b1 = 1500D;
		Double b2 = 1500D;
		System.out.println(b.compareTo(b1));
		System.out.println(b1.compareTo(b));
		System.out.println(b1.compareTo(b2));

		HashMap<String,Object> testDate = new HashMap<>();
		testDate.put("aa",new Date());
		System.out.println(testDate);
//		String testDateStr = JSONObject.toJSONString(testDate, SerializerFeature.UseISO8601DateFormat);
//		System.out.println(testDateStr);
//		TestModel model = new Gson().fromJson(testDateStr,TestModel.class);
//		System.out.println(model.getAa());
		testDate.put("aa",fdf.format((Date)testDate.get("aa")));
		System.out.println(testDate);
	}
}
