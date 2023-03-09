<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<%  
    String sPort = request.getServletPath();
    String sCriptGlobal = request.getRequestURL().toString().replace(sPort, "") + "/Css/GlobalAlert.js";
    String sCssLogin = request.getRequestURL().toString().replace(sPort, "") + "/js/cssLogin_1.css";
    String sStyleSheet11 = request.getRequestURL().toString().replace(sPort, "") + "/font-awesome/css/font-awesome.min.css";
    String sCriptBoostrap = request.getRequestURL().toString().replace(sPort, "") + "/js/bootstrap.min.js";
    String sCssInner11 = request.getRequestURL().toString().replace(sPort, "") + "/js/bootstrap.min.css";
    String sCriptJquery = request.getRequestURL().toString().replace(sPort, "") + "/js/jquery.js";
    String sCriptVN = request.getRequestURL().toString().replace(sPort, "") + "/js/Language.js";
    String sProcessJava = request.getRequestURL().toString().replace(sPort, "") + "/js/process_javajs.js";
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script type="text/javascript" src="<%= sCriptBoostrap%>"></script>
        <script type="text/javascript" src="<%= sCriptJquery%>"></script>
        <script type="text/javascript" src="<%= sCriptVN%>"></script>
        <script type="text/javascript" src="<%= sProcessJava%>"></script>
        <link rel="stylesheet" type="text/css" href="<%= sCssInner11%>"/>
        <link rel="stylesheet" type="text/css" href="<%= sStyleSheet11%>"/>
        <link rel="stylesheet" type="text/css" href="<%= sCssLogin%>"/>
        <script type="text/javascript" src="<%= sCriptGlobal%>"></script>
        <script>
            changeFavicon("");
            document.title = error_title_list;
        </script>
    </head>
    <body>
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
                if(!sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
            %>
            <div style="border: 1px solid #56C2E1; padding: 10px;">
                <div class="col-md-6">
                    <span style="font-size: 15px;">
                        <script>document.write(error_content_home);</script> <a href="Admin/Home.jsp" style="text-decoration: none; color: blue;"><script>document.write(error_content_link_out);</script></a>
                    </span>
                    <span class="failureNotification" style="float: left; text-align: left;">

                    </span>
                </div>
                <div class="col-md-1"></div>
                <div class="col-md-5" style="border-left: 1px dashed #56C2E1;">
                    <div style="text-align: center;padding-left: 10px;">
                        <div class="aOpen">
                            <img id="idBannerPageHeader" class="img-responsive" />
                        </div>
                        <script>
                            $(document).ready(function () {
                                var imageBanner = document.getElementById('idBannerPageHeader');
                                imageBanner.src = LinkBannerPage;
                            });
                        </script>
                    </div>
                </div>
                <div style="clear: both;"></div>
            </div>
            <%
                } else {
            %>
            <div>
                <div class="col-md-3"></div>
                <div class="col-md-6" style="border: 1px solid #56C2E1;">
                    <div style="padding: 10px;">
                        <span style="font-size: 15px;">
                            <script>document.write(error_content_home);</script> <a href="Admin/Home.jsp" style="text-decoration: none; color: blue;"><script>document.write(error_content_link_out);</script></a>
                        </span>
                        <span class="failureNotification" style="float: left; text-align: left;">

                        </span>
                    </div>
                </div>
                <div class="col-md-3"></div>
                <div style="clear: both;"></div>
            </div>
            <%
                }
            %>
        </div>
        <div id="footer1" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background: #f40000;" : ""%>">
            <%@ include file="LFooter.jsp" %>
        </div>
    </body>
</html>