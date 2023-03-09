<%-- 
    Document   : UserRoleNew
    Created on : Jun 5, 2014, 2:53:01 PM
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
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = role_title_add;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.GroupCode.value))
                {
                    document.myname.GroupCode.focus();
                    funErrorAlert(policy_req_empty + role_fm_code);
                    return false;
                } else {
                    if (JSCheckSpaceField(document.myname.GroupCode.value))
                    {
                        document.myname.GroupCode.focus();
                        funErrorAlert(role_fm_code + global_req_no_space);
                        return false;
                    }
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
                var vIdSessIsChoise = $("#idSessIsChoise").val();
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RoleCommon",
                    data: {
                        idParam: 'addrole',
                        GroupCode: document.myname.GroupCode.value,
                        IsCA: vIdSessIsChoise,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(role_succ_add, "UserRole.jsp");
                        }
                        else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        } else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        } else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
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
//                }
//                else
//                {
//                    funErrorAlert(role_noexists_functions);
//                }
            }
            function onBlurToUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function closeForm()
            {
                funLocationBack("UserRole.jsp");
            }
            function SetActiveFunction(name, type, idCSRF)
            {
//                $('body').append('<div id="over"></div>');
//                $(".loading-gif").show();
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
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body class="nav-md">
    <%
        if ((session.getAttribute("sUserID")) != null) {
            session.setAttribute("sessRoleFunctionsToken",null);
            session.setAttribute("sessRoleFunctionsCert",null);
            session.setAttribute("sessRoleFunctionsAnother",null);
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String sessLanguageGlobal = session.getAttribute("sessVN").toString().trim();
//            String sJSON = "{\"attributes\":[{\"attributeType\":\"TOKEN_ROLE\",\"remarkEn\":\"Role Of Token\",\"remark\":\"Quyền chức năng của Token\",\"attributes\":[{\"enabled\":true,\"name\":\"APPROVED_TOKEN\",\"remarkEn\":\"Approved\",\"remark\":\"Duyệt yêu cầu\"},{\"enabled\":false,\"name\":\"INITIALIZE\",\"remarkEn\":\"Initialize\",\"remark\":\"Khởi tạo\"},{\"enabled\":true,\"name\":\"CHANGE_SOPIN\",\"remarkEn\":\"Change SOPIN\",\"remark\":\"Thay đổi SOPIN\"},{\"enabled\":true,\"name\":\"RESET_ACTIVATION_REMAINING_COUNTER\",\"remarkEn\":\"Reset activation remaining counter of Token\",\"remark\":\"Đặt lại giá trị mặc định cho số lần xác thực của token\"},{\"enabled\":true,\"name\":\"LOCK\",\"remarkEn\":\"Lock\",\"remark\":\"Khóa\"},{\"enabled\":true,\"name\":\"UNLOCK\",\"remarkEn\":\"Unlock\",\"remark\":\"Mở khóa\"},{\"enabled\":false,\"name\":\"LOST\",\"remarkEn\":\"Lost\",\"remark\":\"Báo mất\"},{\"enabled\":true,\"name\":\"PUSH_NOTFICATION\",\"remarkEn\":\"Push notification\",\"remark\":\"Gửi thông báo\"},{\"enabled\":false,\"name\":\"MENU_LINK\",\"remarkEn\":\"Set MenuLink\",\"remark\":\"Hiển thị menulink\"}]},{            \"attributeType\":\"CERRTIFICATE_ROLE\",\"remarkEn\":\"Role Of Certificate\",\"remark\":\"Quyền chức năng của chứng thư số\",\"attributes\":[{\"enabled\":true,\"name\":\"DECLINED\",\"remarkEn\":\"Declined\",\"remark\":\"Huỷ yêu cầu\"},{\"enabled\":true,\"name\":\"PRE_APPROVED\",\"remarkEn\":\"Agency approved\",\"remark\":\"Duyệt đại lý\"},{\"enabled\":true,\"name\":\"APPROVED_CERT\",\"remarkEn\":\"CA approved\",\"remark\":\"Duyệt CA\"},{\"enabled\":true,\"name\":\"ISSUE\",\"remarkEn\":\"New\",\"remark\":\"Cấp mới\"},{\"enabled\":true,\"name\":\"RENEWAL\",\"remarkEn\":\"Renewal\",\"remark\":\"Gia hạn\"},{\"enabled\":true,\"name\":\"CHANGE_INFO\",\"remarkEn\":\"Change-Info\",\"remark\":\"Thay đổi thông tin\"},{\"enabled\":true,\"name\":\"COMPENSATION\",\"remarkEn\":\"Compensate\",\"remark\":\"Cấp bù thời hạn\"},{\"enabled\":true,\"name\":\"REISSUE\",\"remarkEn\":\"Re-issue\",\"remark\":\"Cấp lại\"},{\"enabled\":true,\"name\":\"REVOKE\",\"remarkEn\":\"Revoke\",\"remark\":\"Thu hồi\"}]},{\"attributeType\":\"ANOTHER\",\"remarkEn\":\"Another Role\",\"remark\":\"Các quyền chức năng khác\",\"attributes\":[{\"enabled\":true,\"name\":\"APPROVE_SET_CREATE\",\"remarkEn\":\"Allows the approved to create Request\",\"remark\":\"Cho phép người duyệt được tạo yêu cầu\"}]}]}";
//            SessionRoleFunctions cartToken = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsToken");
//            if (cartToken == null) {
//                cartToken = new SessionRoleFunctions();
//            }
//            SessionRoleFunctions cartCert = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsCert");
//            if (cartCert == null) {
//                cartCert = new SessionRoleFunctions();
//            }
//            SessionRoleFunctions cartAnother = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsAnother");
//            if (cartAnother == null) {
//                cartAnother = new SessionRoleFunctions();
//            }
    %>
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
                        document.getElementById("idNameURL").innerHTML = role_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(role_title_add);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
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
                                            <div class="form-group" style="padding: 0px 0px 5px 0px;margin: 0;">
                                                <label class="radio-inline" style="font-size: 14px;color: #000000;font-weight: bold;">
                                                    <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                                    <script>document.write(role_fm_is_ca);</script>
                                                </label>
                                                <label class="radio-inline" style="font-size: 14px;color: #000000;font-weight: bold;">
                                                    <input type="radio" name="nameCheck" id="nameCheck2">
                                                    <script>document.write(role_fm_is_agent);</script>
                                                </label>
                                                <input type="text" style="display: none;" id="idSessIsChoise" name="idSessIsChoise"/>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idSessIsChoise").val('1');
                                                        $('.radio-inline').on('click', function () {
                                                            var s = $(this).find('input').attr('id');
                                                            if (s === 'nameCheck1')
                                                            {
                                                                $("#idSessIsChoise").val('1');
                                                            }
                                                            if (s === 'nameCheck2')
                                                            {
                                                                $("#idSessIsChoise").val('0');
                                                            }
                                                        });
                                                    });
                                                </script>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(role_fm_code);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" oninput="onBlurToUppercase(this);" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_NAME%>" class="form-control123" id="GroupCode" name="GroupCode" />
                                            </div>
                                            
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_vn);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" class="form-control123" name="Remark" />
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_remark_en);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" class="form-control123" name="Remark_EN" />
                                            </div>
                                            <!--<div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;"></div>-->
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(user_title_roleset);</script></legend>
                                                <style type="text/css">
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .btn{margin-bottom: 0px;}
                                                    .panel_toolbox { min-width: 0;}
                                                </style>
                                                <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                    <label class="control-label123" style="text-transform: uppercase;color: #73879c;"><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_token);</script></label>
                                                </div>
                                                <div class="table-responsive">
                                                    <div class="x_title">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_token);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox">
                                                            
                                                        </ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                    <table id="idTableListToken" class="table table-striped projects">
                                                        <thead>
                                                        <th><script>document.write(global_fm_STT);</script></th>
                                                        <th><script>document.write(role_fm_function_name);</script></th>
                                                        <th><script>document.write(global_fm_Description);</script></th>
                                                        <th><script>document.write(global_fm_active);</script></th>
                                                        </thead>
                                                        <tbody>
                                                            <
                                                                int j=1;
                                                                ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                                                CommonFunction.LoadRoleToken(sJSON, rsToken);
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
                                                                <td><= com.convertMoney(j)%></td>
                                                                <td><= rsRole1.name%></td>
                                                                <
                                                                    if("1".equals(sessLanguageGlobal))
                                                                    {
                                                                %>
                                                                <td><= rsRole1.remark%></td>
                                                                <td style="display: none;"><= rsRole1.remarkEn%></td>
                                                                <
                                                                    } else {
                                                                %>
                                                                <td style="display: none;"><= rsRole1.remark%></td>
                                                                <td><= rsRole1.remarkEn %></td>
                                                                <
                                                                    }
                                                                %>
                                                                <td>
                                                                    <script>
                                                                        var sRequiredHTML = "<input TYPE='checkbox' checked id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN%>', '<= anticsrf%>');\" />";
                                                                        if('<= sEnabled%>' === "0")
                                                                        {
                                                                            sRequiredHTML = "<input TYPE='checkbox' id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN %>', '<= anticsrf%>');\" />";
                                                                        }
                                                                        document.write(sRequiredHTML);
                                                                    </script>
                                                                </td>
                                                            </tr>
                                                            <
                                                                    j++;
                                                                    }
                                                                    session.setAttribute("sessRoleFunctionsToken", cartToken);
                                                                }
                                                            %>
                                                        </tbody>
                                                    </table>
                                                    </div>
                                                </div>
                                                <div class="form-group" style="padding: 10px 0px 10px 0px;margin: 0;">
                                                    <label class="control-label123" style="text-transform: uppercase;color: #73879c;"><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_cert);</script></label>
                                                </div>
                                                <div class="table-responsive">
                                                    <div class="x_title">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_cert);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox">
                                                            
                                                        </ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                        <table id="idTableListCert" class="table table-striped projects">
                                                            <thead>
                                                            <th><script>document.write(global_fm_STT);</script></th>
                                                            <th><script>document.write(role_fm_function_name);</script></th>
                                                            <th><script>document.write(global_fm_Description);</script></th>
                                                            <th><script>document.write(global_fm_active);</script></th>
                                                            </thead>
                                                            <tbody>
                                                                <
                                                                    int n=1;
                                                                    ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                                                    CommonFunction.LoadRoleCertificate(sJSON, rsCert);
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
                                                                    <td><= com.convertMoney(n)%></td>
                                                                    <td><= rsRole1.name%></td>
                                                                    <
                                                                        if("1".equals(sessLanguageGlobal))
                                                                        {
                                                                    %>
                                                                    <td><= rsRole1.remark%></td>
                                                                    <td style="display: none;"><= rsRole1.remarkEn%></td>
                                                                    <
                                                                        } else {
                                                                    %>
                                                                    <td style="display: none;"><= rsRole1.remark%></td>
                                                                    <td><= rsRole1.remarkEn %></td>
                                                                    <
                                                                        }
                                                                    %>
                                                                    <td>
                                                                        <script>
                                                                            var sRequiredHTML = "<input TYPE='checkbox' checked id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<= anticsrf%>');\" />";
                                                                            if('<= sEnabled%>' === "0")
                                                                            {
                                                                                sRequiredHTML = "<input TYPE='checkbox' id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<= anticsrf%>');\" />";
                                                                            }
                                                                            document.write(sRequiredHTML);
                                                                        </script>
                                                                    </td>
                                                                </tr>
                                                                <
                                                                        n++;
                                                                        }
                                                                        session.setAttribute("sessRoleFunctionsCert", cartCert);
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                    <label class="control-label123" style="text-transform: uppercase;color: #73879c;"><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_another);</script></label>
                                                </div>
                                                <div class="table-responsive">
                                                    <div class="x_title">
                                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(user_title_roleset_another);</script></h2>
                                                        <ul class="nav navbar-right panel_toolbox">
                                                            
                                                        </ul>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <div class="x_content">
                                                        <table id="idTableListAnother" class="table table-striped projects">
                                                            <thead>
                                                            <th><script>document.write(global_fm_STT);</script></th>
                                                            <th><script>document.write(role_fm_function_name);</script></th>
                                                            <th><script>document.write(global_fm_Description);</script></th>
                                                            <th><script>document.write(global_fm_active);</script></th>
                                                            </thead>
                                                            <tbody>
                                                                <
                                                                    int m=1;
                                                                    ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                                                    CommonFunction.LoadRoleAnother(sJSON, rsAnother);
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
                                                                    <td><= com.convertMoney(m)%></td>
                                                                    <td><= rsRole1.name%></td>
                                                                    <
                                                                        if("1".equals(sessLanguageGlobal))
                                                                        {
                                                                    %>
                                                                    <td><= rsRole1.remark%></td>
                                                                    <td style="display: none;"><= rsRole1.remarkEn%></td>
                                                                    <
                                                                        } else {
                                                                    %>
                                                                    <td style="display: none;"><= rsRole1.remark%></td>
                                                                    <td><= rsRole1.remarkEn %></td>
                                                                    <
                                                                        }
                                                                    %>
                                                                    <td>
                                                                        <script>
                                                                            var sRequiredHTML = "<input TYPE='checkbox' checked id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<= anticsrf%>');\" />";
                                                                            if('<= sEnabled%>' === "0")
                                                                            {
                                                                                sRequiredHTML = "<input TYPE='checkbox' id='<= rsRole1.name%>' onchange=\"SetActiveFunction('<= rsRole1.name%>', '<= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<= anticsrf%>');\" />";
                                                                            }
                                                                            document.write(sRequiredHTML);
                                                                        </script>
                                                                    </td>
                                                                </tr>
                                                                <
                                                                        m++;
                                                                        }
                                                                        session.setAttribute("sessRoleFunctionsAnother", cartAnother);
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </fieldset>-->
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(user_title_roleset_cert);</script></legend>
                                                <style type="text/css">
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .btn{margin-bottom: 0px;}
                                                    .panel_toolbox { min-width: 0;}
                                                </style>
                                                
                                            </fieldset>-->
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(user_title_roleset_another);</script></legend>
                                                <style type="text/css">
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .btn{margin-bottom: 0px;}
                                                    .panel_toolbox { min-width: 0;}
                                                </style>
                                                
                                            </fieldset>-->
<!--                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(user_title_roleset);</script></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsLock"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_islock);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsUnlock" />&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_isunlock);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsSOPIN"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_issopin);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">  
                                                    <input TYPE="checkbox" id="IsPush"/>&nbsp;&nbsp;<label class="control-labelcheckbox"><script>document.write(funrole_fm_ispush);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsInitialize"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_isinit);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsDynamic"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_isdynamic);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsInformation"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_isinformation);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsActive"/>&nbsp;&nbsp;<label class="control-labelcheckbox"><script>document.write(funrole_fm_isactive);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="EditCertificate"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_editcert);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="IsEnrollCers"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_approvecert);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="DeleteRequest"/>&nbsp;&nbsp;<label class="control-labelcheckbox"><script>document.write(funrole_fm_deleterequest);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="AddRenewal"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_addrenewal);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="DeleteRenewal"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_deleterenewal);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="AccessRenewal"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_accessrenewal);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="ImportRenewal"/>&nbsp;&nbsp;<label class="control-labelcheckbox"><script>document.write(funrole_fm_importrenewal);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="RevokeCert"/>&nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;"><script>document.write(funrole_fm_revoke_cert);</script></label>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <input TYPE="checkbox" id="ExportCert"/>&nbsp;&nbsp;<label class="control-labelcheckbox"><script>document.write(funrole_fm_export_cert);</script></label>
                                                </div>
                                            </fieldset>-->
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script src="../style/jquery.min.js"></script>
                    <script src="../style/bootstrap.min.js"></script>
                    <script src="../style/custom.min.js"></script>
                    <script src="../js/active/highlight.js"></script>
                    <script src="../js/active/bootstrap-switch.js"></script>
                    <script src="../js/active/main.js"></script>
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