<%-- 
    Document   : ProfileAccessEdit
    Created on : Dec 18, 2019, 1:47:45 PM
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = role_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(profileaccss_title_edit);
                $("#idLblTitleCode").text(profileaccss_fm_code);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
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
                var sCheckAPPLY_ALL = "0";
                if ($("#APPLY_ALL").is(':checked')) { sCheckAPPLY_ALL = "1"; }
                var sCheckACCESS_PROFILE_ALL = "0";
                if ($("#ACCESS_PROFILE_ALL").is(':checked')) { sCheckACCESS_PROFILE_ALL = "1"; }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../ProfileAccessCommon",
                    data: {
                        idParam: 'editprofileaccess',
                        id: document.myname.GroupID.value,
                        GroupCode: document.myname.GroupCode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        ActiveFlag: sCheckActiveFlag,
                        APPLY_ALL: sCheckAPPLY_ALL,
                        ACCESS_PROFILE_ALL: sCheckACCESS_PROFILE_ALL,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(profileaccss_succ_edit, "ProfileAccessList.jsp");
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
                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
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
    <body>
    <%
        String anticsrf = "" + Math.random();
        request.getSession().setAttribute("anticsrf", anticsrf);
        session.setAttribute("SessProfileBranchAccess",null);
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
                BRANCH_ROLE[][] rs = new BRANCH_ROLE[1][];
                try {
                    String ids = request.getParameter("id");
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString().trim();
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_BRANCH_ROLE_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                        if(rs[0].length > 0) {
                            String strRemark = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                            String strRemark_EN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                            String strCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String sJSON_FUNCTION = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                            if(!"".equals(sJSON_FUNCTION))
                            {
                                CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getProfileCertNewListForAdmin(sJSON_FUNCTION, resIPData);
                                if(resIPData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessProfileBranchAccess", resIPData);
                                }
                            }
                            boolean approveCAProfileEnabled = false;
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                    {
                                        if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                            approveCAProfileEnabled = true;
                                        }
                                        break;
                                    }
                                }
                            }
                            CERTIFICATION_POLICY_DATA[][] resIPData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfileBranchAccess");
            %>
            <form name="myname" method="post" class="form-horizontal">
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly="true" name="GroupID" style="display: none;" value="<%= rs[0][0].ID%>" />
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
                            <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" name="Remark" class="form-control123" value="<%= strRemark%>"/>
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
                            <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" name="Remark_EN" class="form-control123" value="<%= strRemark_EN%>"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ActiveFlag">
                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%= rs[0][0].ENABLED ? "checked" : "" %> />
                                <div class="slider round"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleActiveFlag").text(global_fm_active);</script>
                </div>
                <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;"></div>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleRoleAgentList"></legend>
                    <script>$("#idLblTitleRoleAgentList").text(profileaccss_fm_agency);</script>
                    <style type="text/css">
                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                        .table > thead > tr > th{border-bottom: none;}
                        .btn{margin-bottom: 0px;}
                        .panel_toolbox { min-width: 0;}
                    </style>
                    <div class="col-sm-6" style="padding-left: 0;text-align: left;">
                        <div class="form-group">
                            <label id="idLblTitleAPPLY_ALL" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                <label class="switch" for="APPLY_ALL">
                                    <input TYPE="checkbox" id="APPLY_ALL" name="APPLY_ALL" />
                                    <div class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>$("#idLblTitleAPPLY_ALL").text(profileaccss_apply_profile_agency);</script>
                    </div>
                    <div class="table-responsive" style="clear: both;">
                        <%
                            int jAgency=1;
                            BRANCH[][] rsBranch = new BRANCH[1][];
                            db.S_BO_BRANCH_ROLE_LIST_BRANCH(String.valueOf(rs[0][0].ID), Integer.parseInt(sessLanguageGlobal), rsBranch);
                        %>
                        <table id="tblCertAgency" class="table table-bordered table-striped projects">
                            <thead>
                            <th id="idLblTitleTableAgentSTT"></th>
                            <th id="idLblTitleTableAgentName"></th>
                            <th id="idLblTitleTableAgentRemark"></th>
                            <th id="idLblTitleTableAgentState"></th>
                            <script>
                                $("#idLblTitleTableAgentSTT").text(global_fm_STT);
                                $("#idLblTitleTableAgentName").text(branch_fm_code);
                                $("#idLblTitleTableAgentRemark").text(branch_fm_name);
                                $("#idLblTitleTableAgentState").text(global_fm_Status);
                            </script>
                            </thead>
                            <tbody>
                                <%
                                    if(rsBranch[0] != null && rsBranch[0].length > 0) {
                                        for(BRANCH resBranch : rsBranch[0]) {
                                %>
                                <tr>
                                    <td><%= jAgency%></td>
                                    <td><%= resBranch.NAME %></td>
                                    <td><%= resBranch.REMARK %></td>
                                    <td><%= resBranch.BRANCH_STATE_DESC %></td>
                                </tr>
                                <%
                                            jAgency++;
                                        }
                                    } else {
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
                        <div id="greenAgency" style="margin: 5px 0 5px 0;"> </div>
                        <script>
                            if (parseInt('<%=jAgency%>')-1 > 0)
                            {
                                $('#greenAgency').smartpaginator({totalrecords: parseInt('<%=jAgency%>')-1, recordsperpage: 10, datacontainer: 'tblCertAgency', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                            }
                        </script>
                    </div>
                </fieldset>
                <div class="clearfix"></div>
                <div class="" role="tabpanel" data-example-id="togglable-tabs">
                    <ul id="myTabTypeKey" class="nav nav-tabs bar_tabs" role="tablist">
                        <li role="presentation" class="active" id="idLi_contentAPI">
                            <a href="#tab_contentAPI" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="true">
                                <span id="idTagCredentialAPI"></span>
                            </a>
                        </li>
                        <li role="presentation" class="" id="idLi_contentIP" id="idTagIP">
                            <a href="#tab_contentIP" id="home-tab" role="tab" data-toggle="tab" aria-expanded="false">
                                <span id="idTagIP"></span>
                            </a>
                        </li>
                        <li role="presentation" class="" id="idLi_contentFunction" id="idTagFunction">
                            <a href="#tab_contentFunction" id="home-tab" role="tab" data-toggle="tab" aria-expanded="false">
                                <span id="idTagFunction"></span>
                            </a>
                        </li>
                    </ul>
                    <script>
                        $("#idTagCredentialAPI").text(profileaccss_fm_rose);
                        $("#idTagIP").text(profileaccss_fm_service_type);
                        $("#idTagFunction").text(profileaccss_fm_major_cert);
                    </script>
                    <div id="myTabContentTypeKey" class="tab-content">
                        <div role="tabpanel" class="tab-pane fade active in" id="tab_contentAPI" aria-labelledby="home-tab1">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <div class="form-group" style="text-align: right; padding: 0px 0px 10px 0px;margin: 0;">
                                <%
                                    if(approveCAProfileEnabled == true)
                                    {
                                %>
                                <div class="col-sm-6" style="padding-left: 0;text-align: left;">
                                </div>
                                <div class="col-sm-6" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleLoadProfile" class="control-label" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                        <a style="cursor: pointer;color: blue; font-weight: bold; text-decoration: underline;" onclick="LoadProfileAll('1','0');" id="idLoadProfile"></a>
                                        <script>
                                            $("#idLoadProfile").text(error_content_link_out);
                                            $("#idLblTitleLoadProfile").text(global_fm_button_reload_profile);
                                        </script>
                                    </div>
                                </div>
                                <input TYPE="checkbox" id="ACCESS_PROFILE_ALL" name="ACCESS_PROFILE_ALL" checked="false" style="display: none;" />
                                <%
                                    } else {
                                        String remarkAccessProfileAll = "";
                                        boolean booAccessProfileAll = false;
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resIPData[0])
                                        {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                            {
                                                if(resProfileData1.name.equals("true"))
                                                {
                                                    booAccessProfileAll = true;
                                                }
                                                remarkAccessProfileAll = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                            }
                                        }
                                %>
                                <div class="col-sm-4" style="padding-left: 0;text-align: left;">
                                    <div class="form-group">
                                        <label id="idLblTitleACCESS_PROFILE_ALL" class="control-label col-sm-9" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                        <div class="col-sm-3" style="padding-right: 0px;text-align: left;">
                                            <label class="switch" for="ACCESS_PROFILE_ALL">
                                                <input TYPE="checkbox" id="ACCESS_PROFILE_ALL" name="ACCESS_PROFILE_ALL" <%= booAccessProfileAll == true ? "checked" : ""%>/>
                                                <div class="slider round" id="ACCESS_PROFILE_ALLClass"></div>
                                            </label>
                                        </div>
                                    </div>
                                    <script>$("#idLblTitleACCESS_PROFILE_ALL").text('<%=remarkAccessProfileAll%>');</script>
                                </div>
                                <div class="col-sm-4" style="padding-left: 0;text-align: left;">
                                </div>
                                <div class="col-sm-4" style="padding-left: 0;">
                                    <div class="form-group">
                                        <label id="idLblTitleLoadProfile" class="control-label" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                        <a style="cursor: pointer;color: blue; font-weight: bold; text-decoration: underline;" onclick="LoadProfileAll('1','0');" id="idLoadProfile"></a>
                                        <script>
                                            $("#idLoadProfile").text(error_content_link_out);
                                            $("#idLblTitleLoadProfile").text(global_fm_button_reload_profile);
                                        </script>
                                    </div>
                                </div>
                                <%
                                    }
                                %>
                            </div>
                            <div class="table-responsive">
                                <%
                                    int j=1;
                                %>
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
                                            if(resIPData != null && resIPData[0].length > 0) {
                                                for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                    if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                    {
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
                                            } else {
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
                                                            console.log(sCount);
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
        //                                            LoadProfileAll('0', '0');
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
        //                                            LoadProfileAll('0', '0');
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
                            </div>
                            </div>
                        </div>
                        <div role="tabpanel" class="tab-pane fade" id="tab_contentIP" aria-labelledby="home-tab">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
<!--                                <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleRequestType"></label></h2>
                                <script>$("#idLblTitleRequestType").text(profileaccss_fm_service_type);</script>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li style="color: red;font-weight: bold;"></li>
                                </ul>-->
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content" style="display: <%= approveCAProfileEnabled ? "" : "none" %>">
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
                                                        <input disabled type="checkbox" name="idCheckProfileRequest<%=n%>" id="idCheckProfileRequest<%=n%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                        <div id="idCheckProfileClassRequest<%=n%>" class="slider round"></div>
                                                    </label>
                                                </td>
                                                <td>
                                                    <label class="switch" for="idCheckApproveCARequest<%=n%>" style="margin-bottom: 0;">
                                                        <input onclick="onApproveCARequestActive('<%=resIPData1.name%>', 'idCheckApproveCARequest<%=n%>');" type="checkbox"
                                                            name="idCheckApproveCARequest<%=n%>" id="idCheckApproveCARequest<%=n%>" <%= resIPData1.approveCAEnabled == true ? "checked" : ""%>/>
                                                        <div id="idCheckApproveCAClassRequest<%=n%>" class="slider round"></div>
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
                            </div>
                        </div>
                        <div role="tabpanel" class="tab-pane fade" id="tab_contentFunction" aria-labelledby="home-tab">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <div class="table-responsive">
                                    <table id="tblCertUseRequest" class="table table-bordered table-striped projects">
                                        <thead>
                                        <th id="idLblTitleTableMajorSTT"></th>
                                        <th id="idLblTitleTableMajorName"></th>
                                        <th id="idLblTitleTableMajorProfileRemark"></th>
                                        <th id="idLblTitleTableMajorActive"></th>
                                        <script>
                                            $("#idLblTitleTableMajorSTT").text(global_fm_STT);
                                            $("#idLblTitleTableMajorName").text(cert_fm_major_code);
                                            $("#idLblTitleTableMajorProfileRemark").text(cert_fm_major_name);
                                            $("#idLblTitleTableMajorActive").text(global_fm_active);
                                        </script>
                                        </thead>
                                        <tbody id="idTemplateAssignMajor">
                                            <%
                                                int k=1;
                                                boolean hasMajorRecord = false;
                                                if(resIPData != null && resIPData[0].length > 0) {
                                                    for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                        if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                        {
                                                            hasMajorRecord = true;
                                            %>
                                            <tr>
                                                <td><%= k%></td>
                                                <td><%= resIPData1.name %></td>
                                                <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                <td>
                                                    <label class="switch" for="idCheckProfileMajor<%=k%>" style="margin-bottom: 0;">
                                                        <input onclick="onMajorActive('<%=resIPData1.name%>', 'idCheckProfileMajor<%=k%>');" type="checkbox"
                                                            name="idCheckProfileMajor<%=k%>" id="idCheckProfileMajor<%=k%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                        <div id="idCheckProfileClassMajor<%=k%>" class="slider round"></div>
                                                    </label>
                                                </td>
                                            </tr>
                                            <%
                                                        k++;
                                                        }
                                                    }
                                                }
                                            %>
                                            <%
                                                if(hasMajorRecord == false)
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
                                    <script>
                                        function onMajorActive(idName, idCheckProfie)
                                        {
                                            var sProfileActive = "0";
                                            if ($("#"+idCheckProfie).is(':checked')) {
                                                sProfileActive = "1";
                                            }
                                            $.ajax({
                                                type: "post",
                                                url: "../ProfileAccessCommon",
                                                data: {
                                                    idParam: 'activemajoraccess',
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
                                    </script>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
<!--                <fieldset class="scheduler-border" style="clear: both; display: <= approveCAProfileEnabled ? "" : "none" %>">
                    <legend class="scheduler-border" id="idLblTitleRequestType"></legend>
                    <script>$("#idLblTitleRequestType").text(profileaccss_fm_service_type);</script>
                    <style type="text/css">
                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                        .table > thead > tr > th{border-bottom: none;}
                        .btn{margin-bottom: 0px;}
                        .panel_toolbox { min-width: 0;}
                    </style>
                    
                </fieldset>-->
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                        </div>
                    </div>
                    <script>$("#idLblTitleCreateUser").text(global_fm_user_create);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strDateLimit" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                        </div>
                    </div>
                    <script>$("#idLblTitleCreateDate").text(global_fm_date_create);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= rs[0][0].MODIFIED_BY%>" />
                        </div>
                    </div>
                    <script>$("#idLblTitleUpdateUser").text(global_fm_user_endupdate);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateDate" class="form-control123" value="<%= rs[0][0].MODIFIED_DT%>" />
                        </div>
                    </div>
                    <script>$("#idLblTitleUpdateDate").text(global_fm_date_endupdate);</script>
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
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
    </body> 
</html>