package com.futanari.web;

import com.futanari.dto.ApiRequest;
import com.futanari.dto.ApiResponse;
import com.futanari.enums.ApiMsgEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by TotooriaHyperion on 2017/5/15.
 */
@Controller
public class ApiDispatchAction extends DispatchAction {

	//Restful
	@RequestMapping(value = { "", "/rest"})
	public String restApiDispatch(@RequestBody String bodyData, HttpServletRequest req, HttpServletResponse resp) {
		return null;
	}

	// JSON-rpc
	@RequestMapping(value = { "", "/" })
	public String apiDispatch(@RequestBody String bodyData, HttpServletRequest req, HttpServletResponse rsp) {
		String data = req.getParameter("data");
		if (StringUtils.isEmpty(data)) {
			data = bodyData;
		}
		List<ApiRequest> apiReqList = this.buildApiRequest(data, req);
		String json = makeMoreResponse(apiReqList);
		return WebHelper.outputJson(json, rsp);
	}

	private String makeMoreResponse(List<ApiRequest> apiReqList) {
		StringBuffer buf = new StringBuffer("{");
		if (apiReqList != null && apiReqList.size() > 0) {
			for (int i = 0; i < apiReqList.size(); i++) {
				ApiRequest apiReq = apiReqList.get(i);
				String apiRsp = this.doDispatch(apiReq);
				if (apiReq.isOldVersion()) {
					if (apiReqList.size() == 1) {
						return apiRsp;
					}
					buf.append(apiRsp);
				} else {
					String item = this.buildJsonItem(apiReq.getFunctionCode(), apiRsp);
					buf.append(item);
				}
				if (i != apiReqList.size() - 1) {
					buf.append(",");
				}
			}
		} else {
			String item = this.buildJsonItem("unknown", this.buildApiFinalResponse(new ApiRequest(), new ApiResponse<Object>(ApiMsgEnum.ForbiddenException)));
			buf.append(item);
		}
		buf.append("}");
		return buf.toString();
	}
}
