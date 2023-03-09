<%-- 
    Document   : ConnectionParam
    Created on : May 22, 2017, 1:34:57 PM
    Author     : THANH-PC
--%>
<%@page import="vn.ra.process.ConnectDbPhaseTwo"%>
<%@page import="vn.ra.process.DESEncryption"%>
<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.object.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*,java.io.*" %>
<%
    int checkExFinnaly = 0;
    CommonFunction com = new CommonFunction();
    ConnectDatabase db = new ConnectDatabase();
    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
    DESEncryption seEncript = new DESEncryption();
    response.setHeader("X-Frame-Options", "SAMEORIGIN");
//    String SessRoleSetFuns = "";
//    if ((session.getAttribute("sUserID")) != null && (session.getAttribute("SessRoleSet")) != null) {
//        SessRoleSetFuns = session.getAttribute("SessRoleSet").toString().trim();
//    }
// CHECK ERROR 10 mín auto logout //
//    if(session.getAttribute("sUserID") != null) {
//        if (CommonFunction.CheckURLAccessRootAdmin(request.getRequestURI().trim(),
//            session.getAttribute("RoleID").toString().trim(), session.getAttribute("SessAgentID").toString().trim()) == false) {
//            response.sendRedirect("../Modules/Logout.jsp");
%>
<!--<script>
    window.location = "../Modules/Logout.jsp";
</script>-->
<%
//        }
//    }
//    else {
//        response.sendRedirect("../Modules/Logout.jsp");
%>
<!--<script>
    window.location = "../Modules/Logout.jsp";
</script>-->
<%
    //}
%>