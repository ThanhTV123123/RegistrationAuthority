<%-- 
    Document   : Terms
    Created on : Nov 23, 2020, 9:14:05 AM
    Author     : USER
--%>

<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.object.GENERAL_POLICY"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
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
                document.title = TitleTermsPage;
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
                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_WEBSITE_TERMS_CONDITIONS)) {
                            sValuePolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            break;
                        }
                    }
                }
            %>
            <div style="border: 1px solid #56C2E1; padding: 10px;">
                <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                    <span id="idTitleTerms"></span>
                    <script>
                        document.getElementById("idTitleTerms").innerHTML = TitleTermsPage;
                    </script>
                </div>
                <div id="idTermFooterOut">
                <%=sValuePolicy%>
                </div>
<!--                <p><strong>Terms &amp; Conditions</strong></p>
<p>By downloading or using the app, these terms will automatically apply to you - you should make sure therefore that you read them carefully before using the app. You&rsquo;re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You&rsquo;re not allowed to reserve engineer LCA to extract the source code of the app, and you also shouldn&rsquo;t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Miniistry of Post and Telecommunications.</p>
<p>LCA is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you&rsquo;re paying for.</p>
<p>The LCA app stores and processes personal data that you have provided to us, in order to provide our Service. It&rsquo;s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone&rsquo;s security features and it could mean that LCA app won&rsquo;t work properly or at all.</p>
<p>The app does use third party services that declare their own Terms and Conditions.</p>
<p>You should be aware that there are certain things that LCA will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Internet connection, or provided by your mobile network provider, but LCA cannot take responsibility for the app not working at full functionality if you don&rsquo;t have access to Internet connection, and you don&rsquo;t have any of your data allowance left.</p>
<p>If you&rsquo;re using the app outside of an area with Internet connection, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you&rsquo;re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you&rsquo;re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.</p>
<p>Along the same lines, LCA cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged &ndash; if it runs out of battery and you can&rsquo;t turn it on to avail the Service, LCA cannot accept responsibility.</p>
<p>With respect to LCA&rsquo;s responsibility for your use of the app, when you&rsquo;re using the app, it&rsquo;s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. LCA accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.</p>
<p>At some point, we may wish to update the app. The app is currently available on Android &ndash; the requirements for system (and for any additional systems we decide to extend the availability of the app to) may change, and you&rsquo;ll need to download the updates if you want to keep using the app. LCA does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.</p>
<p><strong>Changes to This Terms and Conditions</strong></p>
<p>We may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Terms and Conditions on this page. These changes are effective immediately after they are posted on this page.</p>
<p><strong>Contact Us</strong></p>
<p>If you have any questions or suggestions about our Terms and Conditions, do not hesitate to contact us at develop@mpt.gov.la.</p>-->
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
                if($("#idTermFooterOut").height() > 500)
                {
                    $("#footer1").css({"position":"static", "padding-bottom":"15px", "clear":"both"});
                } else {
                    $("#footer1").css({"position":"absolute", "padding-bottom":"15px"});
                }
            }); 
        </script>
    </body>
</html>