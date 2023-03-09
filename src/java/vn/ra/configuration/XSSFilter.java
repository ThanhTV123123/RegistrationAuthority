/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author THANH-PC
 */
public class XSSFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(XSSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        log.info("X-XSS-Protection XSSFilter");
        //HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpSession session = httpServletRequest.getSession(false);
//        HttpServletResponse resp = (HttpServletResponse) response;
//        if (session == null || session.getAttribute("sUserID") == null) {
//            log.info("NO SESSION X-XSS-Protection XSSFilter");
//        } else {
//            log.info("SESSION X-XSS-Protection XSSFilter");
//        }
        
        XSSRequestWrapper wrapper = new XSSRequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrapper, response);
//        if("1".equals(wrapper.toString())){
////            String path = ((HttpServletRequest) request).getServletPath();
//            log.info(httpServletRequest.getContextPath()+"/Login.jsp");
//            request.getRequestDispatcher(httpServletRequest.getContextPath()+"/Login.jsp").forward(request, response);
//            //resp.sendRedirect(httpServletRequest.getContextPath()+"/Login.jsp");
//        }
         
        //PrintWriter responseWriter = response.getWriter();
        
        
        //chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }
}
