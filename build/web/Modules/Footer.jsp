<%-- 
    Document   : Footer
    Created on : Oct 12, 2013, 1:16:15 PM
    Author     : Thanh
--%>

<%--<%@page import="vn.ra.utility.EscapeUtils"%>--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<footer style="background:#EDEDED;">
    <%
        String sName = EscapeUtils.CheckTextNull(session.getAttribute("sessNameBranch").toString());
        String sLogo123 = EscapeUtils.CheckTextNull(session.getAttribute("sessLogoBranch").toString());
        if(!"".equals(sName) && !"".equals(sLogo123))
        {
    %>
    <div class="pull-right">
        <span id="idFooterNameInner"></span> <script>document.write(global_version_web);</script>
    </div>
    <script>
        $(document).ready(function () {
            var d = new Date();
            var n = d.getFullYear();
            document.getElementById("idFooterNameInner").innerHTML = footer_name_inner.replace(JS_STR_CHOISE_ANOTHER_DATE_YEAR, n) + '<%= sName %>';
        });
    </script>
    <div class="clearfix"></div>
    <%
        } else {
    %>
    <div class="pull-right">
        <span id="idFooterNameInner"></span> <script>document.write(global_version_web);</script>
    </div>
    <script>
        $(document).ready(function () {
            var d = new Date();
            var n = d.getFullYear();
            document.getElementById("idFooterNameInner").innerHTML = footer_name.replace(JS_STR_CHOISE_ANOTHER_DATE_YEAR, n);
        });
    </script>
    <div class="clearfix"></div>
    <%
        }
    %>
</footer>