<%-- 
    Document   : BranchAPIAccess
    Created on : Sep 19, 2019, 10:30:29 AM
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
                            boolean booAccessIPAll = false;
                            String remarkAccessIPAll = "";
                            boolean booAutoFunctionAll = false;
                            String remarkAutoFunctionAll = "";
                            String sJSON_IP = EscapeUtils.CheckTextNull(rs[0][0].IP_ADDRESS_PROPERTIES);
                            if(!"".equals(sJSON_IP))
                            {
                                CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getAllIPPolicyForBranch(sJSON_IP, resIPData);
                                if(resIPData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessIPPolicyAll", resIPData);
                                    for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0])
                                    {
                                        if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS))
                                        {
                                            if(resIPData1.name.equals("true"))
                                            {
                                                booAccessIPAll = true;
                                            }
                                            remarkAccessIPAll = "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn;
                                        }
                                    }
                                }
                            }
                            String sJSON_FUNCTION = EscapeUtils.CheckTextNull(rs[0][0].FUNCTIONALTITY_PROPERTIES);
                            if(!"".equals(sJSON_FUNCTION))
                            {
                                CERTIFICATION_POLICY_DATA[][] resFunctionData = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getAllFunctionPolicyForBranch(sJSON_FUNCTION, resFunctionData);
                                if(resFunctionData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessFunctionPolicyAll", resFunctionData);
                                    for(CERTIFICATION_POLICY_DATA resFunctionData1 : resFunctionData[0])
                                    {
                                        if(resFunctionData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS))
                                        {
                                            if(resFunctionData1.name.equals("true"))
                                            {
                                                booAutoFunctionAll = true;
                                            }
                                            remarkAutoFunctionAll = "1".equals(sessLanguageGlobal) ? resFunctionData1.remark : resFunctionData1.remarkEn;
                                        }
                                    }
                                }
                            }
                            
            %>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleProfileEdits"></label></h2>
                <script>$("#idLblTitleProfileEdits").text(branch_fm_api_title_access);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li style="padding-right: 10px;">
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
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleAPICode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input id="idID" name="idID" value="<%=strID%>" readonly style="display: none;" />
                                <input value="<%= strBranchCode%>" class="form-control123" readonly id="BranchCode" name="BranchCode">
                            </div>
                        </div>
                        <script>$("#idLblTitleAPICode").text(branch_fm_code);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleAPIRemark"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= strBranchRemark%>" class="form-control123" readonly id="Remark" name="Remark"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleAPIRemark").text(branch_fm_name);</script>
                    </div>
                    <div class="clearfix"></div>
                    <div class="" role="tabpanel" data-example-id="togglable-tabs">
                        <ul id="myTabTypeKey" class="nav nav-tabs bar_tabs" role="tablist">
                            <li role="presentation" class="active" id="idLi_contentAPI">
                                <a href="#tab_contentAPI" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="true">
                                    <span id="idTagCredentialAPI"></span>
                                </a>
                            </li>
                            <li role="presentation" class="" id="idLi_contentRest">
                                <a href="#tab_contentRest" role="tab" data-toggle="tab" id="home-tab" aria-expanded="false">
                                    <span id="idTagCredentialRest"></span>
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
                            $("#idTagCredentialAPI").text(branch_fm_api_tag_credential);
                            $("#idTagCredentialRest").text(branch_fm_rest_tag_credential);
                            $("#idTagIP").text(branch_fm_api_tag_ip);
                            $("#idTagFunction").text(branch_fm_api_tag_function);
                        </script>
                        <div id="myTabContentTypeKey" class="tab-content">
                            <div role="tabpanel" class="tab-pane fade active in" id="tab_contentAPI" aria-labelledby="home-tab1">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleCredentialEdits"></label></h2>
                                    <script>$("#idLblTitleCredentialEdits").text(branch_fm_api_tag_credential);</script>
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;">
                                            <input id="btnAPIUpdate" class="btn btn-info" type="button" onclick="onAPIUpdate('<%= anticsrf%>');" />
                                            <script>
                                                document.getElementById("btnAPIUpdate").value = global_fm_button_edit;
                                            </script>
                                        </li>
                                    </ul>
                                    <div class="clearfix"></div>
                                    <script>
                                        function onAPIUpdate(idCSRF)
                                        {
                                            if (!JSCheckEmptyField($("#APIUsername").val()))
                                            {
                                                $("#APIUsername").focus();
                                                funErrorAlert(policy_req_empty + global_fm_Username);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#APIRemark").val()))
                                            {
                                                $("#APIRemark").focus();
                                                funErrorAlert(policy_req_empty + global_fm_remark_vn);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#APIRemarkEN").val()))
                                            {
                                                $("#APIRemarkEN").focus();
                                                funErrorAlert(policy_req_empty + global_fm_remark_en);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#APISignature").val()))
                                            {
                                                $("#APISignature").focus();
                                                funErrorAlert(policy_req_empty + branch_fm_api_signture);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#APIPublicKeyPem").val()))
                                            {
                                                $("#APIPublicKeyPem").focus();
                                                funErrorAlert(policy_req_empty + branch_fm_api_publishkey);
                                                return false;
                                            }
                                            var sCheckAPIAllow = "0";
                                            if ($("#idCheckAPIAllow").is(':checked'))
                                            {
                                                sCheckAPIAllow = "1";
                                            }
                                            $('body').append('<div id="over"></div>');
                                            $(".loading-gif").show();
                                            $.ajax({
                                                type: "post",
                                                url: "../BranchCommon",
                                                data: {
                                                    idParam: 'editsoapuibranch',
                                                    id: $("#idID").val(),
                                                    sAPIUsername: $("#APIUsername").val(),
                                                    sAPIPassword: $("#APIPassword").val(),
                                                    sAPISignature: $("#APISignature").val(),
                                                    sAPIPublicKeyPem: $("#APIPublicKeyPem").val(),
                                                    sAPIRemark: $("#APIRemark").val(),
                                                    sAPIRemarkEN: $("#APIRemarkEN").val(),
                                                    sCheckAPIAllow: sCheckAPIAllow,
                                                    CsrfToken: idCSRF
                                                },
                                                cache: false,
                                                success: function (html)
                                                {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0")
                                                    {
                                                        $("#APIPassword").val('');
                                                        funSuccNoLoad(inputcertlist_succ_edit);
                                                    } else if(myStrings[0] === "PASS_NULL")
                                                    {
                                                        funErrorAlert(policy_req_empty + global_fm_Password);
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
                                <div class="x_content">
                                    <%
                                        String sSignture = "";
                                        String sRemark = "";
                                        String sRemarkEN = "";
                                        String sPublicKeyPem = "";
                                        ObjectMapper oMapperParse;
                                        oMapperParse = new ObjectMapper();
                                        String sJSON_SOAP = EscapeUtils.CheckTextNull(rs[0][0].SOAP_SECURITY_PROPERTIES);
                                        boolean isCheckAllow = false;
                                        if(!"".equals(sJSON_SOAP))
                                        {
                                            isCheckAllow = true;
                                            SOAPSecureProperties itemParsePush = oMapperParse.readValue(sJSON_SOAP, SOAPSecureProperties.class);
                                            sSignture = EscapeUtils.CheckTextNull(itemParsePush.signature);
                                            sPublicKeyPem = EscapeUtils.CheckTextNull(itemParsePush.publicKeyPem);
                                            sRemark = EscapeUtils.CheckTextNull(itemParsePush.remark);
                                            sRemarkEN = EscapeUtils.CheckTextNull(itemParsePush.remarkEn);
                                        }
                                        String sCheckAllow = "0";
                                        if(isCheckAllow == true){
                                            sCheckAllow = "1";
                                        }
                                    %>
                                    <div class="col-sm-13" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label id="idLblTitleAPIAllow" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-10" style="padding-right: 0px;">
                                                <label class="switch" for="idCheckAPIAllow" style="margin-bottom: 0;">
                                                    <input type="checkbox" name="idCheckAPIAllow" id="idCheckAPIAllow" <%= isCheckAllow == true ? "checked" : "" %> onclick="onClickAllow();"/>
                                                    <div id="idCheckAPIAllowClass" class="slider round"></div>
                                                </label>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIAllow").text(branch_fm_api_allow_access);
//                                            $(document).ready(function () {
//                                                if('<= sCheckAllow%>' === "0")
//                                                {
//                                                    $("#btnAPIUpdate").attr("disabled", true);
//                                                } else {
//                                                    $("#btnAPIUpdate").attr("disabled", false);
//                                                }
//                                            });
                                            function onClickAllow()
                                            {
                                                if ($("#idCheckAPIAllow").is(':checked'))
                                                {
                                                    $("#APIPassword").attr("disabled", false);
                                                    $("#APISignature").attr("disabled", false);
                                                    $("#APIPublicKeyPem").attr("disabled", false);
                                                    $("#APIRemark").attr("disabled", false);
                                                    $("#APIRemarkEN").attr("disabled", false);
//                                                    $("#btnAPIUpdate").attr("disabled", false);
                                                } else {
                                                    $("#APIPassword").attr("disabled", true);
                                                    $("#APISignature").attr("disabled", true);
                                                    $("#APIPublicKeyPem").attr("disabled", true);
                                                    $("#APIRemark").attr("disabled", true);
                                                    $("#APIRemarkEN").attr("disabled", true);
//                                                    $("#btnAPIUpdate").attr("disabled", true);
                                                }
                                            }
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPIUsername"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" readonly id="APIUsername" name="APIUsername" value="<%= EscapeUtils.CheckTextNull(rs[0][0].NAME)%>"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIUsername").text(global_fm_Username);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPIPasword"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" type="password" id="APIPassword" name="APIPassword" <%= isCheckAllow == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIPasword").text(global_fm_Password);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPIRemarkDesc"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteAPIRemarkDesc"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="APIRemark" name="APIRemark" value="<%= sRemark %>" <%= isCheckAllow == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIRemarkDesc").text(global_fm_remark_vn);
                                            $("#idLblTitleNoteAPIRemarkDesc").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPIRemarkENDesc"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteAPIRemarkENDesc"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="APIRemarkEN" name="APIRemarkEN" value="<%= sRemarkEN%>" <%= isCheckAllow == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIRemarkENDesc").text(global_fm_remark_en);
                                            $("#idLblTitleNoteAPIRemarkENDesc").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-13" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPISignature"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteAPISignature"></label>
                                            </label>
                                            <div class="col-sm-10" style="padding-right: 10px;padding-left: 6px;">
                                                <textarea class="form-control123" id="APISignature" style="height: 85px;" name="APISignature" <%= isCheckAllow == false ? "disabled" : "" %>><%= sSignture %></textarea>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPISignature").text(branch_fm_api_signture);
                                            $("#idLblTitleNoteAPISignature").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleAPIPublicKeyPem"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteAPIPublicKeyPem"></label>
                                            </label>
                                            <div class="col-sm-10" style="padding-right: 10px;padding-left: 6px;">
                                                <textarea class="form-control123" id="APIPublicKeyPem" style="height: 85px;" name="APIPublicKeyPem" <%= isCheckAllow == false ? "disabled" : "" %>><%= sPublicKeyPem%></textarea>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleAPIPublicKeyPem").text(branch_fm_api_publishkey);
                                            $("#idLblTitleNoteAPIPublicKeyPem").text(global_fm_require_label);
                                        </script>
                                    </div>
                                </div>
                            </div>
                            <div role="tabpanel" class="tab-pane fade" id="tab_contentRest" aria-labelledby="home-tab">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleRESTCredentialEdits"></label></h2>
                                    <script>$("#idLblTitleRESTCredentialEdits").text(branch_fm_rest_tag_credential);</script>
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;">
                                            <input id="btnRESTUpdate" class="btn btn-info" type="button" onclick="onRESTUpdate('<%= anticsrf%>');" />
                                            <script>
                                                document.getElementById("btnRESTUpdate").value = global_fm_button_edit;
                                            </script>
                                        </li>
                                    </ul>
                                    <div class="clearfix"></div>
                                    <script>
                                        function onRESTUpdate(idCSRF)
                                        {
                                            if (!JSCheckEmptyField($("#RestAPIUsername").val()))
                                            {
                                                $("#RestAPIUsername").focus();
                                                funErrorAlert(policy_req_empty + global_fm_Username);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#RestAPIRemark").val()))
                                            {
                                                $("#RestAPIRemark").focus();
                                                funErrorAlert(policy_req_empty + global_fm_remark_vn);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#RestAPIRemarkEN").val()))
                                            {
                                                $("#RestAPIRemarkEN").focus();
                                                funErrorAlert(policy_req_empty + global_fm_remark_en);
                                                return false;
                                            }
                                            if (!JSCheckEmptyField($("#RestAPITime").val().replace(/,/g, '')))
                                            {
                                                $("#RestAPITime").focus();
                                                funErrorAlert(policy_req_empty + branch_fm_expire_token);
                                                return false;
                                            } else {
                                                if (!JSCheckNumericField($("#RestAPITime").val().replace(/,/g, ''))) {
                                                    $("#RestAPITime").focus();
                                                    funErrorAlert(branch_fm_expire_token + policy_req_number);
                                                    return false;
                                                }
                                            }
                                            if (!JSCheckEmptyField($("#RestAPISecret").val()))
                                            {
                                                $("#RestAPISecret").focus();
                                                funErrorAlert(policy_req_empty + branch_fm_secretkey);
                                                return false;
                                            }
                                            var sCheckAPIAllow = "0";
                                            if ($("#idCheckRestAPIAllow").is(':checked'))
                                            {
                                                sCheckAPIAllow = "1";
                                            }
                                            $('body').append('<div id="over"></div>');
                                            $(".loading-gif").show();
                                            $.ajax({
                                                type: "post",
                                                url: "../BranchCommon",
                                                data: {
                                                    idParam: 'editrestfulbranch',
                                                    id: $("#idID").val(),
                                                    sRestAPIUsername: $("#RestAPIUsername").val(),
                                                    sRestAPIPassword: $("#RestAPIPassword").val(),
                                                    sRestAPITime: $("#RestAPITime").val(),
                                                    sRestAPISecret: $("#RestAPISecret").val(),
                                                    sRestAPIRemark: $("#RestAPIRemark").val(),
                                                    sRestAPIRemarkEN: $("#RestAPIRemarkEN").val(),
                                                    sCheckAPIAllow: sCheckAPIAllow,
                                                    CsrfToken: idCSRF
                                                },
                                                cache: false,
                                                success: function (html)
                                                {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0")
                                                    {
                                                        $("#RestAPIPassword").val('');
                                                        funSuccNoLoad(inputcertlist_succ_edit);
                                                    } else if(myStrings[0] === "PASS_NULL")
                                                    {
                                                        funErrorAlert(policy_req_empty + global_fm_Password);
                                                    }
                                                    else if(myStrings[0] === "TIME_FAIL")
                                                    {
                                                        funErrorAlert(branch_fm_expire_token + policy_req_number);
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
                                <div class="x_content">
                                    <%
                                        String sSecretKey = "";
                                        String sRemarkRest = "";
                                        String sRemarkENRest = "";
                                        String sExpirationTime = "";
                                        oMapperParse = new ObjectMapper();
                                        String sJSON_REST = EscapeUtils.CheckTextNull(rs[0][0].REST_JWT_PROPERTIES);
                                        boolean isCheckAllowRest = false;
                                        if(!"".equals(sJSON_REST))
                                        {
                                            isCheckAllowRest = true;
                                            RESTJWTSecureProperties itemParse = oMapperParse.readValue(sJSON_REST, RESTJWTSecureProperties.class);
                                            sSecretKey = EscapeUtils.CheckTextNull(itemParse.secretKey);
                                            String sTime = EscapeUtils.CheckTextNull(itemParse.expirationTime);
                                            if(!"".equals(sTime)){
                                                sExpirationTime = com.convertMoneyAnotherZero(Integer.parseInt(sTime));
                                            }
                                            sRemarkRest = EscapeUtils.CheckTextNull(itemParse.remark);
                                            sRemarkENRest = EscapeUtils.CheckTextNull(itemParse.remarkEn);
                                        }
                                        String sCheckAllowRest = "0";
                                        if(isCheckAllowRest == true) {
                                            sCheckAllowRest = "1";
                                        }
                                    %>
                                    <div class="col-sm-13" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label id="idLblTitleRestAPIAllow" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                            <div class="col-sm-10" style="padding-right: 0px;">
                                                <label class="switch" for="idCheckRestAPIAllow" style="margin-bottom: 0;">
                                                    <input type="checkbox" name="idCheckRestAPIAllow" id="idCheckRestAPIAllow" <%= isCheckAllowRest == true ? "checked" : "" %> onclick="onClickRestAllow();"/>
                                                    <div id="idCheckRestAPIAllowClass" class="slider round"></div>
                                                </label>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPIAllow").text(branch_fm_api_allow_access);
                                            function onClickRestAllow()
                                            {
                                                if ($("#idCheckRestAPIAllow").is(':checked'))
                                                {
                                                    $("#RestAPIPassword").attr("disabled", false);
                                                    $("#RestAPITime").attr("disabled", false);
                                                    $("#RestAPISecret").attr("disabled", false);
                                                    $("#RestAPIRemark").attr("disabled", false);
                                                    $("#RestAPIRemarkEN").attr("disabled", false);
                                                } else {
                                                    $("#RestAPIPassword").attr("disabled", true);
                                                    $("#RestAPITime").attr("disabled", true);
                                                    $("#RestAPISecret").attr("disabled", true);
                                                    $("#RestAPIRemark").attr("disabled", true);
                                                    $("#RestAPIRemarkEN").attr("disabled", true);
                                                }
                                            }
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPIUsername"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" readonly id="RestAPIUsername" name="RestAPIUsername" value="<%= EscapeUtils.CheckTextNull(rs[0][0].NAME)%>"/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPIUsername").text(global_fm_Username);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPIPasword"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" type="password" id="RestAPIPassword" name="RestAPIPassword" <%= isCheckAllowRest == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPIPasword").text(global_fm_Password);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPIRemarkDesc"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteRestAPIRemarkDesc"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="RestAPIRemark" name="RestAPIRemark" value="<%= sRemarkRest%>" <%= isCheckAllowRest == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPIRemarkDesc").text(global_fm_remark_vn);
                                            $("#idLblTitleNoteRestAPIRemarkDesc").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPIRemarkENDesc"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteRestAPIRemarkENDesc"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="RestAPIRemarkEN" name="RestAPIRemarkEN" value="<%= sRemarkENRest%>" <%= isCheckAllowRest == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPIRemarkENDesc").text(global_fm_remark_en);
                                            $("#idLblTitleNoteRestAPIRemarkENDesc").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPITime"></label>
                                                <label class="CssRequireField" id="idLblTitleRestNoteAPITime"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="RestAPITime" value="<%= sExpirationTime%>" name="RestAPITime" <%= isCheckAllowRest == false ? "disabled" : "" %> />
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPITime").text(branch_fm_expire_token);
                                            $("#idLblTitleRestNoteAPITime").text(global_fm_require_label);
                                        </script>
                                    </div>
                                    <div class="col-sm-6" style="padding-left: 0;">
                                        <div class="form-group">
                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                <label id="idLblTitleRestAPISecret"></label>
                                                <label class="CssRequireField" id="idLblTitleNoteRestAPISecret"></label>
                                            </label>
                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                <input class="form-control123" id="RestAPISecret" value="<%= sSecretKey%>" name="RestAPISecret" <%= isCheckAllowRest == false ? "disabled" : "" %>/>
                                            </div>
                                        </div>
                                        <script>
                                            $("#idLblTitleRestAPISecret").text(branch_fm_secretkey);
                                            $("#idLblTitleNoteRestAPISecret").text(global_fm_require_label);
                                        </script>
                                    </div>
                                </div>
                            </div>
                            <div role="tabpanel" class="tab-pane fade" id="tab_contentIP" aria-labelledby="home-tab">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleIPEdits"></label></h2>
                                    <script>$("#idLblTitleIPEdits").text(branch_fm_api_tag_ip);</script>
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;"></li>
                                    </ul>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <fieldset class="scheduler-border" style="clear: both;">
                                        <legend class="scheduler-border" id="idLblIPGroupIP"></legend>
                                        <script>$("#idLblIPGroupIP").text(branch_fm_api_tag_ip);</script>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <label id="idLblTitleAPIIP" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <input id="IP_ADD" name="IP_ADD" class="form-control123"/>
                                                </div>
                                            </div>
                                            <script>$("#idLblTitleAPIIP").text(global_fm_ip);</script>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <input id="btnAPIIPAdd" class="btn btn-info" type="button" onclick="onFormIPAdd('<%= anticsrf%>');" />
                                                    <script>
                                                        document.getElementById("btnAPIIPAdd").value = global_fm_button_New;
                                                    </script>
                                                </div>
                                                <div class="col-sm-7" style="padding-right: 0px;">

                                                </div>
                                            </div>
                                            <script>
                                                function onFormIPAdd()
                                                {
                                                    if (!JSCheckEmptyField($("#IP_ADD").val()))
                                                    {
                                                        $("#IP_ADD").focus();
                                                        funErrorAlert(policy_req_empty + global_fm_ip);
                                                        return false;
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'addippolicybranch',
                                                            sID: $("#idID").val(),
                                                            sIPCert: $("#IP_ADD").val()
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            var myStrings = sSpace(html).split('#');
                                                            if (myStrings[0] === "0")
                                                            {
                                                                onLoadIP();
                                                                funSuccNoLoad(inputcertlist_succ_add);
                                                            } else if (myStrings[0] === "IP_EXISTS"){
                                                                funErrorAlert(relyparty_exists_add_ip);
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
                                                function onLoadIP()
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadippolicybranch'
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
                                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                                        var isChecked = "";
                                                                        if(obj[i].ACTIVE === "1")
                                                                        {
                                                                            isChecked = "checked";
                                                                        }
                                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onchange=\"onIPActive('"+obj[i].NAME+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                                        contentProps += "<tr>" +
                                                                            "<td>" + obj[i].NO + "</td>" +
                                                                            "<td>" + obj[i].NAME + "</td>" +
                                                                            "<td>" + sActiveHTML + "</td>" +
                                                                            "</tr>";
                                                                        sCount++;
                                                                    }
                                                                    $("#idTemplateAssignIP").append(contentProps);
                                                                    if (sCount > 0)
                                                                    {
                                                                        $('#greenIP').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                                        </div>
                                        <%
                                            CERTIFICATION_POLICY_DATA[][] resIPData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessIPPolicyAll");
                                            if(resIPData != null && resIPData[0].length > 0) {
                                                int j=1;
                                        %>
                                        <div class="table-responsive" style="clear: both;">
                                            <table id="tblCertUseIP" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th id="idLblTitleTableIPSTT"></th>
                                                <th id="idLblTitleTableIPName"></th>
                                                <th id="idLblTitleTableIPActive"></th>
                                                <script>
                                                    $("#idLblTitleTableIPSTT").text(global_fm_STT);
                                                    $("#idLblTitleTableIPName").text(global_fm_ip);
                                                    $("#idLblTitleTableIPActive").text(global_fm_active);
                                                </script>
                                                </thead>
                                                <tbody id="idTemplateAssignIP">
                                                    <%
                                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_IP_LIST))
                                                            {
                                                    %>
                                                    <tr>
                                                        <td><%= j%></td>
                                                        <td><%= resIPData1.name%></td>
                                                        <td>
                                                            <label class="switch" for="idCheckIP<%=j%>" style="margin-bottom: 0;">
                                                                <input onclick="onIPActive('<%=resIPData1.name%>', 'idCheckIP<%=j%>');" type="checkbox" name="idCheckIP<%=j%>" id="idCheckIP<%=j%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                                <div id="idCheckIPClass<%=j%>" class="slider round"></div>
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
                                            <div id="greenIP" style="margin: 5px 0 5px 0;"> </div>
                                            <script>
                                                if (parseInt('<%=j%>')-1 > 0)
                                                {
                                                    $('#greenIP').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                                }
                                                function onIPActive(ipCode, activeKey)
                                                {
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    var isIPActive = "0";
                                                    if ($("#"+activeKey).is(':checked'))
                                                    {
                                                        isIPActive = "1";
                                                    }
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'activeippolicybranch',
                                                            sID: $("#idID").val(),
                                                            sIPActive: isIPActive,
                                                            sIPCode: ipCode
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
                                    <fieldset class="scheduler-border" style="clear: both;">
                                        <legend class="scheduler-border" id="idLblIPGroupOther"></legend>
                                        <script>$("#idLblIPGroupOther").text(token_group_other);</script>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <label id="idLblTitleIPAll" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                <div class="col-sm-6" style="padding-right: 0px;">
                                                    <label class="switch" for="idCheckIPAll" style="margin-bottom: 0;">
                                                        <input type="checkbox" name="idCheckIPAll" id="idCheckIPAll" <%= booAccessIPAll == true ? "checked" : ""%>/>
                                                        <div id="idCheckIPAllClass" class="slider round"></div>
                                                    </label>
                                                </div>
                                            </div>
                                            <script>
                                                $("#idLblTitleIPAll").text('<%=remarkAccessIPAll%>');
                                                function onOtherIPActive()
                                                {
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    var isIPActive = "0";
                                                    if ($("#idCheckIPAll").is(':checked'))
                                                    {
                                                        isIPActive = "1";
                                                    }
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'activeipallpolicybranch',
                                                            sID: $("#idID").val(),
                                                            sCheckIPAll: isIPActive
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
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <div class="col-sm-6" style="padding-right: 0px;text-align: left;padding-left: 0;">
                                                    <input id="btnIPOtherAdd" class="btn btn-info" type="button" onclick="onOtherIPActive();" />
                                                    <script>
                                                        document.getElementById("btnIPOtherAdd").value = global_fm_button_edit;
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-right: 0px;">

                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
                                </div>
                            </div>
                            
                            <div role="tabpanel" class="tab-pane fade" id="tab_contentFunction" aria-labelledby="home-tab">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleFunctionEdits"></label></h2>
                                    <script>$("#idLblTitleFunctionEdits").text(branch_fm_api_tag_function);</script>
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;"></li>
                                    </ul>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <fieldset class="scheduler-border" style="clear: both;">
                                        <legend class="scheduler-border" id="idLblFunctionGroupFunction"></legend>
                                        <script>$("#idLblFunctionGroupFunction").text(branch_fm_api_tag_function);</script>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <label id="idLblTitleAPIFunction" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                <div class="col-sm-10" style="padding-right: 0px;">
                                                    <select name="FUNCTION_ADD" id="FUNCTION_ADD" class="form-control123">
                                                        <%
                                                            FUNCTIONALITY[][] rssFunction = new FUNCTIONALITY[1][];
                                                            db.S_BO_API_FUNCTIONALITY_COMBOBOX(sessLanguageGlobal, rssFunction);
                                                            if (rssFunction[0].length > 0) {
                                                                for (int i = 0; i < rssFunction[0].length; i++) {
                                                        %>
                                                        <option value="<%=rssFunction[0][i].NAME%>"><%=rssFunction[0][i].NAME + " (" +rssFunction[0][i].REMARK + ")"%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <input id="btnAPIFunctionAdd" class="btn btn-info" type="button" onclick="onFormFunctionAdd('<%= anticsrf%>');" />
                                                    <script>
                                                        document.getElementById("btnAPIFunctionAdd").value = global_fm_button_New;
                                                    </script>
                                                </div>
                                                <div class="col-sm-7" style="padding-right: 0px;">

                                                </div>
                                            </div>
                                            <script>
                                                function onFormFunctionAdd()
                                                {
                                                    if (!JSCheckEmptyField($("#FUNCTION_ADD").val()))
                                                    {
                                                        $("#FUNCTION_ADD").focus();
                                                        funErrorAlert(policy_req_empty_choose + global_fm_Function);
                                                        return false;
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'addfunctionpolicybranch',
                                                            sID: $("#idID").val(),
                                                            sFunctionCert: $("#FUNCTION_ADD").val()
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            var myStrings = sSpace(html).split('#');
                                                            if (myStrings[0] === "0")
                                                            {
                                                                onLoadFunction();
                                                                funSuccNoLoad(inputcertlist_succ_add);
                                                            } else if (myStrings[0] === "FUNCTION_EXISTS"){
                                                                funErrorAlert(global_exists_add_function);
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
                                                function onLoadFunction()
                                                {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadfunctionpolicybranch'
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idTemplateAssignFunction").empty();
                                                                    var contentProps = "";
                                                                    var sCount = 1;
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                                        var isChecked = "";
                                                                        if(obj[i].ACTIVE === "1")
                                                                        {
                                                                            isChecked = "checked";
                                                                        }
                                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onclick=\"onFunctionActive('"+obj[i].NAME+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                                        contentProps += "<tr>" +
                                                                            "<td>" + obj[i].NO + "</td>" +
                                                                            "<td>" + obj[i].NAME + "</td>" +
                                                                            "<td>" + sActiveHTML + "</td>" +
                                                                            "</tr>";
                                                                        sCount++;
                                                                    }
                                                                    $("#idTemplateAssignFunction").append(contentProps);
                                                                    if (sCount > 0)
                                                                    {
                                                                        $('#greenFunction').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseFunction', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                                        </div>
                                        <%
                                            CERTIFICATION_POLICY_DATA[][] resFunctionData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessFunctionPolicyAll");
                                            if(resFunctionData[0].length > 0) {
                                                int j=1;
                                        %>
                                        <div class="table-responsive" style="clear: both;">
                                            <table id="tblCertUseFunction" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th id="idLblTitleTableFunctionSTT"></th>
                                                <th id="idLblTitleTableFunctionName"></th>
                                                <th id="idLblTitleTableFunctionActive"></th>
                                                <script>
                                                    $("#idLblTitleTableFunctionSTT").text(global_fm_STT);
                                                    $("#idLblTitleTableFunctionName").text(global_fm_Function);
                                                    $("#idLblTitleTableFunctionActive").text(global_fm_active);
                                                </script>
                                                </thead>
                                                <tbody id="idTemplateAssignFunction">
                                                    <%
                                                        for(CERTIFICATION_POLICY_DATA resFunctionData1 : resFunctionData[0]) {
                                                            if(resFunctionData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_FUNCTION_LIST))
                                                            {
                                                    %>
                                                    <tr>
                                                        <td><%= j%></td>
                                                        <td><%= resFunctionData1.name%></td>
                                                        <td>
                                                            <label class="switch" for="idCheckFunction<%=j%>" style="margin-bottom: 0;">
                                                                <input onclick="onFunctionActive('<%=resFunctionData1.name%>', 'idCheckFunction<%=j%>');" type="checkbox" name="idCheckFunction<%=j%>" id="idCheckFunction<%=j%>" <%= resFunctionData1.enabled == true ? "checked" : ""%>/>
                                                                <div id="idCheckFunctionClass<%=j%>" class="slider round"></div>
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
                                            <div id="greenFunction" style="margin: 5px 0 5px 0;"> </div>
                                            <script>
                                                if (parseInt('<%=j%>')-1 > 0)
                                                {
                                                    $('#greenFunction').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseFunction', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                                }
                                                function onFunctionActive(ipCode, activeKey)
                                                {
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    var isFunctionActive = "0";
                                                    if ($("#"+activeKey).is(':checked'))
                                                    {
                                                        isFunctionActive = "1";
                                                    }
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'activefunctionpolicybranch',
                                                            sID: $("#idID").val(),
                                                            sFunctionActive: isFunctionActive,
                                                            sFunctionCode: ipCode
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
                                    <fieldset class="scheduler-border" style="clear: both;">
                                        <legend class="scheduler-border" id="idLblFunctionGroupOther"></legend>
                                        <script>$("#idLblFunctionGroupOther").text(token_group_other);</script>
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <label id="idLblTitleFunctionAll" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                <div class="col-sm-6" style="padding-right: 0px;">
                                                    <label class="switch" for="idCheckFunctionAll" style="margin-bottom: 0;">
                                                        <input type="checkbox" name="idCheckFunctionAll" id="idCheckFunctionAll" <%= booAutoFunctionAll == true ? "checked" : ""%>/>
                                                        <div id="idCheckFunctionAllClass" class="slider round"></div>
                                                    </label>
                                                </div>
                                            </div>
                                            <script>
                                                $("#idLblTitleFunctionAll").text('<%=remarkAutoFunctionAll %>');
                                                function onOtherFunctionActive()
                                                {
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    var isFunctionActive = "0";
                                                    if ($("#idCheckFunctionAll").is(':checked'))
                                                    {
                                                        isFunctionActive = "1";
                                                    }
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../BranchCommon",
                                                        data: {
                                                            idParam: 'activefunctionallpolicybranch',
                                                            sID: $("#idID").val(),
                                                            sCheckFunctionAll: isFunctionActive
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
                                        <div class="col-sm-6" style="padding-left: 0;">
                                            <div class="form-group">
                                                <div class="col-sm-6" style="padding-right: 0px;text-align: left;padding-left: 0;">
                                                    <input id="btnFunctionOtherAdd" class="btn btn-info" type="button" onclick="onOtherFunctionActive();" />
                                                    <script>
                                                        document.getElementById("btnFunctionOtherAdd").value = global_fm_button_edit;
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-right: 0px;">

                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
                                </div>
                            </div>          
                        </div>
                    </div>
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