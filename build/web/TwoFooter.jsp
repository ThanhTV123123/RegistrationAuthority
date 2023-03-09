<%-- 
    Document   : TwoFooter
    Created on : Nov 23, 2020, 10:39:55 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container" style="padding-left: 0;">
    <div class="footer1123">
        <div class="col-sm-8" style="margin-left: 0;padding-left: 0;">
            <div class="text-center" style="text-align: left;" id="idFooterName"></div>
            <div class="text-center" style="text-align: left;"><script>document.write(footer_address);</script></div>
            <div class="text-center" style="text-align: left;">
                Hotline: <script>document.write(footer_hotline);</script> | Email: <a style="color: #ffffff;cursor: pointer;"><script>document.write(footer_email);</script></a>
            </div>
            <script>
                $(document).ready(function () {
                    var d = new Date();
                    var n = d.getFullYear();
                    document.getElementById("idFooterName").innerHTML = footer_name.replace(JS_STR_CHOISE_ANOTHER_DATE_YEAR, n);
                });
            </script>
        </div>
        <div class="col-sm-4" style="border-left: 1px dashed #56C2E1;padding-left: 30px;">
            <ul style="margin-bottom: 2px;margin-left: 0;">
                <li><a href="Policy.jsp" target="_blank" style="color: #ffffff;"><script>document.write(TitlePolicyLink);</script></a></li>
                <li><a href="Terms.jsp" target="_blank" style="color: #ffffff;"><script>document.write(TitleTermsLink);</script></a></li>
            </ul>
            <!--<div style="margin-left: 0;" id="idLblVersion"></div>-->
            <span id="idLblVersion"></span>
            <script>document.getElementById("idLblVersion").innerHTML = global_version_web;</script>
        </div>
    </div>
</div>