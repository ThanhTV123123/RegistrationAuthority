/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.restful;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
public class CORSFilter { //implements Filter {

//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletResponse resp = (HttpServletResponse) response;
//        resp.addHeader("Access-Control-Allow-Origin", "*");
//        resp.addHeader("Access-Control-Allow-Headers", "*");
//        resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
//        resp.addHeader("Access-Control-Max-Age", "1209500");
//
//        chain.doFilter(request, resp);
//
//    }
//
//    @Override
//    public void init(FilterConfig arg0) throws ServletException {
//
//    }

//    @Override
//    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres)
//            throws IOException {
//        cres.getHeaders().add("Access-Control-Allow-Origin", "*"); // Update specific domains instead of giving to all
//        cres.getHeaders().add("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,Authorization,content-type");
//        cres.getHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
//        cres.getHeaders().add("Access-Control-Max-Age", "1209500");
//    }
}
