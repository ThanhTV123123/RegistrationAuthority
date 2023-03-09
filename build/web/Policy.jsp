<%-- 
    Document   : Policy
    Created on : Nov 23, 2020, 9:07:12 AM
    Author     : USER
--%>

<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.object.GENERAL_POLICY"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.awt.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<%
    response.setHeader("X-Frame-Options", "SAMEORIGIN");
    Config conf = new Config();
    String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    String isChangeContact = "0";
    if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
        if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("MINVOICE.")){
//        if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("LOCALHOST")){
            isChangeContact = "1";
        }
    }
%>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="js/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="js/jquery.js"></script>
        <script>
            $(document).ready(function () {
//                localStorage.setItem("HasLoginLog", null);
                if (localStorage.getItem("VN") !== null && localStorage.getItem("VN") !== "null") {
                    
                }
                else
                {
                    localStorage.setItem("VN", "1");
                }
                var sLangugeCurrent = localStorage.getItem("VN");
                localStorage.clear();
                localStorage.setItem("VN", sLangugeCurrent);
            });
        </script>
        <script src="js/Language.js"></script>
        <script src="js/process_javajs.js"></script>
        <link rel="stylesheet" href="js/cssLogin_1.css">
        <script src="js/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="js/sweetalert.css"/>
        <script type="text/javascript" src="Css/GlobalAlert.js"></script>
        <script src="style/jquery.min.js"></script>
        <script>
            changeFavicon("");
            $(document).ready(function () {
                document.title = TitlePolicyPage;
                var image = document.getElementById('idLogoPageHeader');
                image.src = LinkLogoPage;
                if('<%=isChangeContact%>' === "1") {
                    image.src = "Images/Logo_Minvoice_210.png";
                }
            });
        </script>
    </head>
    <body>
        <%
            String sessionid = request.getSession().getId();
            String contextPath = request.getContextPath();
            response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Path=" + contextPath + "; HttpOnly;");
        %>
        <div id="header-two">
            <div class="header-two-123">
                <div class="container">
                    <div class="col-md-5">
                        <div class="col-sm-6" style="padding: 0px;">
                            <a href="Login.jsp"><img id="idLogoPageHeader" style="max-width: 210px;" class="img-responsive" /></a>
                        </div>
                    </div>
                    <div class="col-md-7" style="text-align: right;">
                        <div class="form-group" style="padding: 0px;">
                            <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                            <p style="color: #000000;" id="idDivLanguage"><img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="Images/en_flag.png" /> | <img onclick="loginEN('0');" id="idLoginBannerLanguageRight" style="width: 18px;height: 18px;cursor: pointer;" title="Vietnamese" src="Images/vn_flag.png" /></p>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                </div>
            </div>
            <script>
                $(document).ready(function () {
                    if(IsWhichCA === "15" || IsWhichCA === "17") {
                        $("#idLoginBannerLanguageRight").attr("src", "Images/Laos_flag.png");
                        //document.getElementById("idDivLanguage").style.display = "none";
                        //localStorage.setItem("VN", "0");
                        document.getElementById("idLoginBannerLanguageRight").setAttribute("title", "Laos");
                    }
                });
                var image = document.getElementById('idLogoPageHeader');
                image.src = LinkLogoPage;
                if('<%=isChangeContact%>' === "1") {
                    image.src = "Images/Logo_Minvoice_210.png";
                }
                function loginEN(id)
                {
                    if (id === "1")
                    {
                        localStorage.setItem("VN", "0");
                    }
                    else
                    {
                        localStorage.setItem("VN", "1");
                    }
                    location.reload();
                }
            </script>
        </div>
        <div class="container">
            <%
                ConnectDatabase db = new ConnectDatabase();
                GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                String sValuePolicy = "";
                db.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
                if (rsPolicy[0].length > 0) {
                    for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_WEBSITE_PRIVACY_POLICY)) {
                            sValuePolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            break;
                        }
                    }
                }
            %>
            <div style="border: 1px solid #56C2E1; padding: 10px;">
                <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                    <span id="idTitlePolicy"></span>
                    <script>
                        document.getElementById("idTitlePolicy").innerHTML = TitlePolicyPage;
                    </script>
                </div>
                <div id="idPolicyFooterOut">
                <%=sValuePolicy%>
                </div>
                <div style="clear: both;"></div>
            </div>
            <a href="http://metop.info" style="display: none;">metop.info close</a>
        </div>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
<!--         style="position: static;padding-bottom: 15px;clear: both;"-->
       <div id="footer1" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background: #f40000;" : ""%>">
            <%@ include file="TwoFooter.jsp" %>
        </div>
        <script> 
            $(document).ready(function() {
                if($("#idPolicyFooterOut").height() > 500)
                {
                    $("#footer1").css({"position":"static", "padding-bottom":"15px", "clear":"both"});
                } else {
                    $("#footer1").css({"position":"absolute", "padding-bottom":"15px"});
                }
            }); 
        </script>
    </body>
</html>