/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author THANH-PC
 */
@WebFilter(filterName = "XXSSProtectionFilter", urlPatterns = {"/TwoFooter.jsp", "/User/UserRoleEdit.jsp"})
public class XXSSProtectionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(XXSSProtectionFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        log.info("X-XSS-Protection header added to response");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"); 
        response.setHeader("X-Content-Type-Options", "nosniff"); 
        response.setHeader("Cache-control", "no-store, no-cache"); 
        response.setHeader("X-Frame-Options", "DENY"); 
        response.setHeader("Set-Cookie", "XSRF-TOKEN=NDKDdfdsfkldsfNd3SZAJfwLsTl5WUgOkE; Path=/; Secure;HttpOnly");
        filterChain.doFilter(servletRequest, response);
//        response.setHeader("X-XSS-Protection", "1; mode=block");
//
//        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
