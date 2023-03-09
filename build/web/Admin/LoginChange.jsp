<%-- 
    Document   : LoginChange
    Created on : Nov 6, 2013, 9:41:13 PM
    Author     : Thanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="vn.ra.utility.Config"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.object.*"%>
<%--<%@include file="ConnectionParam.jsp" %>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script src="../js/bootstrap.min.js"></script>
        <link href="../js/bootstrap.min.css" rel="stylesheet">
        <link href="../font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="../js/jquery.min.js"></script>
        <link rel="stylesheet" href="../js/cssLogin_1.css">
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = pass_fm_Password_first;
            $(document).ready(function () {
                $(".loading-gif").hide();
            });
            function resetPass()
            {
                document.getElementById("sPwdOld").value = "";
                document.getElementById("passNews").value = "";
                document.getElementById("passNewConforms").value = "";
            }
            function backPage()
            {
                window.location = "../Modules/Logout.jsp";
            }
            function ValidateForm(idCSRF)
            {
                var oldp = document.loginform.sPwdOld.value;
                var newp = document.loginform.sPwdNew.value;
                var confirmp = document.loginform.sConfirmPwd.value;
                var MinLenghPass = parseInt(document.loginform.MinLenghPass.value);
                var EffectiveDayPass = parseInt(document.loginform.EffectiveDayPass.value);
                var NumericPass = document.loginform.NumericPass.value;
                var AlphaPass = document.loginform.AlphaPass.value;
                var SpecialPass = document.loginform.SpecialPass.value;
                var UpcaseAlphanumeric = document.loginform.UpcaseAlphanumeric.value;
                if (!JSCheckEmptyField(oldp) || !JSCheckEmptyField(newp) || !JSCheckEmptyField(confirmp))
                {
                    funErrorAlert(global_req_all);
                    return false;
                }
                if (newp.split(' ').length > 1)
                {
                    funErrorAlert(pass_req_no_space);
                    return false;
                }
                if (newp.length < MinLenghPass)
                {
                    funErrorAlert(pass_req_min_greater + MinLenghPass + global_fm_character);
                    return false;
                }
                if (newp.length > EffectiveDayPass)
                {
                    funErrorAlert(pass_req_max_less + EffectiveDayPass + global_fm_character);
                    return false;
                }
                var control = document.loginform.sPwdNew;
                var myString = control.value;
                var ValidateDigits = /[^0-9]/g;
                var ValidateSpChar = /[a-zA-Z0-9]/g;
                var ValidateChar = /[^a-zA-Z]/g;
                var digitString = myString.replace(ValidateDigits, "");
                var specialString = myString.replace(ValidateSpChar, "");
                var charString = myString.replace(ValidateChar, "");
                if (AlphaPass === "true")
                {
                    if (charString < 1)
                    {
                        funErrorAlert(pass_req_character);
                        control.value = "";
                        document.loginform.sConfirmPwd.value = "";
                        control.focus();
                        return false;
                    }
                }
                if (SpecialPass === "true")
                {
                    if (specialString < 1)
                    {
                        funErrorAlert(pass_req_special);
                        control.value = "";
                        document.loginform.sConfirmPwd.value = "";
                        control.focus();
                        return false;
                    }
                }
                if (NumericPass === "true")
                {
                    if (digitString < 1)
                    {
                        funErrorAlert(pass_req_number);
                        control.value = "";
                        document.loginform.sConfirmPwd.value = "";
                        control.focus();
                        return false;
                    }
                }
                if (UpcaseAlphanumeric === "true")
                {
                    for (var i = 0, len = myString.length, count = 0, ch; i < len; ++i)
                    {
                        ch = myString.charAt(i);
                        if (ch >= 'A' && ch <= 'Z')
                            ++count;
                    }
                    if (count <= 0)
                    {
                        funErrorAlert(pass_req_upcase);
                        control.value = "";
                        document.loginform.sConfirmPwd.value = "";
                        control.focus();
                        return false;
                    }
                }
                if (oldp === newp)
                {
                    funErrorAlert(pass_req_another_old);
                    return false;
                }
                if (newp !== confirmp)
                {
                    funErrorAlert(pass_req_conform_new);
                    return false;
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var s = localStorage.getItem("VN");
                $.ajax({
                    type: "post",
                    url: "../LoginCommon",
                    data: {
                        idParam: 'changepassfirst',
                        sPwdOld: document.getElementById('sPwdOld').value,
                        sPwdNew: document.getElementById('sPwdNew').value,
                        nameUser: document.getElementById('nameUser').value,
                        svn: s,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            if (arr[1] === "1")
                            {
                                checkRedrectHome("../Certificate/RegisterList.jsp");
//                                funSuccAlert(pass_succ_change, "../Certificate/RegisterList.jsp");
                            } else {
                                funSuccAlert(pass_succ_change, "Home.jsp");
                            }
                           
                        }
                        else if (arr[0] === "1") {
                            RedirectPageLoginNoSess(login_error_lock);
                        }
                        else if (arr[0] === "2") {
                            var vMAX_RESET_PASSWORD = document.loginform.MAX_RESET_PASSWORD.value;
                            var vTitleAnother = pass_error_choise_another_exists.replace(JS_STR_CHOISE_ANOTHER_NUMBER,vMAX_RESET_PASSWORD);
                            funErrorAlert(vTitleAnother);
                        }
                        else if (arr[0] === "3") {
                            funErrorAlert(pass_error_old);
                        }
                        else if (arr[0] === "4") {
                            funErrorAlert(login_error_inactive);
                        }
                        else if (arr[0] === "5") {
                            funErrorAlert(pass_error_account_old);
                        }
                        else if (arr[0] === "PASS_SPACE") {
                            funErrorAlert(pass_req_no_space);
                        }
                        else if (arr[0] === "PASS_LENGTH") {
                            funErrorAlert(pass_req_min_greater + MinLenghPass + global_fm_character + "\n" + pass_req_max_less + EffectiveDayPass + global_fm_character);
                        }
                        else if (arr[0] === "PASS_SAME") {
                            funErrorAlert(pass_req_another_old);
                        }
                        else if (arr[0] === "PASS_NUMBER") {
                            funErrorAlert(pass_req_number);
                        }
                        else if (arr[0] === "PASS_ALPHA") {
                            funErrorAlert(pass_req_character);
                        }
                        else if (arr[0] === "PASS_SPECIAL") {
                            funErrorAlert(pass_req_special);
                        }
                        else if (arr[0] === "PASS_CAPITAL") {
                            funErrorAlert(pass_req_upcase);
                        }
                        else if (arr[0] === "PASS_EMPTY") {
                            funErrorAlert(global_req_all);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else
                        {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function checkRedrectHome(pageCheck){
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'checkredrecthome',
                        pageCheck: pageCheck
                    },
                    cache: false,
                    success: function (html)
                    {
                        var arr = sSpace(html).split('#');
                        if (arr[1] === "1") {
                            funSuccAlert(pass_succ_change, "../Certificate/RegisterList.jsp");
//                            window.location = "Certificate/RegisterList.jsp";
                        }
                        else
                        {
                            funSuccAlert(pass_succ_change, "Home.jsp");
                        }
                    }
                });
            }
        </script>
    </head>
    <%        String strMin = "";
        String strMax = "";
        String strBack_CyclePass = "";
        Boolean strNumericPass = false;
        Boolean strAlphaPass = false;
        Boolean strSpecialPass = false;
        Boolean strUpcaseAlphanumeric = false;
        int checkExFinnaly = 0;
        CommonFunction com = new CommonFunction();
        ConnectDatabase db = new ConnectDatabase();
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        Config conf = new Config();
        String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
        String isChangeContact = "0";
        if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
            if(request.getRequestURL().toString().replace(request.getRequestURI(), "").toUpperCase().contains("MINVOICE.")){
                isChangeContact = "1";
            }
        }
    %>
    <body>
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                try {
                    String strUser = (String) session.getAttribute("sUserID");
                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                    if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MIN_LENGTH_PASSWORD))
                        {
                            strMin = String.valueOf(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MAX_LENGTH_PASSWORD))
                        {
                            strMax = String.valueOf(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_NUMERIC_PASSWORD))
                        {
                            strNumericPass = "1".equals(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ALPHA_PASSWORD))
                        {
                            strAlphaPass = "1".equals(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SPECIAL_PASSWORD))
                        {
                            strSpecialPass = "1".equals(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_UPERCASE_PASSWORD))
                        {
                            strUpcaseAlphanumeric = "1".equals(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CYCLE_PASSWORD))
                        {
                            strBack_CyclePass = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                    }
//                        strMin = String.valueOf(rs[0][0].MIN_LENGH_PASS);
//                        strMax = String.valueOf(rs[0][0].EFFECTIVE_DAY_PASS);
//                        strNumericPass = rs[0][0].NUMERIC_PASS;
//                        strAlphaPass = rs[0][0].ALPHA_PASS;
//                        strSpecialPass = rs[0][0].SPECIAL_PASS;
//                        strUpcaseAlphanumeric = rs[0][0].UPCASE_ALPHA_NUMERIC;
//                        strBack_CyclePass = String.valueOf(rs[0][0].Back_CyclePass);
                    }
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div id="header-two">
            <div class="header-two-123">
                <div class="container">
                    <div class="col-md-5">
                        <div class="col-sm-6" style="padding: 0px;">
                            <img id="idLogoPageHeader" style="max-width: 210px;" class="img-responsive" />
                        </div>
                    </div>
                    <div class="col-md-7" style="text-align: right;">
                        <div class="form-group" style="padding: 0px;">
                            <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                            <p style="color: #000000;" id="idDivLanguage"><img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="../Images/en_flag.png" /> | <img onclick="loginEN('0');" style="width: 18px;height: 18px;cursor: pointer;" id="idLoginBannerLanguageRight" title="Vietnamese" src="../Images/vn_flag.png" /></p>
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
                <form name="loginform" method="post" autocomplete="off">
                    <div class="col-md-6">
                        <table style="width: 100%;">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <td>
                                        <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                                            <span><script>document.write(pass_fm_Password_first);</script></span>
                                        </div>
                                        <input type="hidden" name="MinLenghPass" value="<%= strMin%>" />
                                        <input type="hidden" name="EffectiveDayPass" value="<%= strMax%>" />
                                        <input type="hidden" name="MAX_RESET_PASSWORD" value="<%= strBack_CyclePass%>" />
                                        <input type="hidden" name="NumericPass" value="<%= strNumericPass%>" />
                                        <input type="hidden" name="AlphaPass" value="<%= strAlphaPass%>" />
                                        <input type="hidden" name="SpecialPass" value="<%= strSpecialPass%>" />
                                        <input type="hidden" name="UpcaseAlphanumeric" value="<%= strUpcaseAlphanumeric%>" />
                                    </td>
                            <script>
                                var image = document.getElementById('idLogoPageHeader');
                                image.src = "../" + LinkLogoPage;
                            </script>
                            </tr>
                            <tr>
                                <td>
                                    <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                        <label class="control-label"><script>document.write(global_fm_Password_old);</script></label>
                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                        <input class="form-control123" style="width: 100%;" name="sPwdOld" maxlength="50"
                                               id="sPwdOld" type="password">
                                        <input type="hidden" id="nameUser" name="nameUser" value="<%= strUser%>" />
                                    </div>
                                    <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                        <label class="control-label"><script>document.write(global_fm_Password_new);</script></label>
                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                        <input class="form-control123" style="width: 100%;"
                                               name="sPwdNew" maxlength="50" id="sPwdNew" type="password">
                                    </div>
                                    <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                        <label class="control-label"><script>document.write(global_fm_Password_conform);</script></label>
                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                        <input class="form-control123" style="width: 100%;" 
                                               name="sConfirmPwd" maxlength="50" id="sConfirmPwd" type="password">
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: left;">
                                    <input type="button" name="btnLogin" id="btnSave" onclick="ValidateForm('<%= anticsrf%>');" class="buttonlog" />
                                    <span style="padding-right: 10px;"></span>
                                    <input type="button" name="btnLogin" id="btnClose" onclick="backPage();" class="buttonlog" />
                                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                    <script>
                                        document.getElementById("btnSave").value = login_fm_buton_OK;
                                        document.getElementById("btnClose").value = global_fm_button_back;
                                    </script>
                                </td>
                            </tr>
                            </tbody>
                        </table>
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
                                    imageBanner.src = "../" + LinkBannerPage;
                                });
                            </script>
                        </div>
<!--                        <div class="loginIconCss">
                        <div class="col-sm-5" style="padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/standardPKIToken.png" alt="Standard PKI Token" title="Standard PKI Token" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="text-align: right; padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/otpToken.png" alt="OTP Token" title="OTP Token" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="padding: 0px 10px 0px 10px;">
                            <div class="aOpen">
                                <img src="../Images/fidoPKI.png" alt="FIDO Token" title="FIDO Token" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="text-align: right; padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/eSignature.png" alt="E-Signature OTP Token" title="E-Signature OTP Token" class="img-responsive"/>
                            </div>
                        </div>

                        <div class="col-sm-5" style="padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/bioFido.png" alt="BIO Fido" title="BIO Fido" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="text-align: right; padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/lcdPKIToken.png" alt="LCD PKI Token" title="LCD PKI Token" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/chipEmpeddedCard.png" alt="Chip Empedded Card" title="Chip Empedded Card" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="text-align: right; padding: 0px 10px 10px 10px;">
                            <div class="aOpen">
                                <img src="../Images/wpkiUsim.png" alt="WPKI USIM" title="WPKI USIM" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="padding: 0px 10px 0px 10px;">
                            <div class="aOpen">
                                <img src="../Images/otpDispalyCard.png" alt="OTP Display Card" title="OTP Display Card" class="img-responsive"/>
                            </div>
                        </div>
                        <div class="col-sm-5" style="text-align: right; padding: 0px 10px 0px 10px;">
                            <div class="aOpen">
                                <img src="../Images/UAF-fido.png" alt="UAF Fido" title="UAF Fido" class="img-responsive"/>
                            </div>
                        </div>
                        </div>-->
                    </div>
                </form>
                <div style="clear: both;"></div>
            </div>
            <%
                } else {
            %>
            <div>
                <form name="loginform" method="post" autocomplete="off">
                    <div class="col-md-3"></div>
                    <div class="col-md-6" style="border: 1px solid #56C2E1;">
                        <table style="width: 100%;">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <td style="height: 20px;"></td>
                                </tr>
                                <tr>
                                    <td>
                                        <div style="padding-bottom: 10px;color: #73879C; font-weight: 600; font-size: 20px;">
                                            <span><script>document.write(pass_fm_Password_first);</script></span>
                                        </div>
                                        <input type="hidden" name="MinLenghPass" value="<%= strMin%>" />
                                        <input type="hidden" name="EffectiveDayPass" value="<%= strMax%>" />
                                        <input type="hidden" name="MAX_RESET_PASSWORD" value="<%= strBack_CyclePass%>" />
                                        <input type="hidden" name="NumericPass" value="<%= strNumericPass%>" />
                                        <input type="hidden" name="AlphaPass" value="<%= strAlphaPass%>" />
                                        <input type="hidden" name="SpecialPass" value="<%= strSpecialPass%>" />
                                        <input type="hidden" name="UpcaseAlphanumeric" value="<%= strUpcaseAlphanumeric%>" />
                                    </td>
                                    <script>
                                        var image = document.getElementById('idLogoPageHeader');
                                        image.src = "../" + LinkLogoPage;
                                    </script>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                            <label class="control-label"><script>document.write(global_fm_Password_old);</script></label>
                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                            <input class="form-control123" style="width: 100%;" name="sPwdOld" maxlength="50"
                                                   id="sPwdOld" type="password">
                                            <input type="hidden" id="nameUser" name="nameUser" value="<%= strUser%>" />
                                        </div>
                                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                            <label class="control-label"><script>document.write(global_fm_Password_new);</script></label>
                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                            <input class="form-control123" style="width: 100%;"
                                                   name="sPwdNew" maxlength="50" id="sPwdNew" type="password">
                                        </div>
                                        <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                            <label class="control-label"><script>document.write(global_fm_Password_conform);</script></label>
                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                            <input class="form-control123" style="width: 100%;" 
                                                   name="sConfirmPwd" maxlength="50" id="sConfirmPwd" type="password">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: left;">
                                        <input type="button" name="btnLogin" id="btnSave" onclick="ValidateForm('<%= anticsrf%>');" class="buttonlog" />
                                        <span style="padding-right: 10px;"></span>
                                        <input type="button" name="btnLogin" id="btnClose" onclick="backPage();" class="buttonlog" />
                                        <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                        <script>
                                            document.getElementById("btnSave").value = login_fm_buton_OK;
                                            document.getElementById("btnClose").value = global_fm_button_back;
                                        </script>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="height: 20px;"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-md-3"></div>
                    <div style="clear: both;"></div>
                </form>
            </div>
            <%
                }
            %>
            <script type="text/javascript">
                $(document).ready(function () {
                    $('#sPwdOld').keydown(function (event) {
                        if (event.keyCode === 13) {
                            ValidateForm('<%= anticsrf%>');
                            return false;
                        }
                    });
                    $('#sPwdNew').keydown(function (event) {
                        if (event.keyCode === 13) {
                            ValidateForm('<%= anticsrf%>');
                            return false;
                        }
                    });
                    $('#sConfirmPwd').keydown(function (event) {
                        if (event.keyCode === 13) {
                            ValidateForm('<%= anticsrf%>');
                            return false;
                        }
                    });
                });
            </script>
        </div>
        <%
            String sTermEnable = conf.GetPropertybyCode(Definitions.CONFIG_WEBSITE_POLICY_TERMS_ENABLED).trim();
            if("1".equals(sTermEnable)) {
        %>
        <div id="footer1" style="padding-bottom: 15px;clear: both;">
            <%@ include file="../TwoFooter.jsp"%>
        </div>
        <%
            } else {
        %>
        <div id="footer1" style="<%=sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background: #f40000;" : ""%>">
            <%@ include file="../LFooter.jsp"%>
        </div>
        <%
            }
        %>
        <%
        } catch (Exception e) {
            checkExFinnaly = 1;
            CommonFunction.LogExceptionJSP(request.getRequestURI(), e.getMessage(), e);
        } finally {
            if (checkExFinnaly == 1) {
                checkExFinnaly = 0;
        %>
        <script type="text/javascript">
            window.onload = function () {
                RedirectPageError();
            }();
        </script>
        <%
                    }
                }
            } else {
                %>
        <script type="text/javascript">
                                                window.onload = function () {
                                                    RedirectPageLoginNoSess(global_alert_login);
                                                }();
        </script>
        <%
            }
        %>
    </body>
</html>