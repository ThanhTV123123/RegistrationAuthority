<%-- 
    Document   : Login
    Created on : Apr 8, 2014, 2:43:10 PM
    Author     : Thanh
--%>

<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.Config"%>
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
                document.title = TitleLoginPage;
                var image = document.getElementById('idLogoPageHeader');
                image.src = LinkLogoPage;
                if('<%=isChangeContact%>' === "1") {
                    image.src = "Images/Logo_Minvoice_210.png";
                }
                $(".loading-gif").hide();
                $("#sUserName").focus(function () {
                    if ($("#sUserName").val() === global_fm_Username)
                    {
                        $("#sUserName").val('');
                    }
                });
                $("#sUserName").blur(function () {
                    if ($("#sUserName").val() === "") {
                        $("#sUserName").val(global_fm_Username);
                    }
                });

                $("#sPwd").focus(function () {
                    $('#sPwd').attr("placeholder", "");
                });
                $("#sPwd").blur(function () {
                    if ($("#sPwd").val() === "")
                    {
                        $('#sPwd').attr("placeholder", global_fm_Password);
                    }
                });
                $("#idCaptchaText").focus(function () {
                    if ($("#idCaptchaText").val() === login_fm_captcha)
                    {
                        $("#idCaptchaText").val('');
                    }
                });
                $("#idCaptchaText").blur(function () {
                    if ($("#idCaptchaText").val() === "")
                    {
                        $("#idCaptchaText").val(login_fm_captcha);
                    }
                });
            });

            $(document).ready(function () {
                $('#sPwd').keydown(function (event) {
                    if (event.keyCode === 13) {
                        var isCA = '<%=sIsCA%>';
                        if(isCA !== JS_IS_WHICH_ABOUT_CA_ICA){
                            loginPage(document.getElementById("CsrfToken").value);
                            return false;
                        } else {
                            loginPageNoCaptcha(document.getElementById("CsrfToken").value);
                            return false;
                        }
                    }
                });
                $('#idCaptchaText').keydown(function (event) {
                    if (event.keyCode === 13) {
                        var isCA = '<%=sIsCA%>';
                        if(isCA !== JS_IS_WHICH_ABOUT_CA_ICA){
                            loginPage(document.getElementById("CsrfToken").value);
                            return false;
                        } else {
                            loginPageNoCaptcha(document.getElementById("CsrfToken").value);
                            return false;
                        }
                    }
                });
                $('#sUserName').keydown(function (event) {
                    if (event.keyCode === 13) {
                        var isCA = '<%=sIsCA%>';
                        if(isCA !== JS_IS_WHICH_ABOUT_CA_ICA){
                            if ($('#sPwd').val() !== '' && $('#sUserName').val() !== '' && $('#idCaptchaText').val() !== '')
                            {
                                loginPage(document.getElementById("CsrfToken").value);
                                return false;
                            }
                            else
                            {
                                funErrorLoginAlert(global_req_all);
                                return false;
                            }
                        } else {
                            if ($('#sPwd').val() !== '' && $('#sUserName').val() !== '')
                            {
                                loginPageNoCaptcha(document.getElementById("CsrfToken").value);
                                return false;
                            }
                            else
                            {
                                funErrorLoginAlert(global_req_all);
                                return false;
                            }
                            
                        }
                        
                    }
                });
            });
            function fogetPage()
            {
                window.location = "Forgot.jsp";
            }
            function loginPage(idCSRF)
            {
                var name = document.loginform.sUserName;
                var pass = document.loginform.sPwd.value;
                var capchas = document.loginform.CaptchaText.value;
                if (!JSCheckEmptyField(name.value) || sSpace(name.value) === global_fm_Username)
                {
                    name.focus();
                    funErrorLoginAlert(policy_req_empty + global_fm_Username);
                    return false;
                }
                else if (!JSCheckEmptyField(pass))
                {
                    funErrorLoginAlert(policy_req_empty + global_fm_Password);
                    return false;
                }
                if (!JSCheckEmptyField(capchas))
                {
                    funErrorLoginAlert(policy_req_empty + login_fm_captcha);
                    return false;
                }
                else
                {
                    var s1 = document.getElementById("idCode").value;
                    if (trim(s1) !== trim(capchas))
                    {
                        LoadCaptcha();
                        funErrorLoginAlert(login_req_captcha);
                        return false;
                    }
                }
                //***if (name.value !== "" && pass !== "" && capchas !== login_fm_captcha && capchas !== "")
                if (name.value !== "" && pass !== "" && capchas !== login_fm_captcha && capchas !== "")
                {
                    if (name.value !== global_fm_Username)
                    {
                        var s1 = $("#idCode").val();
                        if (s1 !== capchas)
                        {
                            LoadCaptcha();
                            swal("", login_req_captcha, "error");
                            $("#idCaptchaText").focus();
                        }
                        else
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            var s = localStorage.getItem("VN");
                            $.ajax({
                                type: "POST",
                                url: "LoginCommon",
                                error: function (jqXHR, textStatus) {
                                    if (textStatus === 'timeout')
                                    {
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                        swal({
                                            title: "", text: login_error_timeout, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                        });
                                    }
                                    else {
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                        swal({
                                            title: "", text: login_error_exception, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                        });
                                    }
                                },
                                data: {
                                    idParam: 'loginpage',
                                    sUserName: name.value,
                                    sPwd: pass,
                                    sCaptcha: capchas,
                                    svn: s,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0") {
                                        localStorage.setItem("HasLoginLog", "1");
                                        localStorage.setItem("localStoreUserName_RoudRB", name.value);
                                        localStorage.setItem("localStoreSessKey_RoudRB", arr[2]);
                                        
                                        if (arr[1] === "1")
                                        {
                                            window.location = "Admin/LoginChange.jsp";
                                        }
                                        else
                                        {
                                            window.location = "Admin/Home.jsp";
                                        }
                                    }
                                    else if (arr[0] === JS_EX_CSRF) {
                                        swal({title: "", text: CSRF_Mess, imageUrl: "Images/icon_error.png", imageSize: "45x45"},
                                        function () {
                                            window.location = "Modules/Logout.jsp";
                                        });
                                    }
                                    else if (arr[0] === "CAPTCHA") {
                                        $("#idCaptchaText").focus();
                                        funErrorLoginAlert(login_req_captcha);
                                    }
                                    else if (arr[0] === JS_EX_ERROR) {
                                        funErrorLoginAlert(global_errorsql);
                                    }
                                    else if (arr[0] === "1") {
                                        funErrorLoginAlert(login_error_lock);
                                    }
                                    else if (arr[0] === "2") {
                                        funErrorLoginAlert(login_error_incorrec);
                                    }
                                    else if (arr[0] === "3") {
                                        funErrorLoginAlert(login_error_incorrec);
                                    }
                                    else if (arr[0] === "4") {
                                        funErrorLoginAlert(login_error_inactive);
                                    }
                                    else
                                    {
                                        funErrorLoginAlert(global_errorsql);
                                    }
                                    LoadCaptcha();
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                },
                                timeout: 100000
                            });
                            return false;
                        }
                    }
                    else
                    {
                        funErrorLoginAlert(global_req_all);
                    }
                }
                else
                {
                    funErrorLoginAlert(global_req_all);
                }
            }
            function loginPageNoCaptcha(idCSRF)
            {
                var name = document.loginform.sUserName;
                var pass = document.loginform.sPwd.value;
                if (!JSCheckEmptyField(name.value) || sSpace(name.value) === global_fm_Username)
                {
                    name.focus();
                    funErrorLoginAlert(policy_req_empty + global_fm_Username);
                    return false;
                } else {
                    if (!JSCheckEmptyField(pass))
                    {
                        funErrorLoginAlert(policy_req_empty + global_fm_Password);
                        return false;
                    }
                }
                if (name.value !== "" && pass !== "")
                {
                    if (name.value !== global_fm_Username)
                    {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        var s = localStorage.getItem("VN");
                        $.ajax({
                            type: "POST",
                            url: "LoginCommon",
                            error: function (jqXHR, textStatus) {
                                if (textStatus === 'timeout')
                                {
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                    swal({
                                        title: "", text: login_error_timeout, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                    });
                                }
                                else {
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                    swal({
                                        title: "", text: login_error_exception, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                    });
                                }
                            },
                            data: {
                                idParam: 'loginpage',
                                sUserName: name.value,
                                sPwd: pass,
                                sCaptcha: "",
                                svn: s,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0") {
                                    localStorage.setItem("HasLoginLog", "1");
                                    localStorage.setItem("localStoreUserName_RoudRB", name.value);
                                    localStorage.setItem("localStoreSessKey_RoudRB", arr[2]);
                                    if (arr[1] === "1")
                                    {
                                        window.location = "Admin/LoginChange.jsp";
                                    } else if (arr[1] === "2")
                                    {
                                        checkRedrectHome("../Certificate/RegisterList.jsp");
//                                        window.location = "Certificate/RegisterList.jsp";
                                    }
                                    else
                                    {
                                        window.location = "Admin/Home.jsp";
                                    }
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    swal({title: "", text: CSRF_Mess, imageUrl: "Images/icon_error.png", imageSize: "45x45"},
                                    function () {
                                        window.location = "Modules/Logout.jsp";
                                    });
                                }
                                else if (arr[0] === JS_EX_ERROR) {
                                    funErrorLoginAlert(global_errorsql);
                                }
                                else if (arr[0] === "1") {
                                    funErrorLoginAlert(login_error_lock);
                                }
                                else if (arr[0] === "2") {
                                    funErrorLoginAlert(login_error_incorrec);
                                }
                                else if (arr[0] === "3") {
                                    funErrorLoginAlert(login_error_incorrec);
                                }
                                else if (arr[0] === "4") {
                                    funErrorLoginAlert(login_error_inactive);
                                }
                                else
                                {
                                    funErrorLoginAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            },
                            timeout: 100000
                        });
                        return false;
                    }
                    else
                    {
                        funErrorLoginAlert(global_req_all);
                    }
                }
                else
                {
                    funErrorLoginAlert(global_req_all);
                }
            }
            function checkRedrectHome(pageCheck){
                $.ajax({
                    type: "post",
                    url: "SomeCommon",
                    data: {
                        idParam: 'checkredrecthome',
                        pageCheck: pageCheck
                    },
                    cache: false,
                    success: function (html)
                    {
                        var arr = sSpace(html).split('#');
                        if (arr[1] === "1") {
                            window.location = "Certificate/RegisterList.jsp";
                        }
                        else
                        {
                            window.location = "Admin/Home.jsp";
                        }
                    }
                });
            }
            function loginSSLPage(idCSRF)
            {
                var s = localStorage.getItem("VN");
                LoadCaptcha();
                $.ajax({
                    type: "GET",
                    url: "GetCertSSL",
                    cache: false,
                    success: function (htmlSSL) {
                       var vResult = sSpace(htmlSSL).split('#');
                       if(vResult[0] === "0")
                       {
                           $('body').append('<div id="over"></div>');
                           $(".loading-gif").show();
                           $.ajax({
                                type: "POST",
                                url: "LoginCommon",
                                error: function (jqXHR, textStatus) {
                                    if (textStatus === 'timeout')
                                    {
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                        swal({
                                            title: "", text: login_error_timeout, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                        });
                                    }
                                    else {
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                        swal({
                                            title: "", text: login_error_exception, imageUrl: "Images/icon_error.png", imageSize: "45x45"
                                        });
                                    }
                                },
                                data: {
                                    idParam: 'loginssl',
                                    svn: s,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html) {
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0") {
                                        localStorage.setItem("HasLoginLog", "1");
                                        window.location = "Admin/Home.jsp";
                                    }
                                    else if (arr[0] === JS_EX_CSRF) {
                                        swal({title: "", text: CSRF_Mess, imageUrl: "Images/icon_error.png", imageSize: "45x45"},
                                        function () {
                                            window.location = "Modules/Logout.jsp";
                                        });
                                    }
                                    else if (arr[0] === JS_EX_ERROR) {
                                        funErrorLoginAlert(global_errorsql);
                                    }
                                    else if (arr[0] === "1") {
                                        funErrorLoginAlert(login_error_lock);
                                    }
                                    else if (arr[0] === "2") {
                                        funErrorLoginAlert(login_error_incorrec);
                                    }
                                    else if (arr[0] === "3") {
                                        funErrorLoginAlert(login_error_incorrec);
                                    }
                                    else if (arr[0] === "4") {
                                        funErrorLoginAlert(login_error_inactive);
                                    }
                                    else if (arr[0] === "5") {
                                        funErrorLoginAlert(login_error_token_ssl);
                                    } else if(arr[0] === JS_EX_ERRORCTS) {
                                        funErrorLoginAlert(global_error_cert_compare_ca);
                                    }
                                    else
                                    {
                                        funErrorLoginAlert(global_errorsql);
                                    }
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                },
                                timeout: 100000
                            });
                            return false;
                       } else if(vResult[0] === JS_EX_NO_CERTCHAN) {
                           funErrorLoginAlert(global_error_chain_cert);
                       } else if(vResult[0] === JS_EX_ERRORCTS) {
                           funErrorLoginAlert(global_error_cert_compare_ca);
                       } else {
                           funErrorLoginAlert(global_errorsql);
                       }
                    },
                    timeout: 100000
                });
                return false;
            }
        </script>
        <script>
            function LoadCaptcha() {
                $.ajax({
                    type: "post",
                    url: "SomeCommon",
                    data: {
                        idParam: 'generatecaptcha'
                    },
                    cache: false,
                    success: function (html)
                    {
                        var arr = sSpace(html).split('#');
                        if (arr[1] !== "")
                        {
                            $("#idCode").val(arr[0]);
                            $("#idImageCap").attr("src", "data:image/jpg;base64," + sSpace(html).replace(arr[0] + "#", ""));
                        }
                        else
                        {
                            funErrorLoginAlert(arr[0]);
                        }
                    }
                });
            }
        </script>
    </head>
    <body onload="<%= sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "" : "LoadCaptcha();"%>">
        <%
            
            String sessionid = request.getSession().getId();
            String contextPath = request.getContextPath();
            response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Path=" + contextPath + "; HttpOnly;");
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        
        <div id="header-two">
            <div class="header-two-123" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "padding: 5px 10px 5px 10px;" : ""%>">
                <div class="container">
                    <div class="col-md-5">
                        <div class="col-sm-6" style="padding: 0px;">
                            <a href="Login.jsp"><img id="idLogoPageHeader" style="max-width: 210px;" class="img-responsive" /></a>
                        </div>
                    </div>
                    <div class="col-md-7" style="text-align: right;">
                        <div class="form-group" style="padding: 0px;">
                            <%
                                if(!sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                            %>
                            <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                            <p style="color: #000000;" id="idDivLanguage"><img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="Images/en_flag.png" /> | <img onclick="loginEN('0');" id="idLoginBannerLanguageRight" style="width: 18px;height: 18px;cursor: pointer;" title="Vietnamese" src="Images/vn_flag.png" /></p>
                            <%
                                } else {
                            %>
                            <script>
                                $(document).ready(function () {
                                    if('<%=isChangeContact%>' === "1") {
                                        header_hotline = header_hotline_minvoice;
                                    }
                                });
                            </script>
                            <p style="color: #000000;padding-top: 22px;" id="idDivLanguage">
                                <span style="color: #FF0000; font-weight: bold; font-size: 16px;height: 25px; vertical-align: middle;">HOTLINE: <script>document.write(header_hotline);</script> </span>
                                <span style="margin-right: 15px;"></span>
                                <img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="Images/en_flag.png" /> | <img onclick="loginEN('0');" id="idLoginBannerLanguageRight" style="width: 18px;height: 18px;cursor: pointer;" title="Vietnamese" src="Images/vn_flag.png" />
                            </p>
                            <%}%>
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
                <form name="loginform" action="" method="post" autocomplete="off">
                    <div class="col-md-6">
                        <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                            <span id="idTitleLogin"></span>
                        </div>
                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                            <label class="control-label"><script>document.write(global_fm_Username);</script></label>
                            <input class="form-control123" style="width: 100%;" name="sUserName" maxlength="50"
                                   id="sUserName" type="text" value="">
                        </div>
                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                            <label class="control-label"><script>document.write(global_fm_Password);</script></label>
                            <div style="display: none;">
                                <input name="sPwd123" maxlength="50" value="" id="sPwd123" type="password">
                            </div>
                            <input class="form-control123" style="width: 100%;"
                                   name="sPwd" maxlength="50" value="" id="sPwd" type="password">
                        </div>
                        <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                            <label class="control-label"><script>document.write(login_fm_captcha);</script></label>
                        </div>
                        <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                            <div class="col-sm-5" style="padding: 0px 10px 10px 0px;">
                                <input class="form-control123" type="text" maxlength="10" name="CaptchaText" id="idCaptchaText"/>
                            </div>
                            <div class="col-sm-6" style="padding: 0px 10px 10px 0px;">
                                <img class="img-rounded" alt="" id="idImageCap"/>
                                <input id="idCode" name="nameCode" style="display: none;" />
                                <a class="idLoadImg" onclick="LoadCaptcha();" style="cursor: pointer;" id="hrefCaptcha">
                                    <img src="Images/refresh.png" style="height: 22px; width: 24px;">
                                </a>
                            </div>
                        </div>
                        <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                            <input type="button" name="btnLogin" id="btnLogin" onclick="loginPage('<%= anticsrf%>');" class="buttonlog" />
                            <input type="button" name="btnFoget" id="btnFoget" onclick="fogetPage();" class="registerlog" />
                            <input type="hidden" name="CsrfToken" id="CsrfToken" value="<%=anticsrf%>"/>
                        </div>
                        <script>
                            document.getElementById("sUserName").value = global_fm_Username;
                            document.getElementById("sPwd").placeholder = global_fm_Password;
                            document.getElementById("idCaptchaText").value = login_fm_captcha;
                            document.getElementById("btnLogin").value = login_fm_buton_login;
                            document.getElementById("btnFoget").value = login_fm_forget;
                            document.getElementById("idTitleLogin").innerHTML = TitleLoginPage;
                            document.getElementById("hrefCaptcha").title = login_title_captcha;
                        </script>
                    </div>
                    <div class="col-md-1">

                    </div>
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
                </form>
                <div style="clear: both;"></div>
            </div>
            <%
                } else {
            %>
            <div>
                <div style="padding: 0px 0 15px 0; text-align: center;color: #56C2E1; font-size: 20px;font-weight: bold;"><script>document.write(global_fm_login_form);</script></div>
                <form name="loginform" action="" method="post" autocomplete="off">
                    <div class="col-md-3"></div>
                    <div class="col-md-6" style="border: 1px solid #56C2E1;">
                        <div style="padding: 10px;">
<!--                            <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                                <span id="idTitleLogin"></span>
                            </div>-->
                            <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                <label class="control-label"><script>document.write(global_fm_Username);</script></label>
                                <input class="form-control123" style="width: 100%;" name="sUserName" maxlength="50"
                                       id="sUserName" type="text" value="">
                            </div>
                            <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                <label class="control-label"><script>document.write(global_fm_Password);</script></label>
                                <div style="display: none;">
                                    <input name="sPwd123" maxlength="50" value="" id="sPwd123" type="password">
                                </div>
                                <input class="form-control123" style="width: 100%;"
                                       name="sPwd" maxlength="50" value="" id="sPwd" type="password">
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;display: none;">
                                <label class="control-label"><script>document.write(login_fm_captcha);</script></label>
                            </div>
                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;display: none;">
                                <div class="col-sm-5" style="padding: 0px 10px 10px 0px;">
                                    <input class="form-control123" type="text" maxlength="10" name="CaptchaText" id="idCaptchaText"/>
                                </div>
                                <div class="col-sm-6" style="padding: 0px 10px 10px 0px;">
                                    <img class="img-rounded" alt="" id="idImageCap"/>
                                    <input id="idCode" name="nameCode" style="display: none;" />
                                    <a class="idLoadImg" onclick="LoadCaptcha();" style="cursor: pointer;" id="hrefCaptcha">
                                        <img src="Images/refresh.png" style="height: 22px; width: 24px;">
                                    </a>
                                </div>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;text-align: center;">
                                <input type="button" name="btnLogin" id="btnLogin" onclick="loginPageNoCaptcha('<%= anticsrf%>');" class="buttonlog" />
                                <input type="button" name="btnFoget" id="btnFoget" onclick="fogetPage();" class="registerlog" style="font-size: 13px;color: #000000;" />
                                <input type="hidden" name="CsrfToken" id="CsrfToken" value="<%=anticsrf%>"/>
                            </div>
                            <script>
                                document.getElementById("sUserName").value = global_fm_Username;
                                document.getElementById("sPwd").placeholder = global_fm_Password;
                                document.getElementById("idCaptchaText").value = login_fm_captcha;
                                document.getElementById("btnLogin").value = login_fm_buton_login;
                                document.getElementById("btnFoget").value = login_fm_forget;
//                                document.getElementById("idTitleLogin").innerHTML = TitleLoginPage;
                                document.getElementById("hrefCaptcha").title = login_title_captcha;
                            </script>
                        </div>
                    </div>
                    <div class="col-md-3"></div>
                </form>
                <div style="clear: both;"></div>
            </div>
            <%
                }
            %>
<!--                </form>-->
                <!--<div style="clear: both;"></div>-->
            <!--</div>-->
            <a href="http://metop.info" style="display: none;">metop.info close</a>
        </div>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
        <%
            String sTermEnable = conf.GetPropertybyCode(Definitions.CONFIG_WEBSITE_POLICY_TERMS_ENABLED).trim();
            if("1".equals(sTermEnable)) {
        %>
        <div id="footer1" style="padding-bottom: 15px;clear: both;">
            <%@ include file="TwoFooter.jsp"%>
        </div>
        <%
            } else {
        %>
        <div id="footer1" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background: #f40000;border-top:none;" : ""%>">
            <%@ include file="LFooter.jsp"%>
        </div>
        <%
            }
        %>
    </body>
</html>

