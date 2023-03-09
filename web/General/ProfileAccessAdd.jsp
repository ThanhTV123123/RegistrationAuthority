<%-- 
    Document   : ProfileAccessAdd
    Created on : Dec 18, 2019, 1:38:27 PM
    Author     : USER
--%>

<%@page import="vn.ra.process.SessionRoleFunctions"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = profileaccss_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleCode").text(profileaccss_fm_code);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.GroupCode.value))
                {
                    funErrorAlert(policy_req_empty + profileaccss_fm_code);
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
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var sCheckACCESS_PROFILE_ALL = "0";
                if ($("#ACCESS_PROFILE_ALL").is(':checked')) { sCheckACCESS_PROFILE_ALL = "1"; }
                $.ajax({
                    type: "post",
                    url: "../ProfileAccessCommon",
                    data: {
                        idParam: 'addprofileaccess',
                        GroupCode: document.myname.GroupCode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        ACCESS_PROFILE_ALL: sCheckACCESS_PROFILE_ALL,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(profileaccss_succ_add, "ProfileAccessAdd.jsp");
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
                                funErrorAlert(profileaccss_exists_code);
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
                        idSession: 'SessRefreshBranchRole'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "ProfileAccessList.jsp";
                        }
                        else
                        {
                            window.location = "ProfileAccessList.jsp";
                        }
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
    <body class="nav-md">
    <%
        if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                session.setAttribute("SessProfileBranchAccess", null);
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
                        document.getElementById("idNameURL").innerHTML = profileaccss_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(profileaccss_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <input type="button" data-switch-get="state" id="btnSave" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
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
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <label id="idLblTitleCode"></label>
                                                        <label class="CssRequireField" id="idLblNoteCode"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="GroupCode" id="GroupCode" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblNoteCode").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                                        <label id="idLblTitleRemark"></label>
                                                        <label class="CssRequireField" id="idLblNoteRemark"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark" class="form-control123"/>
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
                                                        <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN" class="form-control123"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                String strProfilePolicyDefault = "";
                                                boolean approveCAProfileEnabled = false;
                                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                                if (sessGeneralPolicy[0].length > 0) {
                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                    {
                                                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_CERTIFICATION_PROFILE_PROPERTIES_FOR_BRANCH_ROLE))
                                                        {
                                                            strProfilePolicyDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                        }
                                                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                                        {
                                                            if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                                                approveCAProfileEnabled = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                CERTIFICATION_POLICY_DATA[][] resIPData = null;
//                                                strProfilePolicyDefault = "{\"attributes\":[{\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"PROFILE_LIST\",\"attributes\":[{\"name\":\"CNDN1\",\"remarkEn\":\"CNDN1\",\"remark\":\"Cá nhân thuộc doanh nghiệp 12 tháng\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"CNDN2\",\"remarkEn\":\"CNDN2\",\"remark\":\"Cá nhân thuộc doanh nghiệp 24 tháng\",\"enabled\":false,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"CNDN3\",\"remarkEn\":\"CNDN3\",\"remark\":\"Cá nhân thuộc doanh nghiệp 36 tháng\",\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"DN1\",\"remarkEn\":\"DN1\",\"remark\":\"Doanh nghiệp 12 tháng\",\"enabled\":true,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"DN2\",\"remarkEn\":\"DN2\",\"remark\":\"Doanh nghiệp 24 tháng\",\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"DN3\",\"remarkEn\":\"DN3\",\"remark\":\"Doanh nghiệp 36 tháng\",\"enabled\":false,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"CN2\",\"remarkEn\":\"CN2\",\"remark\":\"Cá nhân 24 tháng\",\"enabled\":true,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"CN01\",\"remarkEn\":\"CN1\",\"remark\":\"Cá nhân 12 tháng\",\"enabled\":true,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"CN3\",\"remarkEn\":\"CN3\",\"remark\":\"Cá nhân 36 tháng\",\"enabled\":true,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"T2DVB21Y_TEST\",\"remarkEn\":\"TEST - Device 1 year\",\"remark\":\"TEST - gói device 1 năm\",\"enabled\":true,\"approveCAEnabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"}]},{\"name\":\"false\",\"remarkEn\":\"Profile All Access\",\"remark\":\"Cho phép truy cập tất cả gói CTS\",\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"PROFILE_ALL_ACCESS\"}]}";
                                                if(!"".equals(strProfilePolicyDefault))
                                                {
                                                    resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                                    CommonFunction.getProfileCertNewListForAdmin(strProfilePolicyDefault, resIPData);
                                                    if(resIPData[0].length > 0)
                                                    {
                                                        request.getSession(false).setAttribute("SessProfileBranchAccess", resIPData);
                                                    }
                                                }
                                            %>
                                            <fieldset class="scheduler-border" style="clear: both;">
                                                <legend class="scheduler-border" id="idLblTitleRoleSet"></legend>
                                                <script>$("#idLblTitleRoleSet").text(profileaccss_fm_rose);</script>
                                                <style type="text/css">
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .table > thead > tr > th{border-bottom: none;}
                                                    .btn{margin-bottom: 0px;}
                                                    .panel_toolbox { min-width: 0;}
                                                </style>
                                                <%
                                                    String displayAccessProfileAll = "none;";
                                                    if(approveCAProfileEnabled == false) {
                                                        displayAccessProfileAll = "";
                                                    }
                                                    String remarkAccessProfileAll = "";
                                                    boolean booAccessProfileAll = false;
                                                    if(resIPData != null && resIPData[0].length > 0) {
                                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                                            {
                                                                if(resIPData1.name.equals("true")) {
                                                                    booAccessProfileAll = true;
                                                                }
                                                                remarkAccessProfileAll = "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn;
                                                            }
                                                        }
                                                    }
                                                %>
                                                <div class="form-group" style="text-align: right; padding: 0px 0px 10px 0px;margin: 0;">
                                                    <div class="col-sm-6" style="padding-left: 0;text-align: left;">
                                                        <div class="form-group" style="display: <%= displayAccessProfileAll%>">
                                                            <label id="idLblTitleACCESS_PROFILE_ALL" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                                            <div class="col-sm-6" style="padding-right: 0px;text-align: left;">
                                                                <label class="switch" for="ACCESS_PROFILE_ALL">
                                                                    <input TYPE="checkbox" id="ACCESS_PROFILE_ALL" name="ACCESS_PROFILE_ALL" <%= booAccessProfileAll == true ? "checked" : ""%> />
                                                                    <div class="slider round" id="ACCESS_PROFILE_ALLClass"></div>
                                                                </label>
                                                            </div>
                                                        </div>
                                                        <script>$("#idLblTitleACCESS_PROFILE_ALL").text('<%=remarkAccessProfileAll%>');</script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;text-align: right;">
                                                        <label id="idLblTitleLoadProfile" class="control-label" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                                        <a style="cursor: pointer;color: blue; font-weight: bold; text-decoration: underline;" onclick="LoadProfileAll('1', '0');" id="idLoadProfile"></a>
                                                        <script>
                                                            $("#idLoadProfile").text(error_content_link_out);
                                                            $("#idLblTitleLoadProfile").text(global_fm_button_reload_profile);
                                                        </script>
                                                    </div>
                                                </div>
                                                <div class="table-responsive">
                                                    <table id="tblCertUseIP" class="table table-bordered table-striped projects">
                                                        <thead>
                                                        <th id="idLblTitleTableIPSTT"></th>
                                                        <th id="idLblTitleTableIPName"></th>
                                                        <th id="idLblTitleTableIPProfileRemark"></th>
                                                        <th id="idLblTitleTableIPPercent"></th>
                                                        <th id="idLblTitleTableIPActive"></th>
                                                        <script>
                                                            $("#idLblTitleTableIPSTT").text(global_fm_STT);
                                                            $("#idLblTitleTableIPName").text(certprofile_fm_code);
                                                            $("#idLblTitleTableIPProfileRemark").text(global_fm_Description);
                                                            $("#idLblTitleTableIPPercent").text(global_fm_active);
                                                            $("#idLblTitleTableIPActive").text(global_fm_approve_ca);
                                                        </script>
                                                        </thead>
                                                        <tbody id="idTemplateAssignIP">
                                                            <%
                                                                int j=1;
                                                                boolean hasDataRecord = false;
                                                                if(resIPData != null && resIPData[0].length > 0) {
                                                                    for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                                        if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                                        {
                                                                            hasDataRecord = true;
                                                            %>
                                                            <tr>
                                                                <td><%= j%></td>
                                                                <td><%= resIPData1.name %></td>
                                                                <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                                <td>
                                                                    <label class="switch" for="idCheckProfile<%=j%>" style="margin-bottom: 0;">
                                                                        <input onclick="onProfileActive('<%=resIPData1.name%>', 'idCheckProfile<%=j%>');" type="checkbox"
                                                                            name="idCheckProfile<%=j%>" id="idCheckProfile<%=j%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                                        <div id="idCheckProfileClass<%=j%>" class="slider round"></div>
                                                                    </label>
                                                                </td>
                                                                <td>
                                                                    <label class="switch" for="idCheckApproveCA<%=j%>" style="margin-bottom: 0;">
                                                                        <input onclick="onApproveCAActive('<%=resIPData1.name%>', 'idCheckApproveCA<%=j%>');" type="checkbox"
                                                                            name="idCheckApproveCA<%=j%>" id="idCheckApproveCA<%=j%>" <%= resIPData1.approveCAEnabled == true ? "checked" : ""%>/>
                                                                        <div id="idCheckApproveCAClass<%=j%>" class="slider round"></div>
                                                                    </label>
                                                                </td>
                                                            </tr>
                                                            <%
                                                                        j++;
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                            <%
                                                                if(hasDataRecord == false)
                                                                {
                                                            %>
                                                            <tr><td colspan="5" id="idLblTitleTableNoFile"></td></tr>
                                                                <script>
                                                                    $("#idLblTitleTableNoFile").text(global_no_data);
                                                                </script>
                                                            <%
                                                                }
                                                            %>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <div id="greenIP" style="margin: 5px 0 5px 0;"> </div>
                                                <script>
                                                    if (parseInt('<%=j%>')-1 > 0)
                                                    {
                                                        $('#greenIP').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                                    }
                                                    function LoadProfileAll(sAllEnabled, hasBranchRole)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        setTimeout(function () {
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadprofileallproperties',
                                                                    sAllEnabled: sAllEnabled,
                                                                    hasBranchRole: hasBranchRole
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var obj = JSON.parse(html);
                                                                        console.log(obj[0].Code);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            $("#idTemplateAssignIP").empty();
                                                                            var contentProps = "";
                                                                            var sCount = 1;
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                if(obj[i].ATTRIBUTE_TYPE === 'PROFILE_LIST/ITEM')
                                                                                {
                                                                                    var idCheckBoxProfile = 'idCheckProfile' + obj[i].NO;
                                                                                    var idCheckBoxApprove = 'idCheckApproveCA' + obj[i].NO;
                                                                                    var isCheckedProfile = "";
                                                                                    var isCheckedApprove = "";
                                                                                    if(obj[i].ACTIVE === "1")
                                                                                    {
                                                                                        isCheckedProfile = "checked";
                                                                                    }
                                                                                    if(obj[i].APPROVE_CA === "1")
                                                                                    {
                                                                                        isCheckedApprove = "checked";
                                                                                    }
                                                                                    var sActiveProfile = "<label class='switch' for='"+idCheckBoxProfile+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedProfile+" id='"+idCheckBoxProfile+"' onchange=\"onProfileActive('"+obj[i].NAME+"', '"+idCheckBoxProfile+"');\" /><div class='slider round'></div></label>";
                                                                                    var sActiveApproveCA = "<label class='switch' for='"+idCheckBoxApprove+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedApprove+" id='"+idCheckBoxApprove+"' onchange=\"onApproveCAActive('"+obj[i].NAME+"', '"+idCheckBoxApprove+"');\" /><div class='slider round'></div></label>";
                                                                                    contentProps += "<tr>" +
                                                                                        "<td>" + sCount + "</td>" +
                                                                                        "<td>" + obj[i].NAME + "</td>" +
                                                                                        "<td>" + obj[i].REMARK + "</td>" +
                                                                                        "<td>" + sActiveProfile + "</td>" +
                                                                                        "<td>" + sActiveApproveCA + "</td>" +
                                                                                        "</tr>";
                                                                                    sCount++;
                                                                                }
                                                                            }
                                                                            $("#idTemplateAssignIP").append(contentProps);
                                                                            if (sCount > 0)
                                                                            {
                                                                                $('#greenIP').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                                                            }
                                                                        } else if (obj[0].Code === "1") {

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
                                                                        $(".loading-gif").hide();
                                                                        $('#over').remove();
                                                                    }
                                                                }
                                                            });
                                                            return false;
                                                        }, 1000);
                                                    }
                                                    function onProfileActive(idName, idCheckProfie)
                                                    {
                                                        var sProfileActive = "0";
                                                        if ($("#"+idCheckProfie).is(':checked')) {
                                                            sProfileActive = "1";
                                                        }
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ProfileAccessCommon",
                                                            data: {
                                                                idParam: 'activeprofileaccess',
                                                                idName: idName,
                                                                sProfileActive: sProfileActive
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    //LoadProfileAll('0','0');
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
                                                                    if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                        funErrorAlert(global_error_login_info);
                                                                    }
                                                                    else {
                                                                        funErrorAlert(global_errorsql);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                    function onApproveCAActive(idName, idCheckApproveCA)
                                                    {
                                                        var sProfileActive = "0";
                                                        if ($("#"+idCheckApproveCA).is(':checked'))
                                                        {
                                                            sProfileActive = "1";
                                                        }
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ProfileAccessCommon",
                                                            data: {
                                                                idParam: 'activeapproveaccess',
                                                                idName: idName,
                                                                sProfileActive: sProfileActive
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    //LoadProfileAll('0','0');
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
                                                                    if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                        funErrorAlert(global_error_login_info);
                                                                    }
                                                                    else {
                                                                        funErrorAlert(global_errorsql);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                    function onApproveCARequestActive(idName, idCheckApproveCA)
                                                    {
                                                        var sProfileActive = "0";
                                                        if ($("#"+idCheckApproveCA).is(':checked'))
                                                        {
                                                            sProfileActive = "1";
                                                        }
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ProfileAccessCommon",
                                                            data: {
                                                                idParam: 'activeapproveaccess',
                                                                idName: idName,
                                                                sProfileActive: sProfileActive
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
//                                                                    LoadProfileAll('0','0');
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
                                                                    if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                        funErrorAlert(global_error_login_info);
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
                                            </fieldset>
                                            <fieldset class="scheduler-border" style="clear: both;display: <%= approveCAProfileEnabled ? "" : "none" %>">
                                                <legend class="scheduler-border" id="idLblTitleRequestType"></legend>
                                                <script>$("#idLblTitleRequestType").text(profileaccss_fm_service_type);</script>
                                                <style type="text/css">
                                                    .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                                    .table > thead > tr > th{border-bottom: none;}
                                                    .btn{margin-bottom: 0px;}
                                                    .panel_toolbox { min-width: 0;}
                                                </style>
                                                <div class="table-responsive">
                                                    <table id="tblCertUseRequest" class="table table-bordered table-striped projects">
                                                        <thead>
                                                        <th id="idLblTitleTableRequestSTT"></th>
                                                        <th id="idLblTitleTableRequestName"></th>
                                                        <th id="idLblTitleTableRequestProfileRemark"></th>
                                                        <th id="idLblTitleTableRequestPercent" style="display: none;"></th>
                                                        <th id="idLblTitleTableRequestActive"></th>
                                                        <script>
                                                            $("#idLblTitleTableRequestSTT").text(global_fm_STT);
                                                            $("#idLblTitleTableRequestName").text(cert_fm_type_request);
                                                            $("#idLblTitleTableRequestProfileRemark").text(global_fm_Description);
                                                            $("#idLblTitleTableRequestPercent").text(global_fm_active);
                                                            $("#idLblTitleTableRequestActive").text(global_fm_approve_ca);
                                                        </script>
                                                        </thead>
                                                        <tbody id="idTemplateAssignRequest">
                                                            <%
                                                                int n=1;
                                                                boolean hasRequestRecord = false;
                                                                if(resIPData != null && resIPData[0].length > 0) {
                                                                    for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                                        if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE))
                                                                        {
                                                                            hasRequestRecord = true;
                                                            %>
                                                            <tr>
                                                                <td><%= n%></td>
                                                                <td><%= resIPData1.name %></td>
                                                                <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                                <td style="display: none;">
                                                                    <label class="switch" for="idCheckProfileRequest<%=n%>" style="margin-bottom: 0;">
                                                                        <input type="checkbox" name="idCheckProfileRequest<%=n%>" id="idCheckProfileRequest<%=n%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                                        <div id="idCheckProfileClassRequest<%=n%>" class="slider round"></div>
                                                                    </label>
                                                                </td>
                                                                <td>
                                                                    <label class="switch" for="idCheckApproveCARequest<%=n%>" style="margin-bottom: 0;">
                                                                        <input onclick="onApproveCARequestActive('<%=resIPData1.name%>', 'idCheckApproveCARequest<%=n%>');" type="checkbox"
                                                                            name="idCheckApproveCARequest<%=n%>" id="idCheckApproveCARequest<%=n%>" <%= resIPData1.approveCAEnabled == true ? "checked" : ""%>/>
                                                                        <div id="idCheckApproveCARequestClass<%=n%>" class="slider round"></div>
                                                                    </label>
                                                                </td>
                                                            </tr>
                                                            <%
                                                                        n++;
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                            <%
                                                                if(hasRequestRecord == false)
                                                                {
                                                            %>
                                                            <tr><td colspan="5" id="idLblTitleTableRequestNoFile"></td></tr>
                                                                <script>
                                                                    $("#idLblTitleTableRequestNoFile").text(global_no_data);
                                                                </script>
                                                            <%
                                                                }
                                                            %>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </fieldset>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
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