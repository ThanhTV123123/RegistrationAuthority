/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.google.gson.Gson;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author THANH-PC
 */
public class XSSFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(XSSFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //resetCookies(request, (HttpServletResponse) response);
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")) {
//            MultipartHttpServletRequest multipartHttpServletRequest
//                    = new CommonsMultipartResolver().resolveMultipart(request);
//            XSSNormalRequestWrapper xssNormalRequestWrapper = new XSSNormalRequestWrapper(multipartHttpServletRequest);
//            logUrlAndParam(request, xssNormalRequestWrapper, null);
            //chain.doFilter(xssNormalRequestWrapper, response);
        } else if (StringUtils.isNotBlank(contentType) && contentType.contains("application/x-www-form-urlencoded")) {
            XSSNormalRequestWrapper xssNormalRequestWrapper = new XSSNormalRequestWrapper(request);
            logUrlAndParam(request, xssNormalRequestWrapper, null);
            chain.doFilter(xssNormalRequestWrapper, response);
        } else if (StringUtils.isNotBlank(contentType) && contentType.contains("application/json")) {
            XSSBodyRequestWrapper xssBodyRequestWrapper = new XSSBodyRequestWrapper(request);
            logUrlAndParam(request, null, xssBodyRequestWrapper);
            chain.doFilter(xssBodyRequestWrapper, response);
        } else {
            logUrlAndParam(request, null, null);
            chain.doFilter(request, response);
        }

    }

//    public Cookie[] resetCookies(HttpServletRequest request,
//            HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        Map<String, String> map = new HashMap<>();
//        if (cookies != null && cookies.length != 0) {
//            for (Cookie cookie : cookies) {
//                String value = cookie.getValue();
//                if (value != null) {
//                    String deleteValue = stripXSS(value);
//                    if (!value.equals(deleteValue)) {
//                        map.put(cookie.getName(), cookie.getDomain());
//                    }
//                }
//            }
//
//            if (CollectionUtils.isNotEmpty(map)) {
//                for (Map.Entry<String, String> cookie : map.entrySet()) {
//                    CookieUtil.removeCookie(request, response, cookie
//                            .getValue(), cookie.getKey());
//                }
//            }
//        }
//        return cookies;
//    }

    @Override
    public void destroy() {

    }

    private void logUrlAndParam(HttpServletRequest request, XSSNormalRequestWrapper xssNormalRequestWrapper, XSSBodyRequestWrapper xssBodyRequestWrapper) {
        // Get request URL
        String url = request.getRequestURI();
        // Get request method
        String method = request.getMethod();
        // Get path
        String path = request.getQueryString();
        // Get header
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {//Loop through the parameters in the Header and put the traversed parameters into the Map
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        String header = new Gson().toJson(map);
        // Get parameters
        String params = "";
        if (xssNormalRequestWrapper != null) {
            params = new Gson().toJson(xssNormalRequestWrapper.getParameterMap());
        }
        if (xssBodyRequestWrapper != null) {
            params = xssBodyRequestWrapper.getBody();
        }
        if (xssNormalRequestWrapper == null && xssBodyRequestWrapper == null) {
            params = new Gson().toJson(request.getParameterMap());
        }
        //logger.info("Request url:{},method:{},path:{},header:{},param:{}", url, method, path, header, params);
    }
}
