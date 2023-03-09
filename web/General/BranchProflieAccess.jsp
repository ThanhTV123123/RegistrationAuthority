<%-- 
    Document   : BranchProflieAccess
    Created on : Sep 19, 2019, 10:27:57 AM
    Author     : THANH-PC
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <style>
            .projects th{font-weight: bold;}.navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 10px 10px;border-bottom:0px;
            }
            .level0{
                background: red;
            }
            .collapse_abc .toggle_abc {
                background: url("../Images/collapse.gif");
            }
            .expand_abc .toggle_abc {
                background: url("../Images/expand.gif");
            }
            .toggle_abc {
                height: 9px;
                width: 9px;
                display: inline-block;   
            }.x_panel {
                padding:15px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
             .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
        <script>
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshBranch'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "BranchList.jsp";
                        }
                        else
                        {
                            window.location = "BranchList.jsp";
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
        </style>
    </head>
    <body>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
            <%                BRANCH[][] rs = new BRANCH[1][];
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                try {
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString().trim();
                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_BRANCH_DETAIL(ids, rs);
                        if (rs[0].length > 0) {
                            String strID = String.valueOf(rs[0][0].ID);
                            String strBranchCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String strBranchRemark = "1".equals(sessLanguageGlobal) ? EscapeUtils.CheckTextNull(rs[0][0].REMARK) : EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
//                            ObjectMapper oMapperParse = new ObjectMapper();
                            boolean booAccessProfileAll = false;
                            String remarkAccessProfileAll = "";
                            boolean booAutoApproveCert = false;
                            String remarkAutoApproveCert = "";
                            boolean booPushoticeEnabled = false;
                            String remarkPushoticeEnabled = "";
                            boolean booP12EmailEnabled = false;
                            String remarkP12EmailEnabled = "";
//                            boolean booShareCertEnabled = false;
//                            String remarkShareCertEnabled = "";
                            String sBeneficiacyUser = "";
                            String remarkBeneficiacyUser = "";
                            String sApproveCAUser = "";
                            String remarkApproveCAUser = "";
                            String strCADefault="";
                            String sJSON_PROFILE = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_POLICY_PROPERTIES);
                            if(!"".equals(sJSON_PROFILE))
                            {
                                CERTIFICATION_POLICY_DATA[][] resProfileData = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getAllProfilePolicyForBranch(sJSON_PROFILE, resProfileData);
                                if(resProfileData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessProfilePolicyAll", resProfileData);
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                        {
                                            if(resProfileData1.name.equals("true"))
                                            {
                                                booAccessProfileAll = true;
                                            }
                                            remarkAccessProfileAll = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVED_ENABLED))
                                        {
                                            if(resProfileData1.name.equals("true"))
                                            {
                                                booAutoApproveCert = true;
                                            }
                                            remarkAutoApproveCert = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_BENEFICIACY_USER))
                                        {
                                            sBeneficiacyUser = resProfileData1.name;
                                            remarkBeneficiacyUser = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVE_CA_USER))
                                        {
                                            sApproveCAUser = resProfileData1.name;
                                            remarkApproveCAUser = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PUSH_NOTICE_ENABLED))
                                        {
                                            if(resProfileData1.name.equals("true"))
                                            {
                                                booPushoticeEnabled = true;
                                            }
                                            remarkPushoticeEnabled = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_P12_EMAIL_ENABLED))
                                        {
                                            if(resProfileData1.name.equals("true"))
                                            {
                                                booP12EmailEnabled = true;
                                            }
                                            remarkP12EmailEnabled = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
                                        }
//                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
//                                        {
//                                            if(resProfileData1.name.equals("true"))
//                                            {
//                                                booShareCertEnabled = true;
//                                            }
//                                            remarkShareCertEnabled = "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn;
//                                        }
                                    }
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                        {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT))
                                            {
                                                strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
            %>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleProfileEdits"></label></h2>
                <script>$("#idLblTitleProfileEdits").text(branch_fm_profile_title_access);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li style="padding-right: 10px;">
                        <input id="btnProfileOtherAdd" class="btn btn-info" type="button" onclick="onOtherActive('<%= anticsrf%>');" />
                        <script>
                            document.getElementById("btnProfileOtherAdd").value = global_fm_button_edit;
                        </script>
                        <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                        <script>
                            document.getElementById("btnClose").value = global_fm_button_close;
                        </script>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <form name="myname" method="post" class="form-horizontal">
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfileCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input id="idID" name="idID" value="<%=strID%>" readonly style="display: none;" />
                                <input value="<%= strBranchCode%>" class="form-control123" readonly id="BranchCode" name="BranchCode">
                            </div>
                        </div>
                        <script>$("#idLblTitleProfileCode").text(branch_fm_code);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleProfileRemark"></label>
                                <label class="CssRequireField" id="idLblNoteRemark"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= strBranchRemark%>" class="form-control123" readonly id="Remark" name="Remark"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleProfileRemark").text(branch_fm_name);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: none;">
                        <div class="form-group">
                            <label id="idLblTitleProfileAll" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckProfileAll" style="margin-bottom: 0;">
                                    <input type="checkbox" name="idCheckProfileAll" id="idCheckProfileAll" <%= booAccessProfileAll == true ? "checked" : ""%>/>
                                    <div id="idCheckProfileAllClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfileAll").text('<%=remarkAccessProfileAll%>');
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfieAutoApprove" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckProfieAutoApprove" style="margin-bottom: 0;">
                                    <input type="checkbox" name="idCheckProfieAutoApprove" id="idCheckProfieAutoApprove" <%= booAutoApproveCert == true ? "checked" : ""%>/>
                                    <div id="idCheckPushNoticeEnabledClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfieAutoApprove").text('<%=remarkAutoApproveCert%>');
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfieApproveCAUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <!--<input id="idProfileApproveCAUser" class="form-control123" type="text" value="<=sApproveCAUser%>" />-->
                                <select name="idProfileApproveCAUser" id="idProfileApproveCAUser" class="form-control123">
                                    <option value="" id="idAllApproveCAUser"></option>
                                    <script>
                                        $("#idAllApproveCAUser").text(global_fm_combox_choose);
                                    </script>
                                    <%
                                        BACKOFFICE_USER[][] rssApproveCAUser = new BACKOFFICE_USER[1][];
                                        db.S_BO_USER_ACCESS_PROFILE(ids, 1, Integer.parseInt(sessLanguageGlobal), rssApproveCAUser);
                                        if (rssApproveCAUser[0].length > 0) {
                                            for (int i = 0; i < rssApproveCAUser[0].length; i++) {
                                    %>
                                    <option value="<%=rssApproveCAUser[0][i].USERNAME%>" <%= rssApproveCAUser[0][i].USERNAME.equals(sApproveCAUser) ? "selected" : "" %>><%=rssApproveCAUser[0][i].USERNAME + " (" + rssApproveCAUser[0][i].BRANCH_REMARK + ")" %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfieApproveCAUser").text('<%=remarkApproveCAUser%>');
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfileBeneficiacyUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
<!--                                    <input id="idProfileBeneficiacyUser" class="form-control123" type="text" value="<=sBeneficiacyUser%>" />-->
                                <select name="idProfileBeneficiacyUser" id="idProfileBeneficiacyUser" class="form-control123">
                                    <option value="" id="idAllBeneficiacyUser"></option>
                                    <script>
                                        $("#idAllBeneficiacyUser").text(global_fm_combox_choose);
                                    </script>
                                    <%
                                        BACKOFFICE_USER[][] rssBeneficiacyUser = new BACKOFFICE_USER[1][];
                                        db.S_BO_USER_ACCESS_PROFILE(ids, 0, Integer.parseInt(sessLanguageGlobal), rssBeneficiacyUser);
                                        if (rssBeneficiacyUser[0].length > 0) {
                                            for (int i = 0; i < rssBeneficiacyUser[0].length; i++) {
                                    %>
                                    <option value="<%=rssBeneficiacyUser[0][i].USERNAME%>" <%= rssBeneficiacyUser[0][i].USERNAME.equals(sBeneficiacyUser) ? "selected" : "" %>><%=rssBeneficiacyUser[0][i].USERNAME + " (" + rssBeneficiacyUser[0][i].BRANCH_REMARK + ")" %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfileBeneficiacyUser").text('<%=remarkBeneficiacyUser%>');
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfieP12EmailEnabled" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckP12EmailEnabled" style="margin-bottom: 0;">
                                    <input type="checkbox" name="idCheckP12EmailEnabled" id="idCheckP12EmailEnabled" <%= booP12EmailEnabled == true ? "checked" : ""%>/>
                                    <div id="idCheckP12EmailEnabledClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfieP12EmailEnabled").text('<%=remarkP12EmailEnabled%>');
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleProfilePushNoticeEnabled" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="idCheckPushNoticeEnabled" style="margin-bottom: 0;">
                                    <input type="checkbox" name="idCheckPushNoticeEnabled" id="idCheckPushNoticeEnabled" <%= booPushoticeEnabled == true ? "checked" : ""%>/>
                                    <div id="idCheckPushNoticeEnabledClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfilePushNoticeEnabled").text('<%=remarkPushoticeEnabled%>');
                        </script>
                    </div>
<!--                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <div class="col-sm-6" style="padding-right: 0px;text-align: left;padding-left: 0;">
                                <input id="btnProfileOtherAdd" class="btn btn-info" type="button" onclick="onOtherActive('<= anticsrf%>');" />
                                <script>
                                    document.getElementById("btnProfileOtherAdd").value = global_fm_button_edit;
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-right: 0px;">

                            </div>
                        </div>
                        <script>
                            $("#idLblTitleProfieP12EmailEnabled").text('<=remarkP12EmailEnabled%>');
                        </script>
                    </div>-->
                    <script>
                        function onOtherActive(idCSRF)
                        {
                            if (!JSCheckEmptyField($("#idProfileApproveCAUser").val()))
                            {
                                $("#idProfileApproveCAUser").focus();
                                funErrorAlert(policy_req_empty + '<%=remarkApproveCAUser%>');
                                return false;
                            }
                            var isCheckProfileAll = "0";
                            if ($("#idCheckProfileAll").is(':checked'))
                            {
                                isCheckProfileAll = "1";
                            }
                            var isCheckProfieAutoApprove = "0";
                            if ($("#idCheckProfieAutoApprove").is(':checked'))
                            {
                                isCheckProfieAutoApprove = "1";
                            }
                            var isCheckPushNoticeEnabled = "0";
                            if ($("#idCheckPushNoticeEnabled").is(':checked'))
                            {
                                isCheckPushNoticeEnabled = "1";
                            }
                            var isCheckP12EmailEnabled = "0";
                            if ($("#idCheckP12EmailEnabled").is(':checked'))
                            {
                                isCheckP12EmailEnabled = "1";
                            }
//                                var isCheckShareCertEnabled = "0";
//                                if ($("#idCheckShareCertEnabled").is(':checked'))
//                                {
//                                    isCheckShareCertEnabled = "1";
//                                }
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../BranchCommon",
                                data: {
                                    idParam: 'activeotherpolicybranch',
                                    sID: $("#idID").val(),
                                    isCheckProfileAll: isCheckProfileAll,
                                    isCheckProfieAutoApprove: isCheckProfieAutoApprove,
                                    isCheckPushNoticeEnabled: isCheckPushNoticeEnabled,
                                    isCheckP12EmailEnabled: isCheckP12EmailEnabled,
//                                        isCheckShareCertEnabled: isCheckShareCertEnabled,
                                    sProfileApproveCAUser: $("#idProfileApproveCAUser").val(),
                                    sProfileBeneficiacyUser: $("#idProfileBeneficiacyUser").val(),
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccNoLoad(inputcertlist_succ_edit);
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
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                }
                            });
                            return false;
                        }
                    </script>
                            
                    <fieldset class="scheduler-border" style="clear: both;display: none;">
                        <legend class="scheduler-border" id="idLblProfileGroupProfile"></legend>
                        <script>$("#idLblProfileGroupProfile").text(branch_fm_profile_group_profile);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleProfileCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                        onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                        <%
                                            String sFristCA = "";
                                            CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                            db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                            if (rssProfile[0].length > 0) {
                                                int intCAIDDefault = CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                for (int i = 0; i < rssProfile[0].length; i++) {
                                                    if(rssProfile[0][i].ID == intCAIDDefault)
                                                    {
                                                        if("".equals(sFristCA)) {
                                                            sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                        }
                                                    }
                                        %>
                                        <option value="<%=String.valueOf(rssProfile[0][i].ID)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitleProfileCA").text(global_fm_ca);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleProfileCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                        onchange="LOAD_CERTIFICATION_PURPOSE($('#CERTIFICATION_AUTHORITY').val(), this.value, '<%= anticsrf%>');">
                                        <%
                                            String sFristCerPurpose="";
                                            CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                            db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                            if (rsCertPro.length > 0) {
                                                for (int i = 0; i < rsCertPro[0].length; i++) {
                                                    sFristCerPurpose = String.valueOf(rsCertPro[0][0].ID);
                                        %>
                                        <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>"><%= rsCertPro[0][i].REMARK%></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitleProfileCertPurpose").text(global_fm_certpurpose);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleProfileCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                        onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                        <%
                                            CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                            db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, "1", sessLanguageGlobal, rsDuration);
                                            if (rsDuration[0].length > 0) {
                                                for (int i = 0; i < rsDuration[0].length; i++) {
                                        %>
                                        <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitleProfileCertDuration").text(global_fm_duration_cts);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <div class="col-sm-5" style="padding-right: 0px;">
                                    
                                </div>
                                <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                    <input id="btnProfileAdd" class="btn btn-info" type="button" onclick="onProfileAdd('<%= anticsrf%>');" />
                                    <script>
                                        document.getElementById("btnProfileAdd").value = global_fm_button_New;
                                    </script>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleProfilePushNoticeEnabled").text('<%=remarkPushoticeEnabled%>');
                                function onProfileAdd()
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'addprofilepolicybranch',
                                            sID: $("#idID").val(),
                                            sProfileCert: $("#CERTIFICATION_DURATION").val()
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onLoadProfile();
                                                funSuccNoLoad(inputcertlist_succ_add);
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
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }
                                function onLoadProfile()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadprofilepolicybranch'
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idTemplateAssignProfile").empty();
                                                    var contentProps = "";
                                                    var sCount = 1;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                        var isChecked = "";
                                                        if(obj[i].ACTIVE === "1")
                                                        {
                                                            isChecked = "checked";
                                                        }
                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onchange=\"onProfileActive('"+obj[i].NAME+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].NAME + "</td>" +
                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                            "<td>" + sActiveHTML + "</td>" +
                                                            "</tr>";
                                                        sCount++;
                                                    }
                                                    $("#idTemplateAssignProfile").append(contentProps);
                                                    if (sCount > 0)
                                                    {
                                                        $('#greenProfile').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseProfile', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                                function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadcert_authority_ofsofttoken',
                                            idCA: objCA,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var cbxCERTIFICATION_PURPOSE = document.getElementById("CERTIFICATION_PURPOSE");
                                                removeOptions(cbxCERTIFICATION_PURPOSE);
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                    }
                                                    LOAD_CERTIFICATION_PURPOSE(objCA, obj[0].ID, idCSRF);
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
                                                    cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option("---", "");
                                                }
                                                else {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                        }
                                    });
                                    return false;
                                }
                                function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadcert_purpose',
                                            idCA: objCA,
                                            idPurpose: objPurpose,
                                            idAttrType: "",
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
                                                removeOptions(cbxCERTIFICATION_DURATION);
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
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
                                                else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                {
                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                }
                                                else if (obj[0].Code === "1")
                                                {
                                                    cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
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
                            CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfilePolicyAll");
                            if(resProfileData[0].length > 0) {
                            int j=1;
                        %>
                        <div class="table-responsive">
                            <table id="tblCertUseProfile" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableProfileSTT"></th>
                                <th id="idLblTitleTableProfileName"></th>
                                <th id="idLblTitleTableProfileRemark"></th>
                                <th id="idLblTitleTableProfileActive"></th>
                                <script>
                                    $("#idLblTitleTableProfileSTT").text(global_fm_STT);
                                    $("#idLblTitleTableProfileName").text(global_fm_duration_cts);
                                    $("#idLblTitleTableProfileRemark").text(global_fm_Description);
                                    $("#idLblTitleTableProfileActive").text(global_fm_active);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignProfile">
                                    <%
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0]) {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                            {
                                    %>
                                    <tr>
                                        <td><%= j%></td>
                                        <td><%= resProfileData1.name%></td>
                                        <td><%= "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn%></td>
                                        <td>
                                            <label class="switch" for="idCheckProfile<%=j%>" style="margin-bottom: 0;">
                                                <input onclick="onProfileActive('<%=resProfileData1.name%>', 'idCheckProfile<%=j%>');" type="checkbox" name="idCheckProfile<%=j%>" id="idCheckProfile<%=j%>" <%= resProfileData1.enabled == true ? "checked" : ""%>/>
                                                <div id="idCheckProfileClass<%=j%>" class="slider round"></div>
                                            </label>
                                        </td>
                                    </tr>
                                    <%
                                                j++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                            <div id="greenProfile" style="margin: 5px 0 5px 0;"> </div>
                            <script>
                                if (parseInt('<%=j%>')-1 > 0)
                                {
                                    $('#greenProfile').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseProfile', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                }
                                function onProfileActive(profileCode, activeKey)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    var isProfileActive = "0";
                                    if ($("#"+activeKey).is(':checked'))
                                    {
                                        isProfileActive = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'activeprofilepolicybranch',
                                            sID: $("#idID").val(),
                                            sProfileActive: isProfileActive,
                                            sProfileCode: profileCode
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                funSuccNoLoad(inputcertlist_succ_edit);
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
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }
                            </script>
                        </div>
                        <%
                            }
                        %>
                    </fieldset>
<!--                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblProfileGroupFactor"></legend>
                        <script>$("#idLblProfileGroupFactor").text(branch_fm_profile_group_formfactor);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123">
                                        <
                                            PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                            sFristCerPurpose = "0";
                                            db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                sessLanguageGlobal, rsPKIFormFactor);
                                            if (rsPKIFormFactor.length > 0) {
                                                for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                        %>
                                        <option value="<= String.valueOf(rsPKIFormFactor[0][i].ID)%>"><= rsPKIFormFactor[0][i].REMARK%></option>
                                        <
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitlePKIFormfactor").text(global_fm_Method);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <div class="col-sm-5" style="padding-right: 0px;">
                                    
                                </div>
                                <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                    <input id="btnFactorAdd" class="btn btn-info" type="button" onclick="onFormFactorAdd('<=anticsrf%>');" />
                                    <script>
                                        document.getElementById("btnFactorAdd").value = global_fm_button_New;
                                    </script>
                                </div>
                            </div>
                            <script>
                                function onFormFactorAdd(idCSRF)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'addfactorpolicybranch',
                                            sID: $("#idID").val(),
                                            sFactorCert: $("#PKI_FORMFACTOR").val(),
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onLoadFactor();
                                                funSuccNoLoad(inputcertlist_succ_add);
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
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }
                                function onLoadFactor()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadfactorpolicybranch'
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idTemplateAssignFactor").empty();
                                                    var contentProps = "";
                                                    var sCount = 1;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                        var isChecked = "";
                                                        if(obj[i].ACTIVE === "1")
                                                        {
                                                            isChecked = "checked";
                                                        }
                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onchange=\"onFactorActive('"+obj[i].name+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].NAME + "</td>" +
                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                            "<td>" + sActiveHTML + "</td>" +
                                                            "</tr>";
                                                        sCount++;
                                                    }
                                                    $("#idTemplateAssignFactor").append(contentProps);
                                                    if (sCount > 0)
                                                    {
                                                        $('#greenFactor').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseFactor', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                        </div>&nbsp;
                        <
                            CERTIFICATION_POLICY_DATA[][] resFormFactorData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfilePolicyAll");
                            if(resFormFactorData[0].length > 0) {
                            int j=1;
                        %>
                        <div class="table-responsive">
                            <table id="tblCertUseFactor" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableFactorSTT"></th>
                                <th id="idLblTitleTableFactorName"></th>
                                <th id="idLblTitleTableFactorRemark"></th>
                                <th id="idLblTitleTableFactorActive"></th>
                                <script>
                                    $("#idLblTitleTableFactorSTT").text(global_fm_STT);
                                    $("#idLblTitleTableFactorName").text(global_fm_Method);
                                    $("#idLblTitleTableFactorRemark").text(global_fm_Description);
                                    $("#idLblTitleTableFactorActive").text(global_fm_active);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignFactor">
                                    <
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resFormFactorData[0]) {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST))
                                            {
                                    %>
                                    <tr>
                                        <td><= j%></td>
                                        <td><= resProfileData1.name%></td>
                                        <td>%= "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn%></td>
                                        <td>
                                            <label class="switch" for="idCheckFactor<=j%>" style="margin-bottom: 0;">
                                                <input onclick="onFactorActive('<=resProfileData1.name%>', 'idCheckFactor<=j%>');" type="checkbox" name="idCheckFactor<=j%>" id="idCheckFactor<=j%>" <= resProfileData1.enabled == true ? "checked" : ""%>/>
                                                <div id="idCheckFactorClass<=j%>" class="slider round"></div>
                                            </label>
                                        </td>
                                    </tr>
                                    <
                                                j++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                            <div id="greenFactor" style="margin: 5px 0 5px 0;"> </div>
                            <script>
                                if (parseInt('<=j%>')-1 > 0)
                                {
                                    $('#greenFactor').smartpaginator({totalrecords: parseInt('<=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseFactor', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                }
                                function onFactorActive(factorCode, activeKey)
                                {
                                    $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                    var isFactorActive = "0";
                                    if ($("#"+activeKey).is(':checked'))
                                    {
                                        isFactorActive = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'activefactorpolicybranch',
                                            sID: $("#idID").val(),
                                            sFactorActive: isFactorActive,
                                            sFactorCode: factorCode
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                funSuccNoLoad(inputcertlist_succ_edit);
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
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }
                            </script>
                        </div>
                        <
                            }
                        %>
                    </fieldset>-->
<!--                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblProfileGroupOther"></legend>
                        <script>$("#idLblProfileGroupOther").text(token_group_other);</script>
                        
                    </fieldset>-->
                </form>
            </div>
            <%
            } else {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;">error</label>
            </div>
            <%
                }
            } else {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;">error</label>
            </div>
            <%
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
        <!--<script src="../style/bootstrap.min.js"></script>-->
        <!--<script src="../style/custom.min.js"></script>-->
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
    </body>
</html>