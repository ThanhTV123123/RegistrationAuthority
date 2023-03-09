<%-- 
    Document   : MenuLeft
    Created on : Feb 26, 2014, 3:29:42 PM
    Author     : Thanh
--%>

<%@page import="vn.ra.utility.LoadParamSystem"%>
<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String sCheckSCA = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
%>
<script>
    if(localStorage.getItem("HasLoginLog") === "" || localStorage.getItem("HasLoginLog") === null
        || localStorage.getItem("HasLoginLog") === "null")
    {
        window.location = "../Login.jsp";
    }
</script>
<%    
    CommonFunction.LogDebugString(null, "SESSION-ACCESS-PAGE", "TIME: " + CommonFunction.GetDateFromLong(session.getLastAccessedTime())
        + "; USER: " + session.getAttribute("sUserID").toString().trim()
        + "; SESSION-KEY: " + session.getAttribute("sesSessKey").toString().trim()
        + "; Page: " + request.getRequestURI());
    try {
        String strSessGlobalTimeOut = session.getAttribute("SessGlobalTimeOut").toString().trim();
        if (!"".equals(strSessGlobalTimeOut)) {
            request.getSession().setMaxInactiveInterval(Integer.parseInt(strSessGlobalTimeOut));
        }
        String strSessAgentID = session.getAttribute("SessAgentID").toString().trim();
        String strRole = session.getAttribute("RoleID").toString().trim();
        String strLeftUsername = session.getAttribute("sUserID").toString().trim();
        String strENVN = session.getAttribute("sessVN").toString().trim();
        String strLeftSessionKey = session.getAttribute("sesSessKey").toString().trim();
        int sOut = db.CheckIsLoginOnline(strLeftUsername, strLeftSessionKey);
        if (sOut == 1) {
            String sNameProject = request.getContextPath();
            String strURL = request.getRequestURI();
            String strURLCurrent = strURL.replace(sNameProject + "/", "../");
            if(strURLCurrent.contains("Certificate/RegisterCertificate.jsp")) {
                if(sCheckSCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                    strURLCurrent = "../Certificate/RegisterList.jsp";
                }
            }
            int isURL = db.CheckIsURLRelated(strURLCurrent, strRole);
            if (isURL == 1) {
                Config configMenuLeft = new Config();
                String sCheckBackEnd = configMenuLeft.GetPropertybyCode(Definitions.CONFIG_IS_FRONTEND_BACKEND);
%>
<div class="menu_section">
    <br /><br />
    <%
        MENULINK[][] rsLeft = new MENULINK[1][];
        db.S_BO_URI_PARENT_LIST(strRole, strSessAgentID, rsLeft, strENVN);
        if (rsLeft.length > 0) {
            for (MENULINK tempParent : rsLeft[0]) {
                String sClass = com.ClassCss(tempParent.ID);
                String sLinkParentVN = tempParent.REMARK;
                if(sCheckSCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                    if(sCheckBackEnd.equals("0")) {
                        if(tempParent.ID == Definitions.CONFIG_AGENCY_MENU_ID_LIST_ALLOW_GENERALCONFIG) {
                            if("1".equals(strENVN)) {
                                sLinkParentVN = "CẤU HÌNH ĐẠI LÝ";
                            } else {sLinkParentVN = "AGENT CONFIGURATION";}
                        }
                    }
                }
    %>
    <ul class="nav side-menu">
        <li>
            <a>
                <i class="<%= sClass%>"></i> <%= sLinkParentVN%> <span class="fa fa-chevron-down"></span>
            </a>
            <ul class="nav child_menu">
                <%
                    MENULINK[][] rsChilrent = new MENULINK[1][];
                    db.S_BO_URI_CHILDRENT_LIST_INID(String.valueOf(tempParent.ID), strRole, strSessAgentID, rsChilrent, strENVN);
                    if (rsChilrent[0].length > 0) {
                        for (MENULINK tempChilrent : rsChilrent[0]) {
                            String strHref = tempChilrent.LINKURL;
                            String sLinkChilrenVN = tempChilrent.REMARK;
                %>
                <li><a href="<%= strHref%>"><%= sLinkChilrenVN%></a></li>
                <%
                        }
                    }
                %>
            </ul>
        </li>
    </ul>
    <%
            }
        }
    %>
</div>
<%
} else {
%>
<!--<script type="text/javascript">
    window.onload = function () {
        document.body.innerHTML = "";
        RedirectPageLoginNoSess(global_alert_another_login);
    }();
</script>-->
<%
    }
} else {
%>
<script type="text/javascript">
    window.onload = function () {
        document.body.innerHTML = "";
        RedirectPageLoginNoSess(global_alert_another_login);
    }();
</script>
<%
    }
    }
    catch (Exception e) {
        checkExFinnaly = 1;
        CommonFunction.LogExceptionJSP(request.getRequestURI(), e.getMessage(), e);
    }
    finally {
    if (checkExFinnaly == 1) {
        checkExFinnaly = 0;
%>
<script type="text/javascript">
    window.onload = function () {
        document.body.innerHTML = "";
        RedirectPageError();
    }();
</script>
<%
        }
    }
%>