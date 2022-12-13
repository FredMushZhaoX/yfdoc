package com.yf.document.ability.shiro.aop;


import com.alibaba.fastjson.JSON;
import com.yf.boot.base.api.api.ApiError;
import com.yf.boot.base.api.api.ApiRest;
import com.yf.document.ability.Constant;
import com.yf.document.ability.shiro.jwt.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权登录拦截器
 * @author bool
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 跨域请求
	 */
	private static final String CROSS_OPTIONS = "OPTIONS";

	/**
	 * 执行登录认证
	 * @param servletRequest
	 * @param servletResponse
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		//这几句代码是关键
		if (CROSS_OPTIONS.equals(request.getMethod())){
			response.setStatus(HttpStatus.SC_NO_CONTENT);
			log.info("++++++++++放行options请求");
			return true;
		}

		return this.executeLogin(servletRequest, servletResponse);
	}


	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(Constant.TOKEN);

		JwtToken jwtToken = new JwtToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		try {
			getSubject(request, response).login(jwtToken);
			return true;
		}catch (Exception e){
			// 捕获异常并返回false即可，下一步给onAccessDenied去处理
			return false;
		}
	}

	/**
	 * 执行授权错误时的方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(200);

		// 写入错误信息
		ApiRest apiRest = new ApiRest(ApiError.ERROR_10010002);
		httpServletResponse.getWriter().print(JSON.toJSONString(apiRest));
		return false;
	}


}
