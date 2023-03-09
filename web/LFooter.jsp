<%-- 
    Document   : LFooter
    Created on : Oct 17, 2016, 10:40:10 AM
    Author     : THANH
--%>

<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="footer1123">
    <script>
        $(document).ready(function () {
            if('<%=isChangeContact%>' === "1") {
                footer_address = footer_address_minvoice;
                footer_office = "";
                footer_name = footer_name_minvoice;
                footer_hotline = footer_hotline_minvoice;
                footer_email = footer_email_minvoice;
            }
        });
    </script>
    <div class="text-center" id="idFooterName"></div>
    <div class="text-center"><script>document.write(footer_address);</script></div>
    <div class="text-center"><script>document.write(footer_office);</script></div>
    <div class="text-center">
        Hotline: <script>document.write(footer_hotline);</script> | Email: <a style="color: #ffffff;cursor: pointer;"><script>document.write(footer_email);</script></a>
    </div>
<!--    <div class="text-center">
        Hotline: <script>document.write(footer_hotline);</script>
    </div>-->
    <script>
        $(document).ready(function () {
            var d = new Date();
            var n = d.getFullYear();
            document.getElementById("idFooterName").innerHTML = footer_name.replace(JS_STR_CHOISE_ANOTHER_DATE_YEAR, n);
        });
    </script>
    <%
        Config confFooter = new Config();
        String sIsCAFooter = confFooter.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    %>
    <div class="text-center" style="display: <%=sIsCAFooter.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
        <script>document.write(global_version_web);</script>
    </div>
</div>
