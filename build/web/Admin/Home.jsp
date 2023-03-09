<%-- 
    Document   : Home
    Created on : Oct 10, 2013, 11:04:09 AM
    Author     : Thanh
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="ConnectionParam.jsp" %>
<!DOCTYPE html>
<%
    String sessionid = request.getSession().getId();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type="text/javascript">
            document.title = TitleHomePage;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                console.log('A-SESSID: ' + '<%=sessionid%>');
            });
            function resetPass()
            {
                document.getElementById("passOlds").value = "";
                document.getElementById("passNews").value = "";
                document.getElementById("passNewConforms").value = "";
            }
            function ValidateForm(idCSRF)
            {
                var oldp = document.getElementById("passOlds").value;
                var newp = document.getElementById("passNews").value;
                var confirmp = document.getElementById("passNewConforms").value;
                var MinLenghPass = parseInt(document.myname.MinLenghPass.value);
                var EffectiveDayPass = parseInt(document.myname.EffectiveDayPass.value);
                var NumericPass = document.myname.NumericPass.value;
                var AlphaPass = document.myname.AlphaPass.value;
                var SpecialPass = document.myname.SpecialPass.value;
                var UpcaseAlphanumeric = document.myname.UpcaseAlphanumeric.value;
                if (!JSCheckEmptyField(oldp) || !JSCheckEmptyField(newp) || !JSCheckEmptyField(confirmp))
                {
                    funErrorAlert(global_req_all);
                    return false;
                }
                if (newp.split(' ').length > 1)
                {
                    funErrorAlert(pass_req_no_space);
                    document.getElementById("passNews").focus();
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
                var control = document.getElementById("passNews");
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
                        document.getElementById("passNewConforms").value = "";
                        return false;
                    }
                }
                if (SpecialPass === "true")
                {
                    if (specialString < 1)
                    {
                        funErrorAlert(pass_req_special);
                        control.value = "";
                        document.getElementById("passNewConforms").value = "";
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
                        document.getElementById("passNewConforms").value = "";
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
                        document.getElementById("passNewConforms").value = "";
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
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'changepass',
                        Username: $("#Username").val(),
                        NewPass: newp,
                        OldPass: oldp,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0") {
                            funSuccAlert(pass_succ_change, "Home.jsp");
                        }
                        else if (myStrings[0] === "1") {
                            RedirectPageLoginNoSess(login_error_lock);
                        }
                        else if (myStrings[0] === "2") {
                            var vMAX_RESET_PASSWORD = document.myname.MAX_RESET_PASSWORD.value;
                            var vTitleAnother = pass_error_choise_another_exists.replace(JS_STR_CHOISE_ANOTHER_NUMBER,vMAX_RESET_PASSWORD);
                            funErrorAlert(vTitleAnother);
                        }
                        else if (myStrings[0] === "3") {
                            funErrorAlert(pass_error_old);
                        }
                        else if (myStrings[0] === "4") {
                            funErrorAlert(login_error_inactive);
                        }
                        else if (myStrings[0] === "5") {
                            funErrorAlert(pass_error_account_old);
                        }
                        else if (myStrings[0] === "PASS_SPACE") {
                            funErrorAlert(pass_req_no_space);
                        }
                        else if (myStrings[0] === "PASS_LENGTH") {
                            funErrorAlert(pass_req_min_greater + MinLenghPass + global_fm_character + "\n" + pass_req_max_less + EffectiveDayPass + global_fm_character);
                        }
                        else if (myStrings[0] === "PASS_SAME") {
                            funErrorAlert(pass_req_another_old);
                        }
                        else if (myStrings[0] === "PASS_NUMBER") {
                            funErrorAlert(pass_req_number);
                        }
                        else if (myStrings[0] === "PASS_ALPHA") {
                            funErrorAlert(pass_req_character);
                        }
                        else if (myStrings[0] === "PASS_SPECIAL") {
                            funErrorAlert(pass_req_special);
                        }
                        else if (myStrings[0] === "PASS_CAPITAL") {
                            funErrorAlert(pass_req_upcase);
                        }
                        else if (myStrings[0] === "PASS_EMPTY") {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (myStrings[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN) {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
        </script>
        <style>
            button,.buttons,.btn,.modal-footer .btn+.btn{margin-right:0px}
            .x_title{
                padding: 1px 5px 5px 6px;
            }
        </style>
    </head>
    <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
         left: 0; height: 100%;" class="loading-gif">
        <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
    </div>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
        %>
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <%@ include file="../Modules/Header.jsp" %>
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                            <%@ include file="../Modules/MenuLeft.jsp" %>
                        </div>
                    </div>
                </div>
                <div class="top_nav">
                    <%@ include file="../Modules/Navigate.jsp" %>
                    <script>
                        document.getElementById("idNameURL").innerHTML = TitleHomePage;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <%        
//                            String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                            String strMin = "";
                            String strMax = "";
                            String strBack_CyclePass = "";
                            Boolean strNumericPass = false;
                            Boolean strAlphaPass = false;
                            Boolean strSpecialPass = false;
                            Boolean strUpcaseAlphanumeric = false;
                            String anticsrf = "" + Math.random();
                            request.getSession().setAttribute("anticsrf", anticsrf);
                            String strName = (String) session.getAttribute("sesFullname");
                            String strUserName = (String) session.getAttribute("sUserID");
                            String strUserID = (String) session.getAttribute("UserID");
                            String strRoleDes = (String) session.getAttribute("RoleDesc");
                            String strBranchDes = (String) session.getAttribute("SessAgentCode") + " - " + (String) session.getAttribute("SessAgentName");
                            String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                            String sEmail = "";
                            String sPhone = "";
                            try {
                                BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                db.S_BO_USER_DETAIL(EscapeUtils.escapeHtml(strUserID),sessLanguage, rsUser);
                                if (rsUser[0].length > 0) {
                                    sEmail = EscapeUtils.CheckTextNull(rsUser[0][0].EMAIL);
                                    sPhone = EscapeUtils.CheckTextNull(rsUser[0][0].MSISDN);
                                }
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                    {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MIN_LENGTH_PASSWORD))
                                        {
                                            strMin = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MAX_LENGTH_PASSWORD))
                                        {
                                            strMax = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
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
                                }
                            } catch(Exception e){
                                CommonFunction.LogExceptionJSP(request.getRequestURI(), e.getMessage(), e);
                            }
                    %>
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(global_fm_title_account);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(global_fm_Username);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="Username" id="Username" class="form-control123" value="<%= strUserName%>"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(global_fm_role);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="Role" class="form-control123" value="<%= strRoleDes%>"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(global_fm_fullname);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="FullName" class="form-control123" value="<%= strName%>"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(global_fm_Branch);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="Branch" class="form-control123" value="<%= strBranchDes%>"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(token_fm_email);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="Email" class="form-control123" value="<%= sEmail%>"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;padding-top: 10px;">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-top: 5px;">
                                                    <script>document.write(token_fm_mobile);</script>
                                                </label>
                                                <div class="col-sm-8" style="padding-right: 0px;">
                                                    <input readonly name="Phone" class="form-control123" value="<%= sPhone%>"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(global_fm_Password_change);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input type="button" id="btnClose" class="btn btn-info" onclick="resetPass();"/>
                                            </li>
                                            <script>
                                                document.getElementById("btnSave").value = global_fm_button_PasswordChange;
                                                document.getElementById("btnClose").value = global_fm_refresh;
                                            </script>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="myname" method="post" class="form-horizontal" autocomplete="off">
                                            <input type="hidden" name="MinLenghPass" value="<%= strMin%>" />
                                            <input type="hidden" name="EffectiveDayPass" value="<%= strMax%>" />
                                            <input type="hidden" name="MAX_RESET_PASSWORD" value="<%= strBack_CyclePass%>" />
                                            <input type="hidden" name="NumericPass" value="<%= strNumericPass%>" />
                                            <input type="hidden" name="AlphaPass" value="<%= strAlphaPass%>" />
                                            <input type="hidden" name="SpecialPass" value="<%= strSpecialPass%>" />
                                            <input type="hidden" name="UpcaseAlphanumeric" value="<%= strUpcaseAlphanumeric%>" />
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(global_fm_Password_old);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="password" maxlength="30" class="form-control123" id="passOlds" name="passOld">
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(global_fm_Password_new);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="password" maxlength="30" class="form-control123" id="passNews" name="passNew">
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label123"><script>document.write(global_fm_Password_conform);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="password" maxlength="30" class="form-control123" id="passNewConforms" name="passNewConform">
                                            </div>
                                        </form>
                                        <script src="../style/jquery.min.js"></script>
                                        <script src="../style/bootstrap.min.js"></script>
                                        <script src="../style/custom.min.js"></script>
                                    </div>
                                    <script type="text/javascript">
                                        $(document).ready(function () {
                                            $('#passOlds').keydown(function (event) {
                                                if (event.keyCode === 13) {
                                                    ValidateForm('<%= anticsrf%>');
                                                    return false;
                                                }
                                            });
                                            $('#passNews').keydown(function (event) {
                                                if (event.keyCode === 13) {
                                                    ValidateForm('<%= anticsrf%>');
                                                    return false;
                                                }
                                            });
                                            $('#passNewConforms').keydown(function (event) {
                                                if (event.keyCode === 13) {
                                                    ValidateForm('<%= anticsrf%>');
                                                    return false;
                                                }
                                            });
                                        });
                                    </script>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <%
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
