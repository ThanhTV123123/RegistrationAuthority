/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.AsyncContext;
/**
 *
 * @author THANH-PC
 */
@WebServlet(urlPatterns = {"/PushNotiRequestDecline"}, asyncSupported = true)
public class PushNotiRequestDecline extends HttpServlet {
    private final List<AsyncContext> contexts = new LinkedList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(10 * 60 * 1000);
        contexts.add(asyncContext);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
        this.contexts.clear();
        int iCount = 1;
        String sBRANCH_ID = request.getParameter("name");
        String sUSER_ID = request.getParameter("user");
        String sNameSess = "sessPushNotiRequestDecline-" + sBRANCH_ID + "-" + sUSER_ID;
        ServletContext sc = request.getSession().getServletContext();
        if (sc.getAttribute(sNameSess) == null) {
            sc.setAttribute(sNameSess, String.valueOf(iCount));
        } else {
            int currentMessages = Integer.parseInt((String) sc.getAttribute(sNameSess));
            sc.setAttribute(sNameSess, String.valueOf(iCount + currentMessages));
        }
//        if (sc.getAttribute("sessPushNotiRequestDecline") == null) {
//            sc.setAttribute("sessPushNotiRequestDecline", String.valueOf(iCount));
//        } else {
//            int currentMessages = Integer.parseInt((String) sc.getAttribute("sessPushNotiRequestDecline"));
//            sc.setAttribute("sessPushNotiRequestDecline", String.valueOf(iCount + currentMessages));
//        }
        for (AsyncContext asyncContext : asyncContexts) {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(String.valueOf(iCount)+"#"+sBRANCH_ID+"#"+sUSER_ID);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ex) {
            }
        }
    }
}
