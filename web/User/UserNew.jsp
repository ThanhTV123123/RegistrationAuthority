<%-- 
    Document   : UserNew
    Created on : Oct 12, 2013, 1:21:31 PM
    Author     : Thanh
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <!--<link href="../style/switchery/dist/switchery.min.css" rel="stylesheet">-->
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <%
            Config conf = new Config();
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = user_title_add;
            function randomStringNew() {
                var chars1 = "0123456789";
                var string_length1 = 8;
                var randomstring1 = '';
                for (var i = 0; i < string_length1; i++)
                {
                    var rnum = Math.floor(Math.random() * chars1.length);
                    randomstring1 += chars1.substring(rnum, rnum + 1);
                }
                $("#Password").val(randomstring1);
            }
            $(document).ready(function () {
                $('.loading-gif').hide();
                randomStringNew();
//                JSLoadPhoneNew($("#MobileNumber"));
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.Username.value))
                {
                    document.myname.Username.focus();
                    funErrorAlert(policy_req_empty + global_fm_Username);
                    return false;
                } else {
                    if(JSCheckSpaceField(document.myname.Username.value))
                    {
                        document.myname.Username.focus();
                        funErrorAlert(user_req_no_space);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.Password.value))
                {
                    document.myname.Password.focus();
                    funErrorAlert(policy_req_empty + global_fm_Password);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.FullName.value))
                {
                    document.myname.FullName.focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }
                var email = document.myname.Email;
                if (!JSCheckEmptyField(email.value))
                {
                    email.focus();
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                }
                else
                {
//                    var vValuePhone = sSpace($("#Email").val());
//                    var cChatStatus = getCheckStatusEmail(vValuePhone);
//                    cChatStatus.done(function(msg) {
//                        var s = sSpace(msg).split('#');
//                        if(s[0] !== "0")
//                        {
//                            $("#Email").focus();
//                            funErrorAlert(global_req_mail_format);
//                            return false;
//                        }
//                    });
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), email.value)) {
                        email.focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                var x = document.myname.MobileNumber;
                if (!JSCheckEmptyField(x.value))
                {
                    x.focus();
                    funErrorAlert(policy_req_empty + global_fm_phone);
                    return false;
                }
                else
                {
//                    var vValuePhoneNum = trimAllString($("#MobileNumber").val());
//                    var cChatStatusNum = getCheckStatusPhone(vValuePhoneNum);
//                    cChatStatusNum.done(function(msg) {
//                        var s = sSpace(msg).split('#');
//                        if(s[0] !== "0")
//                        {
//                            $("#MobileNumber").focus();
//                            funErrorAlert(global_req_phone_format);
//                            return false;
//                        }
//                    });
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#MobileNumber")))
                    {
                        x.focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                if(document.myname.UserRole.value !== JS_STR_ROLE_ADMIN_CA && document.myname.UserRole.value !== JS_STR_ROLE_ADMIN_SUPER_CA)
                {
                    document.myname.idCertCompany.value = "";
                }
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'adduser',
                        Username: document.myname.Username.value,
                        Password: document.myname.Password.value,
                        FullName: document.myname.FullName.value,
                        UserRole: document.myname.UserRole.value,
                        idSSL: document.myname.idCertCompany.value,
                        Email: email.value,
                        MobileNumber: x.value,
                        BranchName: document.myname.BranchName.value.split('#')[0],
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(user_succ_add, "UserList.jsp");
                        }
                        else if (arr[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (arr[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === "1") {
                            funErrorAlert(user_exists_username);
                        }
                        else if (arr[0] === "2") {
                            funErrorAlert(user_exists_email);
                        } else if (arr[0] === "3") {
                            funErrorAlert(user_exists_user_role_admin);
                        } else if (arr[0] === "4") {
                            funErrorAlert(user_exists_cert_hash);
                        } else if (arr[1] === JS_STR_ERROR_CODE_99) {
                            funErrorAlert(global_error_login_info);
                        } else {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshUserSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "UserList.jsp";
                        }
                        else
                        {
                            window.location = "UserList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
            .panel_toolbox { min-width: 0;}
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
                session.setAttribute("sessRoleFunctionsToken",null);
                session.setAttribute("sessRoleFunctionsCert",null);
                session.setAttribute("sessRoleFunctionsAnother",null);
                session.setAttribute("sessCertSSLToken", null);
                session.setAttribute("sessHashSSLToken", null);
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String sessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessAgentName = session.getAttribute("SessAgentName").toString().trim();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String sPermisionFrist = "";
                String sRegexPolicy = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                        {
                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            break;
                        }
                    }
                }
                String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                if(!"".equals(sRegexPolicy))
                {
                    sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                    sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                }
                String certAccessEnabled = conf.GetPropertybyCode(Definitions.CONFIG_CERT_MODULES_ACCESS_ENABLED);
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
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
                        document.getElementById("idNameURL").innerHTML = user_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(user_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_add;
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleUsername"></label>
                                                        <label id="idLblNoteUsername"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="Username" maxlength="64" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleUsername").text(global_fm_Username);
                                                    $("#idLblNoteUsername").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitlePassword"></label>
                                                        <label id="idLblNotePassword"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <div style="float: left;width: 50%;">
                                                            <input type="text" name="Password" id="Password" maxlength="30" class="form-control1234" style="width: 100%;" />
                                                        </div>
                                                        <div style="float: right;text-align: right;">
                                                            <input type="button" class="btn btn-info" style="margin-right: 0px;" id="btnGenPass" onclick="randomStringNew();" />
                                                        </div>
                                                        <script>
                                                            document.getElementById("btnGenPass").value = global_fm_gen_pass;
                                                        </script>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitlePassword").text(global_fm_Password);
                                                    $("#idLblNotePassword").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleFullname"></label>
                                                        <label id="idLblNoteFullname" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="FullName" maxlength="64" class="form-control123"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleFullname").text(global_fm_fullname);
                                                    $("#idLblNoteFullname").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <%
                                                String sParent_Frist = "";
                                                //if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="BranchName" class="form-control123" onchange="LOAD_ROLE_FOR_BRANCH(this.value);">
                                                            <%
//                                                                BRANCH[][] rst = new BRANCH[1][];
                                                                String sBranchIDRoot = "";
                                                                try {
//                                                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                                                    String sSessTreeName = "sessTreeBranchSystem";
                                                                    if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                        sSessTreeName = "sessTreeBranchSystemRoot";
                                                                    }
                                                                    BRANCH[][] rst = (BRANCH[][]) session.getAttribute(sSessTreeName);
                                                                    if (rst[0].length > 0) {
                                                                        for (BRANCH temp1 : rst[0]) {
                                                                            if("".equals(sParent_Frist)) {
                                                                                sParent_Frist = String.valueOf(temp1.PARENT_ID);
                                                                            }
                                                                            if("".equals(sBranchIDRoot)) {
                                                                                if(String.valueOf(temp1.ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                    sBranchIDRoot = String.valueOf(temp1.ID);
                                                                                }
                                                                            }
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID) + "#" + String.valueOf(temp1.PARENT_ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
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
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleAgentName").text(global_fm_Branch);
                                                    function LOAD_ROLE_FOR_BRANCH(objc)
                                                    {
                                                        if(objc.split('#')[0] === JS_STR_AGENCY_ROOT_CA)
                                                        {
                                                            $("#UserRole").attr("disabled", true);
                                                        } else{$("#UserRole").attr("disabled", false);}
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../JSONCommon",
                                                            data: {
                                                                idParam: 'loadrole_forbranch',
                                                                idParent: objc.split('#')[1]
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                if (html.length > 0)
                                                                {
                                                                    var sessRoleAdminCA = '<%=SessRoleID_ID%>';
                                                                    var cbxUSER = document.getElementById("UserRole");
                                                                    removeOptions(cbxUSER);
                                                                    var obj = JSON.parse(html);
                                                                    if (obj[0].Code === "0")
                                                                    {
                                                                        var vRoleID_Frist = "";
                                                                        if(objc.split('#')[0] === JS_STR_AGENCY_ROOT_CA) {
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(vRoleID_Frist === "") {
                                                                                    vRoleID_Frist = obj[i].ID;
                                                                                }
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                                if(obj[i].ID === objc.split('#')[1])
                                                                                {
                                                                                    $("#UserRole" + " option[value='" + JS_STR_ROLE_ADMIN_SUPER_CA + "']").attr("selected", "selected");
                                                                                }
                                                                            }
                                                                            $("#UserRole").attr("disabled", true);
                                                                        } else {
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(vRoleID_Frist === "")
                                                                                {
                                                                                    vRoleID_Frist = obj[i].ID;
                                                                                }
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                            }
                                                                            $("#UserRole").attr("disabled", false);
                                                                        }
                                                                        onChangeFunction(vRoleID_Frist, '<%= sessLanguageGlobal%>', '<%= anticsrf%>', "0");
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_CSRF)
                                                                    {
                                                                        funCsrfAlert();
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_LOGIN)
                                                                    {
                                                                        RedirectPageLoginNoSess(global_alert_login);
                                                                    }
                                                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                    {
                                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                                    }
                                                                    else if (obj[0].Code === "1")
                                                                    {
                                                                        cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                    }
                                                                    else {
                                                                        funErrorAlert(global_errorsql);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                </script>
                                            </div>
                                            
                                            <%
                                            //} else {
                                            %>
<!--                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" style="display: none;" name="BranchName" id="BranchName" value="<= SessUserAgentID + "#" + SessUserAgentID%>" />
                                                        <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<= SessAgentName%>" class="form-control123"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleAgentName").text(global_fm_Branch);
                                                </script>
                                            </div>-->
                                            <%
                                               // }
                                            %>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleRole" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <%
                                                            if(!"".equals(sBranchIDRoot)) {
                                                        %>
                                                        <select name="UserRole" id="UserRole" class="form-control123" disabled>
                                                            <%
                                                                ROLE[][] rss = new ROLE[1][];
                                                                db.S_BO_ROLE_COMBOBOX(sessLanguageGlobal, rss);
                                                                if (rss[0].length > 0) {
                                                                    for (int i = 0; i < rss[0].length; i++) {
                                                                        if ("".equals(sPermisionFrist)) {
                                                                            sPermisionFrist = Definitions.CONFIG_ROLE_ID_CA_ADMIN;
                                                                        }
                                                        %>
                                                        <option value="<%=Definitions.CONFIG_ROLE_ID_CA_ADMIN%>" <%= String.valueOf(rss[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) ? "selected" : "" %>><%=rss[0][i].REMARK%></option>
                                                        <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                        <%
                                                            } else {
                                                        %>
                                                        <select name="UserRole" id="UserRole" class="form-control123" onchange="onChangeFunction(this.value, '<%= sessLanguageGlobal%>', '<%= anticsrf%>', '0');">
                                                            <%
                                                                ROLE[][] rss = new ROLE[1][];
                                                                try {
                                                                    db.S_BO_ROLE_COMBOBOX(sessLanguageGlobal, rss);
                                                                    if (rss[0].length > 0) {
                                                                        if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                                                if(!"1".equals(sParent_Frist))
                                                                                {
                                                                                    for (int i = 0; i < rss[0].length; i++) {
                                                                                        if (rss[0][i].CA_ENABLED == false) {
                                                                                            if ("".equals(sPermisionFrist)) {
                                                                                                sPermisionFrist = String.valueOf(rss[0][i].ID);
                                                                                            }
                                                                        %>
                                                                        <option value="<%=String.valueOf(rss[0][i].ID)%>"><%=rss[0][i].REMARK%></option>
                                                                        <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    for (int i = 0; i < rss[0].length; i++) {
                                                                                        if (rss[0][i].CA_ENABLED == true) {
                                                                                        if ("".equals(sPermisionFrist)) {
                                                                                            sPermisionFrist = String.valueOf(rss[0][i].ID);
                                                                                        }
                                                                        %>
                                                                        <option value="<%=String.valueOf(rss[0][i].ID)%>"><%=rss[0][i].REMARK%></option>
                                                                        <%
                                                                                    }
                                                                                    }
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                if(!"1".equals(sParent_Frist))
                                                                                {
                                                                                    for (int i = 0; i < rss[0].length; i++) {
                                                                                        if (rss[0][i].CA_ENABLED == false) {
                                                                                            if(!String.valueOf(rss[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
                                                                                            {
                                                                                                if ("".equals(sPermisionFrist)) {
                                                                                                    sPermisionFrist = String.valueOf(rss[0][i].ID);
                                                                                                }
                                                            %>
                                                            <option value="<%=String.valueOf(rss[0][i].ID)%>"><%=rss[0][i].REMARK%></option>
                                                            <%
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    for (int i = 0; i < rss[0].length; i++) {
                                                                                        if (rss[0][i].CA_ENABLED == true) {
                                                                                            if(!String.valueOf(rss[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
                                                                                            {
                                                                                                if ("".equals(sPermisionFrist)) {
                                                                                                    sPermisionFrist = String.valueOf(rss[0][i].ID);
                                                                                                }
                                                            %>
                                                            <option value="<%=String.valueOf(rss[0][i].ID)%>"><%=rss[0][i].REMARK%></option>
                                                            <%
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int i = 0; i < rss[0].length; i++) {
                                                                    if(rss[0][i].CA_ENABLED == false)
                                                                    {
                                                                        if (!String.valueOf(rss[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                                                                        {
                                                                            if ("".equals(sPermisionFrist)) {
                                                                                sPermisionFrist = String.valueOf(rss[0][i].ID);
                                                                            }
                                                            %>
                                                            <option value="<%=String.valueOf(rss[0][i].ID)%>"><%=rss[0][i].REMARK%></option>
                                                            <%
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
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
                                                            %>
                                                        </select>
                                                        <%}%>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRole").text(global_fm_role);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleEmail"></label>
                                                        <label id="idLblNoteEmail"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" maxlength="128" id="Email" name="Email" class="form-control123"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleEmail").text(global_fm_email);
                                                    $("#idLblNoteEmail").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitlePhone"></label>
                                                        <label id="idLblNotePhone"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="MobileNumber" name="MobileNumber" maxlength="16" class="form-control123"
                                                            onblur="autoTrimTextField('MobileNumber', this.value);"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitlePhone").text(global_fm_phone);
                                                    $("#idLblNotePhone").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div id="idViewTokenSSL" class="form-group" style="display: none; padding-top: 10px;">
                                                <label class="control-label123"><script>document.write(global_fm_login_ssl);</script></label>
                                                <br/>
                                                <a id="idAShow" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="addTokenSSL();"><script>document.write(global_fm_choose_cert);</script></a>
                                                <a id="idAHide" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="deleteTokenSSL('<%= anticsrf%>');"><script>document.write(global_fm_unchoose_cert);</script></a>
                                                <div id="idViewTokenCert" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;">
                                                    <fieldset class="scheduler-border">
                                                        <legend class="scheduler-border"><script>document.write(global_group_cert);</script></legend>
                                                        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                                            <label class="control-label123"><script>document.write(global_fm_company);</script></label>
                                                            <textarea id="idCertCompany" class="form-control123" readonly="true" name="idCertCompany" style="height: 120px;"></textarea>
                                                        </div>
                                                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                            <label class="control-label123"><script>document.write(global_fm_issue);</script></label>
                                                            <textarea id="idCertIssuer" class="form-control123" readonly="true" name="idCertIssuer" style="height: 120px;"></textarea>
                                                        </div>
                                                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                            <label class="control-label123"><script>document.write(global_fm_valid);</script></label>
                                                            <input id="idCertValid" name="idCertValid" class="form-control123" type="text" readonly="true"/>
                                                        </div>
                                                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                            <label class="control-label123"><script>document.write(global_fm_Expire);</script></label>
                                                            <input id="idCertExpire" name="idCertExpire" class="form-control123" type="text" readonly="true" />
                                                        </div>
                                                    </fieldset>
                                                </div>
                                                <script>
                                                    function addTokenSSL()
                                                    {
                                                        $.ajax({
                                                            type: "GET",
                                                            url: "../GetCertSSL",
                                                            cache: false,
                                                            success: function (htmlSSL) 
                                                            {
                                                               var vResult = sSpace(htmlSSL).split('#');
                                                               if(vResult[0] === "0")
                                                               {
                                                                   $.ajax({
                                                                        type: "post",
                                                                        url: "../JSONCommon",
                                                                        data: {
                                                                            idParam: 'sslparsecert'
                                                                        },
                                                                        cache: false,
                                                                        success: function (html)
                                                                        {
                                                                            if (html.length > 0)
                                                                            {
                                                                                $("#idViewTokenCert").css("display", "none");
                                                                                $("#idAHide").css("display", "none");
                                                                                $("#idAShow").css("display", "");
                                                                                $("#idCertCompany").val('');
                                                                                $("#idCertIssuer").val('');
                                                                                $("#idCertValid").val('');
                                                                                $("#idCertExpire").val('');
                                                                                var obj = JSON.parse(html);
                                                                                if (obj[0].Code === "0")
                                                                                {
                                                                                    $("#idViewTokenCert").css("display", "");
                                                                                    $("#idAHide").css("display", "");
                                                                                    $("#idAShow").css("display", "none");
                                                                                    $("#idCertCompany").val(obj[0].SUBJECT);
                                                                                    $("#idCertIssuer").val(obj[0].ISSUER);
                                                                                    $("#idCertValid").val(obj[0].DATE_VALID);
                                                                                    $("#idCertExpire").val(obj[0].DATE_EXPIRE);
                                                                                }
                                                                                else if (obj[0].Code === "1")
                                                                                {
                                                                                    funErrorAlert(ca_req_info_cert);
                                                                                }
                                                                                else if (obj[0].Code === JS_EX_LOGIN)
                                                                                {
                                                                                    RedirectPageLoginNoSess(global_alert_login);
                                                                                }
                                                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                                {
                                                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                                                }
                                                                                else
                                                                                {
                                                                                    funErrorAlert(global_errorsql);
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                    return false;
                                                                } else if(vResult[0] === JS_EX_NO_CERTCHAN) {
                                                                    funErrorAlert(global_error_chain_cert);
                                                                } else if(vResult[0] === JS_EX_ERRORCTS) {
                                                                    funErrorAlert(global_error_cert_compare_ca);
                                                                } else {
                                                                   funErrorAlert(global_errorsql);
                                                                }
                                                            },
                                                            timeout: 100000
                                                        });
                                                        return false;
                                                    }
                                                    function deleteTokenSSL(idCSRF)
                                                    {
                                                        swal({
                                                            title: "",
                                                            text: global_ssl_conform_delete,
                                                            imageUrl: "../Images/icon_warning.png",
                                                            imageSize: "45x45",
                                                            showCancelButton: true,
                                                            closeOnConfirm: true,
                                                            allowOutsideClick: false,
                                                            confirmButtonText: login_fm_buton_OK,
                                                            cancelButtonText: global_button_grid_cancel,
                                                            cancelButtonColor: "#199DC0"
                                                        },
                                                        function () {
                                                            $('body').append('<div id="over"></div>');
                                                            $(".loading-gif").show();
                                                            setTimeout(function () {
                                                                $.ajax({
                                                                    type: "post",
                                                                    url: "../UserCommon",
                                                                    data: {
                                                                        idParam: 'deletetokenssl',
                                                                        CsrfToken: idCSRF
                                                                    },
                                                                    cache: false,
                                                                    success: function (html) {
                                                                        var arr = sSpace(html).split('#');
                                                                        if (arr[0] === "0")
                                                                        {
                                                                            $("#idViewTokenCert").css("display", "none");
                                                                            $("#idAHide").css("display", "none");
                                                                            $("#idAShow").css("display", "");
                                                                            $("#idCertCompany").val('');
                                                                            $("#idCertIssuer").val('');
                                                                            $("#idCertValid").val('');
                                                                            $("#idCertExpire").val('');
                                                                        }
                                                                        else if (arr[0] === JS_EX_CSRF) {
                                                                            funCsrfAlert();
                                                                        }
                                                                        else if (arr[0] === JS_EX_LOGIN)
                                                                        {
                                                                            RedirectPageLoginNoSess(global_alert_login);
                                                                        }
                                                                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                                                        {
                                                                            RedirectPageLoginNoSess(global_alert_another_login);
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
                                                            }, JS_STR_ACTION_TIMEOUT);
                                                        });
                                                    }
                                                </script>
                                            </div>
                                            <fieldset class="scheduler-border" id="idViewRoleFunction" style="clear: both;">
                                                <legend class="scheduler-border"><script>document.write(user_title_roleset);</script></legend>
                                                <div class="table-responsive" id="idViewRoleToken">
                                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_token);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox"></ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                        <div class="table-responsive">
                                                            <table id="idTableListToken" class="table table-bordered table-striped projects">
                                                                <thead>
                                                                <th style="width: 50px;"><script>document.write(global_fm_STT);</script></th>
                                                                <th style="width: 350px;"><script>document.write(role_fm_function_name);</script></th>
                                                                <th style="width: 350px;"><script>document.write(global_fm_Description);</script></th>
                                                                <th><script>document.write(global_fm_active);</script></th>
                                                                </thead>
                                                                <tbody id="idTBodyRoleToken"></tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="table-responsive" id="idViewRoleCert">
                                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_cert);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox"></ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                        <div class="table-responsive">
                                                        <table id="idTableListCert" class="table table-bordered table-striped projects">
                                                            <thead>
                                                            <th style="width: 50px;"><script>document.write(global_fm_STT);</script></th>
                                                        <th style="width: 350px;"><script>document.write(role_fm_function_name);</script></th>
                                                        <th style="width: 350px;"><script>document.write(global_fm_Description);</script></th>
                                                            <th><script>document.write(global_fm_active);</script></th>
                                                            </thead>
                                                            <tbody id="idTBodyRoleCert"></tbody>
                                                        </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="table-responsive" id="idViewRoleAnother">
                                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_another);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox"></ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                        <div class="table-responsive">
                                                        <table id="idTableListAnother" class="table table-bordered table-striped projects">
                                                            <thead>
                                                            <th style="width: 50px;"><script>document.write(global_fm_STT);</script></th>
                                                            <th style="width: 350px;"><script>document.write(role_fm_function_name);</script></th>
                                                            <th style="width: 350px;"><script>document.write(global_fm_Description);</script></th>
                                                            <th><script>document.write(global_fm_active);</script></th>
                                                            </thead>
                                                            <tbody id="idTBodyRoleAnother"></tbody>
                                                        </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border" id="idViewUserApprove" style="display: none;clear: both;">
                                                <legend class="scheduler-border"><script>document.write(token_group_request_edit);</script></legend>
                                                <div class="table-responsive">
                                                <table class="table table-bordered table-striped projects" id="mtToken">
                                                    <thead>
                                                    <th><script>document.write(global_fm_STT);</script></th>
                                                    <th><script>document.write(global_fm_Username);</script></th>
                                                    <th><script>document.write(global_fm_fullname);</script></th>
                                                    <th><script>document.write(global_fm_email);</script></th>
                                                    <th><script>document.write(global_fm_date_create);</script></th>
                                                    <th><script>document.write(global_fm_action);</script></th>
                                                    </thead>
                                                    <tbody id="idListUserApprove"></tbody>
                                                </table>
                                                </div>
                                            </fieldset>
                                            <script>
                                                function onChangeFunction(idRole, idLanguage, idCSRF, sFrist)
                                                {
                                                    var isDisabledRoleCA = "";
                                                    if(sFrist === "1")
                                                    {
                                                        if(idRole === JS_STR_ROLE_ADMIN_SUPER_CA && '<%=sBranchIDRoot%>' === JS_STR_AGENCY_ROOT_CA)
                                                        {
                                                            isDisabledRoleCA = "disabled";
                                                        }
                                                    } else {
                                                        if(idRole === JS_STR_ROLE_ADMIN_SUPER_CA && document.myname.BranchName.value.split('#')[0] === JS_STR_AGENCY_ROOT_CA)
                                                        {
                                                            isDisabledRoleCA = "disabled";
                                                        }
                                                    }
                                                    
                                                    if(idRole === JS_STR_ROLE_ADMIN_CA || idRole === JS_STR_ROLE_ADMIN_SUPER_CA)
                                                    {
                                                        // open none -> "" make to SSL show
                                                        $("#idViewTokenSSL").css("display", "none");
                                                    } else {
                                                        $("#idViewTokenSSL").css("display", "none");
                                                    }
                                                    
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadfunctions_forroleadd',
                                                            idRole: idRole,
                                                            idLanguage: idLanguage,
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idTBodyRoleToken").empty();
                                                                    $("#idTBodyRoleCert").empty();
                                                                    $("#idTBodyRoleAnother").empty();
                                                                    var vStringViewCheckbox = "";
                                                                    var contentToken = "";
                                                                    var contentCert = "";
                                                                    var contentAnother = "";
                                                                    var indexToken = 1;
                                                                    var indexCert = 1;
                                                                    var indexAnother = 1;
                                                                    var certAccessEnabled = '<%= certAccessEnabled%>';
                                                                    var userPermissionEnable = "";
                                                                    if('<%=conf.GetPropertybyCode(Definitions.CONFIG_USER_PERMISSION_EDIT_ENABLED)%>' === "0"){
                                                                        userPermissionEnable = "disabled";
                                                                    }
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_TOKEN)
                                                                        {
                                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' checked id='"+idCheckBox+"' "+isDisabledRoleCA+" "+userPermissionEnable+" onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                                            if(obj[i].enabled === "0")
                                                                            {
                                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' id='"+idCheckBox+"' "+isDisabledRoleCA+" "+userPermissionEnable+" onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                                            }
                                                                            contentToken += "<tr>" +
                                                                                    "<td>" + indexToken + "</td>" +
                                                                                    "<td>" + obj[i].name + "</td>" +
                                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                                    "</tr>";
                                                                            indexToken++;
                                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                                        }
                                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_CERT)
                                                                        {
                                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                                            var disableRolePreApprove = "";
                                                                            if(obj[i].disableOption === "1") {
                                                                                disableRolePreApprove = "disabled";
                                                                            }
                                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+ disableRolePreApprove +" "+isDisabledRoleCA+" "+userPermissionEnable+" checked id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+ disableRolePreApprove + " "+userPermissionEnable+"'></div></label>";
                                                                            if(obj[i].enabled === "0")
                                                                            {
                                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+ disableRolePreApprove +" "+isDisabledRoleCA+" "+userPermissionEnable+" id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+ disableRolePreApprove +" "+userPermissionEnable+"'></div></label>";
                                                                            }
                                                                            contentCert += "<tr>" +
                                                                                    "<td>" + indexCert + "</td>" +
                                                                                    "<td>" + obj[i].name + "</td>" +
                                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                                    "</tr>";
                                                                            indexCert++;
                                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                                        }
                                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_ANOTHER)
                                                                        {
                                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' checked id='"+idCheckBox+"' "+isDisabledRoleCA+" "+userPermissionEnable+" onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                                            if(obj[i].enabled === "0")
                                                                            {
                                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' id='"+idCheckBox+"' "+isDisabledRoleCA+" "+userPermissionEnable+" onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                                            }
                                                                            contentAnother += "<tr>" +
                                                                                    "<td>" + indexAnother + "</td>" +
                                                                                    "<td>" + obj[i].name + "</td>" +
                                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                                    "</tr>";
                                                                            indexAnother++;
                                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                                        }
                                                                    }
                                                                    if(contentToken !== "")
                                                                    {
                                                                        $("#idTBodyRoleToken").append(contentToken);
                                                                        $("#idViewRoleToken").css("display", "");
                                                                    } else{
                                                                        $("#idViewRoleToken").css("display", "none");
                                                                    }
                                                                    if(contentCert !== "")
                                                                    {
                                                                        $("#idTBodyRoleCert").append(contentCert);
                                                                        if(certAccessEnabled === "0"){$("#idViewRoleCert").css("display", "none");}
                                                                        else {$("#idViewRoleCert").css("display", "");}
                                                                    } else{
                                                                        $("#idViewRoleCert").css("display", "none");
                                                                    }
                                                                    if(contentAnother !== "")
                                                                    {
                                                                        $("#idTBodyRoleAnother").append(contentAnother);
                                                                        $("#idViewRoleAnother").css("display", "");
                                                                    } else{
                                                                        $("#idViewRoleAnother").css("display", "none");
                                                                    }
                                                                    if(contentToken === "" && contentCert === "" && contentAnother === "")
                                                                    {
                                                                        $("#idViewRoleFunction").css("display", "none");
                                                                    } else{
                                                                        $("#idViewRoleFunction").css("display", "");
                                                                    }
                                                                    if(vStringViewCheckbox !== "")
                                                                    {
                                                                        ViewCheckBox(vStringViewCheckbox);
                                                                    }
                                                                }
                                                                else if (obj[0].Code === JS_EX_CSRF)
                                                                {
                                                                    funCsrfAlert();
                                                                }
                                                                else if (obj[0].Code === JS_EX_LOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_login);
                                                                }
                                                                else if (obj[0].Code === "1")
                                                                {
                                                                    $("#idTBodyRoleToken").empty();
                                                                    $("#idViewRoleToken").css("display", "none");
                                                                    $("#idTBodyRoleCert").empty();
                                                                    $("#idViewRoleCert").css("display", "none");
                                                                    $("#idTBodyRoleAnother").empty();
                                                                    $("#idViewRoleAnother").css("display", "none");
                                                                    $("#idViewRoleFunction").css("display", "none");
                                                                }
                                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
//                                                                $(".loading-gif").hide();
//                                                                $('#over').remove();
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function ViewCheckBox(vvViewCheckbox)
                                                {
                                                    var sStringTemp = "";
                                                    var vItemViewCheckbox = vvViewCheckbox.split('@@@');
                                                    for (var n = 1; n < vItemViewCheckbox.length; n++) {
//                                                        $("#"+vItemViewCheckbox[n]).addClass("js-switch");
                                                        sStringTemp = sStringTemp + "setTimeout(function() {$('#"+vItemViewCheckbox[n]+"').addClass('js-switch');}, 500);";
//                                                        sStringTemp = sStringTemp + "setTimeout(function() {$('#"+vItemViewCheckbox[n]+"').bootstrapSwitch('setState', true);}, 500);";
                                                    }
                                                    eval(sStringTemp);
                                                }
                                                function SetActiveFunction(name, type_chilrent, type, idCSRF)
                                                {
                                                    var strActive = "0";
                                                    if ($("#"+name).is(':checked')) {
                                                        strActive = "1";
                                                    }
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../SomeCommon",
                                                        data: {
                                                            idParam: 'setactiverolefunction',
                                                            name: name,
                                                            attributeType: type_chilrent,
                                                            enabled: strActive,
                                                            type: type,
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            var myStrings = sSpace(html).split('#');
                                                            if (myStrings[0] === "0")
                                                            {
                                                            }
                                                            else if (myStrings[0] === JS_EX_CSRF)
                                                            {
                                                                funCsrfAlert();
                                                            } else if (myStrings[0] === JS_EX_LOGIN)
                                                            {
                                                                RedirectPageLoginNoSess(global_alert_login);
                                                            }
                                                            else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                                            {
                                                                RedirectPageLoginNoSess(global_alert_another_login);
                                                            }
                                                            else
                                                            {
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_USER_APPROVE(idBranch, idCSRF)
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loaduser_approve',
                                                            idBranch: idBranch,
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idListUserApprove").empty();
                                                                    var content = "";
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        content += "<tr>" +
                                                                                "<td>" + obj[i].Index + "</td>" +
                                                                                "<td>" + obj[i].USERNAME + "</td>" +
                                                                                "<td>" + obj[i].FULL_NAME + "</td>" +
                                                                                "<td>" + obj[i].EMAIL + "</td>" +
                                                                                "<td>" + obj[i].CREATED_DT + "</td>" +
                                                                                "<td>" +                                                                                
                                                                                "</td>" +
                                                                                "</tr>";
                                                                    }
                                                                    $("#idListUserApprove").append(content);
                                                                    $("#idViewUserApprove").css("display", "");
                                                                }
                                                                else if (obj[0].Code === JS_EX_CSRF)
                                                                {
                                                                    funCsrfAlert();
                                                                }
                                                                else if (obj[0].Code === JS_EX_LOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_login);
                                                                }
                                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                            </script>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            if('<%= sPermisionFrist%>' !== "")
                            {
                                onChangeFunction('<%= sPermisionFrist%>', '<%= sessLanguageGlobal%>', '<%= anticsrf%>', "1");
                            }
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
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