<%-- 
    Document   : UserRoleEdit
    Created on : Jun 5, 2014, 2:52:40 PM
    Author     : Thanh
--%>

<%@page import="vn.ra.process.SessionRoleFunctions"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = role_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(role_title_edit);
                $("#idLblTitleCode").text(role_fm_code);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                $("#idLblTitleRoleSet").text(user_title_roleset);
                $("#idLblTitleRoleSetToken").text(user_title_roleset_token);
                $("#idLblTitleTableTokenSST").text(global_fm_STT);
                $("#idLblTitleTableTokenName").text(role_fm_function_name);
                $("#idLblTitleTableTokenDes").text(global_fm_Description);
                $("#idLblTitleTableTokenActive").text(global_fm_active);
                $("#idLblTitleRoleSetCert").text(user_title_roleset_cert);
                $("#idLblTitleTableCertSST").text(global_fm_STT);
                $("#idLblTitleTableCertName").text(role_fm_function_name);
                $("#idLblTitleTableCertDes").text(global_fm_Description);
                $("#idLblTitleTableCertActive").text(global_fm_active);
                $("#idLblTitleRoleSetOther").text(user_title_roleset_another);
                $("#idLblTitleTableOtherSST").text(global_fm_STT);
                $("#idLblTitleTableOtherName").text(role_fm_function_name);
                $("#idLblTitleTableOtherDes").text(global_fm_Description);
                $("#idLblTitleTableOtherActive").text(global_fm_active);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
//                if(localStorage.getItem("LOCAL_PARAM_USERROLELIST") !== null && localStorage.getItem("LOCAL_PARAM_USERROLELIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_USERROLELIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "UserRole.jsp";
//                }
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.GroupCode.value))
                {
                    funErrorAlert(policy_req_empty + role_fm_code);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark_EN.value))
                {
                    document.myname.Remark_EN.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_en);
                    return false;
                }
                var sCheckActiveFlag = "0";
                if ($("#ActiveFlag").is(':checked')) { sCheckActiveFlag = "1"; }
                var sCheckAllUser = "0";
                if ($("#applyUserAll").is(':checked')) { sCheckAllUser = "1"; }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RoleCommon",
                    data: {
                        idParam: 'editrole',
                        id: document.myname.GroupID.value,
                        GroupCode: document.myname.GroupCode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        ActiveFlag: sCheckActiveFlag,
                        checkAllUser: sCheckAllUser,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_USERROLE", document.myname.GroupID.value);
                            funSuccAlert(role_succ_edit, "UserRole.jsp");
                        }
                        else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        }
                        else if (myStrings[0] === JS_EX_CSRF) {
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
                            if (myStrings[1] === "1") {
                                funErrorAlert(role_exists_code);
                            } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
                            }
                            else
                            {
                                funErrorAlert(global_errorsql);
                            }
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
                        idSession: 'SessRefreshRole'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "UserRole.jsp";
                        }
                        else
                        {
                            window.location = "UserRole.jsp";
                        }
                    }
                });
                return false;
            }
            function SetActiveFunction(name, type_chilrent, type, idCSRF)
            {
//                $('body').append('<div id="over"></div>');
//                $(".loading-gif").show();
//                var strActive = "0";
//                if ($("#"+name).is(':checked')) {
//                    strActive = "1";
//                }
                var strActive = "0";
                if ($("#"+type_chilrent.replace('/','') +'_'+name).is(':checked')) {
                    strActive = "1";
                }
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'setactiverolefunction',
                        name: name,
                        enabled: strActive,
                        attributeType: type_chilrent,
                        type: type,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            //funSuccAlert(relyparty_succ_edit, "RelyingPartyList.jsp");
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
//                        $(".loading-gif").hide();
//                        $('#over').remove();
                    }
                });
                return false;
            }
        </script>
        <style>
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body>
    <%
        String anticsrf = "" + Math.random();
        request.getSession().setAttribute("anticsrf", anticsrf);
        session.setAttribute("sessRoleFunctionsToken",null);
        session.setAttribute("sessRoleFunctionsCert",null);
        session.setAttribute("sessRoleFunctionsAnother",null);
    %>
    <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
         left: 0; height: 100%;" class="loading-gif">
        <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
    </div>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input type="button" data-switch-get="state" id="btnSave" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                        document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <%
                ROLE[][] rs = new ROLE[1][];
                try {
                    String ids = request.getParameter("id");
//                    ids = seEncript.decrypt(ids);
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString().trim();
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_ROLE_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                        if(rs[0].length > 0) {
                            String strRemark = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                            String strRemark_EN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                            String strCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String sJSON_FUNCTION = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                            SessionRoleFunctions cartToken = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsToken");
                            if (cartToken == null) {
                                cartToken = new SessionRoleFunctions();
                            }
                            SessionRoleFunctions cartCert = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsCert");
                            if (cartCert == null) {
                                cartCert = new SessionRoleFunctions();
                            }
                            SessionRoleFunctions cartAnother = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsAnother");
                            if (cartAnother == null) {
                                cartAnother = new SessionRoleFunctions();
                            }
            %>
            <form name="myname" method="post" class="form-horizontal">
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly="true" name="GroupID" style="display: none;" value="<%= rs[0][0].ID %>" />
                            <input type="text" readonly="true" name="GroupCode" class="form-control123" value="<%= strCode%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark"></label>
                            <label class="CssRequireField" id="idLblNoteRemark"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="256" name="Remark" class="form-control123" value="<%= strRemark%>"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark_EN"></label>
                            <label class="CssRequireField" id="idLblNoteRemark_EN"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="256" name="Remark_EN" class="form-control123" value="<%= strRemark_EN%>"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ActiveFlag">
                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                <div class="slider round"></div>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <div class="form-group">
                        <div class="col-sm-1" style="padding-right: 0px;">
                            <label class="switch" for="applyUserAll">
                                <input TYPE="checkbox" id="applyUserAll" name="applyUserAll" />
                                <div class="slider round"></div>
                            </label>
                        </div>
                        <label id="idLblTitleApplyUserAll" class="control-label col-sm-10" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <script>$("#idLblTitleApplyUserAll").text(global_fm_all_apply_user);</script>
                    </div>
                </div>
                <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;"></div>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleRoleSet"></legend>
                    <style type="text/css">
                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                        .table > thead > tr > th{border-bottom: none;}
                        .btn{margin-bottom: 0px;}
                        .panel_toolbox { min-width: 0;}
                    </style>
                    <%
                        ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                        CommonFunction.LoadRoleToken(sJSON_FUNCTION, rsToken);
                        String sDisableToken = "none";
                        if(rsToken[0] != null)
                        {
                            if (rsToken[0].length > 0) {
                                sDisableToken = "";
                            }
                        }
                    %>
                    <div class="table-responsive" style="display: <%= sDisableToken%>">
                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetToken" style="color: #36526D;"></span></h2>
                            <ul class="nav navbar-right panel_toolbox">

                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <div class="table-responsive">
                                <table id="idTableListToken" class="table table-bordered table-striped projects">
                                    <thead>
                                    <th style="width: 50px;" id="idLblTitleTableTokenSST"></th>
                                    <th style="width: 350px;" id="idLblTitleTableTokenName"></th>
                                    <th style="width: 350px;" id="idLblTitleTableTokenDes"></th>
                                    <th id="idLblTitleTableTokenActive"></th>
                                    </thead>
                                    <tbody>
                                        <%
                                            int j=1;
                                            if(rsToken[0] != null)
                                            {
                                            if (rsToken[0].length > 0) {
                                            for (ROLE_DATA rsRole1 : rsToken[0]) {
                                                String sEnabled = "1";
                                                if(rsRole1.enabled == false)
                                                {
                                                    sEnabled = "0";
                                                }
                                                cartToken.AddRoleFunctionsList(rsRole1);
                                        %>
                                        <tr>
                                            <td><%= com.convertMoney(j)%></td>
                                            <td><%= rsRole1.name%></td>
                                            <%
                                                if("1".equals(sessLanguageGlobal))
                                                {
                                            %>
                                            <td><%= rsRole1.remark%></td>
                                            <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                            <%
                                                } else {
                                            %>
                                            <td style="display: none;"><%= rsRole1.remark%></td>
                                            <td><%= rsRole1.remarkEn %></td>
                                            <%
                                                }
                                            %>
                                            <td>
                                                <div id="idCheckTableTokenCert<%=j%>"></div>
                                                <script>
                                                    var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN%>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                    if('<%= sEnabled%>' === "0")
                                                    {
                                                        sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN %>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                    }
                                                    $("#idCheckTableTokenCert"+'<%=j%>').append(sRequiredHTML);
                                                </script>
                                            </td>
                                        </tr>
                                        <%
                                                j++;
                                                }
                                                session.setAttribute("sessRoleFunctionsToken", cartToken);
                                                }
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <%
                        Config conf = new Config();
                        String certAccessEnabled = conf.GetPropertybyCode(Definitions.CONFIG_CERT_MODULES_ACCESS_ENABLED);
                        ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                        CommonFunction.LoadRoleCertificate(sJSON_FUNCTION, rsCert);
                        String sDisableCert = "none";
                        if("1".equals(certAccessEnabled)) {
                            if(rsCert[0] != null)
                            {
                                if (rsCert[0].length > 0) {
                                    sDisableCert = "";
                                }
                            }
                        }
                    %>
                    <div class="table-responsive" style="display: <%= sDisableCert%>">
                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetCert" style="color: #36526D;"></span></h2>
                            <ul class="nav navbar-right panel_toolbox">

                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <div class="table-responsive">
                            <table id="idTableListCert" class="table table-bordered table-striped projects">
                                <thead>
                                <th style="width: 50px;" id="idLblTitleTableCertSST"></th>
                                <th style="width: 350px;" id="idLblTitleTableCertName"></th>
                                <th style="width: 350px;" id="idLblTitleTableCertDes"></th>
                                <th id="idLblTitleTableCertActive"></th>
                                </thead>
                                <tbody>
                                    <%
                                        int n=1;
                                        if(rsCert[0] != null)
                                        {
                                        if (rsCert[0].length > 0) {
                                        for (ROLE_DATA rsRole1 : rsCert[0]) {
                                            String sEnabled = "1";
                                            if(rsRole1.enabled == false)
                                            {
                                                sEnabled = "0";
                                            }
                                            cartCert.AddRoleFunctionsList(rsRole1);
                                    %>
                                    <tr>
                                        <td><%= com.convertMoney(n)%></td>
                                        <td><%= rsRole1.name%></td>
                                        <%
                                            if("1".equals(sessLanguageGlobal))
                                            {
                                        %>
                                        <td><%= rsRole1.remark%></td>
                                        <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                        <%
                                            } else {
                                        %>
                                        <td style="display: none;"><%= rsRole1.remark%></td>
                                        <td><%= rsRole1.remarkEn %></td>
                                        <%
                                            }
                                        %>
                                        <td>
                                            <div id="idCheckTableCert<%=n%>"></div>
                                            <script>
                                                var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                if('<%= sEnabled%>' === "0")
                                                {
                                                    sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                }
                                                $("#idCheckTableCert"+'<%=n%>').append(sRequiredHTML);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                        n++;
                                        }
                                        session.setAttribute("sessRoleFunctionsCert", cartCert);
                                        }
                                        }
                                    %>
                                </tbody>
                            </table>
                            </div>
                        </div>
                    </div>
                    <%
                        ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                        CommonFunction.LoadRoleAnother(sJSON_FUNCTION, rsAnother);
                        String sDisableAnother = "none";
                        if(rsAnother[0] != null)
                        {
                            if (rsAnother[0].length > 0) {
                                sDisableAnother = "";
                            }
                        }
                    %>
                    <div class="table-responsive" style="display: <%= sDisableAnother%>">
                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetOther" style="color: #36526D;"></span></h2>
                            <ul class="nav navbar-right panel_toolbox">

                            </ul>
                            <div class="clearfix"></div>
                        </div>
                        <div class="x_content">
                            <div class="table-responsive">
                            <table id="idTableListAnother" class="table table-bordered table-striped projects">
                                <thead>
                                <th style="width: 50px;" id="idLblTitleTableOtherSST"></th>
                                <th style="width: 350px;" id="idLblTitleTableOtherName"></th>
                                <th style="width: 350px;" id="idLblTitleTableOtherDes"></th>
                                <th id="idLblTitleTableOtherActive"></th>
                                </thead>
                                <tbody>
                                    <%
                                        int m=1;
                                        if(rsAnother[0] != null)
                                        {
                                        if (rsAnother[0].length > 0) {
                                        for (ROLE_DATA rsRole1 : rsAnother[0]) {
                                            String sEnabled = "1";
                                            if(rsRole1.enabled == false)
                                            {
                                                sEnabled = "0";
                                            }
                                            cartAnother.AddRoleFunctionsList(rsRole1);
                                    %>
                                    <tr>
                                        <td><%= com.convertMoney(m)%></td>
                                        <td><%= rsRole1.name%></td>
                                        <%
                                            if("1".equals(sessLanguageGlobal))
                                            {
                                        %>
                                        <td><%= rsRole1.remark%></td>
                                        <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                        <%
                                            } else {
                                        %>
                                        <td style="display: none;"><%= rsRole1.remark%></td>
                                        <td><%= rsRole1.remarkEn %></td>
                                        <%
                                            }
                                        %>
                                        <td>
                                            <div id="idCheckTableOtherCert<%=m%>"></div>
                                            <script>
                                                var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                if('<%= sEnabled%>' === "0")
                                                {
                                                    sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<%= anticsrf%>');\" /><div class='slider round'></div></label>";
                                                }
                                                $("#idCheckTableOtherCert"+'<%=m%>').append(sRequiredHTML);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                        m++;
                                        }
                                        session.setAttribute("sessRoleFunctionsAnother", cartAnother);
                                        }
                                        }
                                    %>
                                </tbody>
                            </table>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strDateLimit" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateDate" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>" />
                        </div>
                    </div>
                </div>
            </form>
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
        </div>
        <script src="../style/jquery.min.js"></script>
       <script src="../style/bootstrap.min.js"></script>
        <!-- <script src="../style/custom.min.js"></script>-->
<!--        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>-->
    </body> 
</html>