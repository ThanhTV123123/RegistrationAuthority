<%-- 
    Document   : CertificateTypeEdit
    Created on : Oct 10, 2019, 11:11:24 AM
    Author     : THANH-PC
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(certtype_title_edit);
                $("#idLblTitleNoData").text(global_no_data);
                $("#idLblTitleCode").text(certtype_fm_code);
                $("#idLblTitleDesc").text(global_fm_remark_vn);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblNoteDesc").text(global_fm_require_label);
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshCertType'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertificateTypeList.jsp";
                        }
                        else
                        {
                            window.location = "CertificateTypeList.jsp";
                        }
                    }
                });
                return false;
            }
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.RemarkEN.value))
                {
                    document.myname.RemarkEN.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_en);
                    return false;
                }
                var sCheckActiveFlag = "0";
                if ($("#ActiveFlag").is(':checked'))
                {
                    sCheckActiveFlag = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CertificateTypeCommon",
                    data: {
                        idParam: 'editcertificatetype',
                        id: document.myname.cityid.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.RemarkEN.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
//                            localStorage.setItem("EDIT_PROVINCE", document.myname.cityid.value);
                            funSuccAlert(certtype_succ_edit, "CertificateTypeList.jsp");
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
                            if (myStrings[1] === "1") {
                                funErrorAlert(certtype_exists_code);
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
        </script>
    </head>
    <body>
        <%
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String sessLanguageGlobal = session.getAttribute("sessVN").toString();
            session.setAttribute("SessComponentCertTypeAdd",null);
            session.setAttribute("SessComponentFileProfileAdd",null);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                        <input type="button" id="btnClose" class="btn btn-info" onclick="closeForm('<%=anticsrf%>');"/>
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
                    CERTIFICATION_PURPOSE[][] rs = new CERTIFICATION_PURPOSE[1][];
                    try {
                        String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                        if (EscapeUtils.IsInteger(ids) == true) {
                            db.S_BO_CERTIFICATION_PURPOSE_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                            if (rs[0].length > 0) {
                                String strDesc = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                                String strDescEN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                                String strPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                                String strFILE_PROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].FILE_PROPERTIES);
                %>
                <form name="myname" method="post" class="form-horizontal">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <div class="form-group" style="padding: 0;">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="hidden" name="cityid" value="<%= rs[0][0].ID%>" />
                                    <input type="text" readonly="true" name="citycode" class="form-control123" value="<%= rs[0][0].NAME%>" />
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleDesc"></label>
                                    <label class="CssRequireField" id="idLblNoteDesc"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input class="form-control123" value="<%= strDesc%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleDescEN"></label>
                                    <label class="CssRequireField" id="idLblNoteDescEN"></label>
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input class="form-control123" value="<%= strDescEN%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="RemarkEN" name="RemarkEN"/>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleDescEN").text(global_fm_remark_en);
                                $("#idLblNoteDescEN").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <label class="switch" for="ActiveFlag">
                                        <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked='checked'" : ""%> />
                                        <div class="slider round"></div>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
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
                                    <input type="text" readonly="true" name="dates" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
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
                                    <input type="text" readonly="true" name="UpdateDate" class="form-control123" value="<%= rs[0][0].MODIFIED_DT%>" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <fieldset class="scheduler-border" style="clear: both;padding-top: 10px;">
                        <legend class="scheduler-border" id="idLblTitleGroupComponent"><span></span></legend>
                        <script>
                            $("#idLblTitleGroupComponent").text(certlist_title_detail);
                        </script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleCertTypeCode"></label>
                                    <label id="idLblTitleAPINoteFunction" class="CssRequireField"></label>
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <input class="form-control123" id="CERT_FIELDCODE" name="CERT_FIELDCODE"/>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypeCode").text(certtype_component_field_code);
                                $("#idLblTitleAPINoteFunction").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertTypePrefix" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <input class="form-control123" id="CERT_PREFIX" name="CERT_PREFIX"/>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypePrefix").text(global_fm_prefix);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleCertTypeRemarkVN"></label>
                                    <label id="idLblTitleCertTypeNoteRemarkVN" class="CssRequireField"></label>
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <input class="form-control123" id="CERT_DESC_VN" name="CERT_DESC_VN"/>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypeRemarkVN").text(global_fm_remark_vn);
                                $("#idLblTitleCertTypeNoteRemarkVN").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                    <label id="idLblTitleCertTypeRemarkEN"></label>
                                    <label class="CssRequireField" id="idLblTitleCertTypeNoteRemarkEN"></label>
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <input class="form-control123" id="CERT_DESC_EN" name="CERT_DESC_EN"/>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypeRemarkEN").text(global_fm_remark_en);
                                $("#idLblTitleCertTypeNoteRemarkEN").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertTypeRequire" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <label class="switch" for="idCERT_REQUIRE">
                                        <input type="checkbox" name="idCERT_REQUIRE" id="idCERT_REQUIRE" checked />
                                        <div class="slider round"></div>
                                    </label>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypeRequire").text(global_fm_required_input);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertTypeAttrType" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <select name="CERT_ATTRIBUTE_TYPE" id="CERT_ATTRIBUTE_TYPE" class="form-control123">
                                        <option value="TEXT_FIELD" id="CERT_ATTRIBUTE_TYPE_TEXT"></option>
                                        <option value="UID_LIST/COMPANY" id="CERT_ATTRIBUTE_TYPE_COMPANY"></option>
                                        <option value="UID_LIST/PERSONAL" id="CERT_ATTRIBUTE_TYPE_PERSONAL"></option>
                                    </select>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCertTypeAttrType").text(certtype_component_attributetype);
                                $("#CERT_ATTRIBUTE_TYPE_TEXT").text(certtype_fm_component_text);
                                $("#CERT_ATTRIBUTE_TYPE_COMPANY").text(certtype_fm_component_uuid_company);
                                $("#CERT_ATTRIBUTE_TYPE_PERSONAL").text(certtype_fm_component_uuid_personal);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertTypeCNType" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <select name="CERT_CN_TYPE" id="CERT_CN_TYPE" class="form-control123">
                                        <option value="" id="CERT_CN_TYPE_CHOOSE"></option>
                                        <option value="COMPANY" id="CERT_CN_TYPE_COMPANY"></option>
                                        <option value="PERSON" id="CERT_CN_TYPE_PERSON"></option>
                                    </select>
                                </div>
                            </div>
                            <script>
                                $("#CERT_CN_TYPE_CHOOSE").text(global_fm_combox_empty);
                                $("#CERT_CN_TYPE_COMPANY").text(global_fm_grid_company);
                                $("#CERT_CN_TYPE_PERSON").text(global_fm_grid_personal);
                                $("#idLblTitleCertTypeCNType").text(certtype_component_cntype);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="btnAPIFunctionAdd" class="btn btn-info" type="button" onclick="onFormFunctionAdd('<%= anticsrf%>');" />
                                    <script>
                                        document.getElementById("btnAPIFunctionAdd").value = global_fm_button_New;
                                    </script>
                                </div>
                                <div class="col-sm-7" style="padding-right: 0px;"></div>
                            </div>
                            <script>
                                function onFormFunctionAdd()
                                {
                                    if (!JSCheckEmptyField($("#CERT_FIELDCODE").val()))
                                    {
                                        $("#CERT_FIELDCODE").focus();
                                        funErrorAlert(policy_req_empty + certtype_component_field_code);
                                        return false;
                                    }
                                    if (!JSCheckEmptyField($("#CERT_DESC_VN").val()))
                                    {
                                        $("#CERT_DESC_VN").focus();
                                        funErrorAlert(policy_req_empty + global_fm_remark_vn);
                                        return false;
                                    }
                                    if (!JSCheckEmptyField($("#CERT_DESC_EN").val()))
                                    {
                                        $("#CERT_DESC_EN").focus();
                                        funErrorAlert(policy_req_empty + global_fm_remark_en);
                                        return false;
                                    }
                                    var isCERT_REQUIRE = "0";
                                    if ($("#idCERT_REQUIRE").is(':checked'))
                                    {
                                        isCERT_REQUIRE = "1";
                                    }
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../CertificateTypeCommon",
                                        data: {
                                            idParam: 'addcomponentcerttypeadd',
                                            idCERT_FIELDCODE: $("#CERT_FIELDCODE").val(),
                                            idCERT_PREFIX: $("#CERT_PREFIX").val(),
                                            idCERT_DESC_VN: $("#CERT_DESC_VN").val(),
                                            idCERT_DESC_EN: $("#CERT_DESC_EN").val(),
                                            idCERT_REQUIRE: isCERT_REQUIRE,
                                            idCERT_ATTRIBUTE_TYPE: $("#CERT_ATTRIBUTE_TYPE").val(),
                                            idCERT_CN_TYPE: $("#CERT_CN_TYPE").val()
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                $("#CERT_FIELDCODE").val('');
                                                $("#CERT_PREFIX").val('');
                                                $("#CERT_DESC_VN").val('');
                                                $("#CERT_DESC_EN").val('');
                                                $("#CERT_ATTRIBUTE_TYPE").val('TEXT_FIELD');
                                                $("#CERT_CN_TYPE").val('');
                                                onLoadFunction();
                                                funSuccNoLoad(token_succ_edit);
                                            } else if (myStrings[0] === "FIELDCODE_EXISTS") {
                                                funErrorAlert(certtype_component_field_code_exists);
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
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                    return false;
                                }
                                function onFormFunctionRequire(idFieldCode, idRequire)
                                {
                                    var isCheck = "0";
                                    if ($("#" + idRequire).is(':checked'))
                                    {
                                        isCheck = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../CertificateTypeCommon",
                                        data: {
                                            idParam: 'requirecomponentcerttypeadd',
                                            idCERT_FIELDCODE: idFieldCode,
                                            idRequire: isCheck
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0") {
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
                                function onFormFunctionDelete(idFieldCode)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../CertificateTypeCommon",
                                        data: {
                                            idParam: 'deletecomponentcerttypeadd',
                                            idCERT_FIELDCODE: idFieldCode
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onLoadFunction();
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
                                            idParam: 'loadcomponentcerttypeadd'
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
                                                        if(obj[i].REQUIRE === "1")
                                                        {
                                                            isChecked = "checked";
                                                        }
                                                        var isRequireDisabled = "";
                                                        if(obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY
                                                            || obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                        {
                                                            isRequireDisabled="disabled";
                                                        }
                                                        var sATTRIBUTE_TYPE = "";
                                                        if(obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                        {
                                                            sATTRIBUTE_TYPE = certtype_fm_component_uuid_company;
                                                        } else if(obj[i].ATTRIBUTE_TYPE === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                        {
                                                            sATTRIBUTE_TYPE = certtype_fm_component_uuid_personal;
                                                        } else {
                                                            sATTRIBUTE_TYPE = certtype_fm_component_text;
                                                        }
                                                        var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                            btn-xs" onclick="onFormFunctionDelete(\'' + obj[i].NAME + '\');">\n\
                                                            <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                        var sRequire = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+isRequireDisabled+" class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' /><div class='slider round "+isRequireDisabled+"'></div></label>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].NAME + "</td>" +
                                                            "<td>" + obj[i].PREFIX + "</td>" +
                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                            "<td>" + sRequire + "</td>" +
                                                            "<td>" + sATTRIBUTE_TYPE + "</td>" +
                                                            "<td>" + sActionCRL + "</td>" +
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
                        <div class="table-responsive" style="clear: both;">
                            <table id="tblCertUseFunction" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableFunctionSTT"></th>
                                <th id="idLblTitleTableFunctionName"></th>
                                <th id="idLblTitleTableFunctionPrefix"></th>
                                <th id="idLblTitleTableFunctionDesc"></th>
                                <th id="idLblTitleTableFunctionRequire"></th>
                                <th id="idLblTitleTableFunctionAttriType"></th>
                                <th id="idLblTitleTableFunctionAction"></th>
                                <script>
                                    $("#idLblTitleTableFunctionSTT").text(global_fm_STT);
                                    $("#idLblTitleTableFunctionName").text(certtype_component_field_code);
                                    $("#idLblTitleTableFunctionPrefix").text(global_fm_prefix);
                                    $("#idLblTitleTableFunctionDesc").text(global_fm_Description);
                                    $("#idLblTitleTableFunctionRequire").text(global_fm_required);
                                    $("#idLblTitleTableFunctionAttriType").text(certtype_component_attributetype);
                                    $("#idLblTitleTableFunctionAction").text(global_fm_action);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignFunction">
                                    <%
                                        CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];//(CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                        CommonFunction.getJsonComponentForCert(strPROPERTIES, resProfileData);
                                        if(resProfileData != null) {
                                            session.setAttribute("SessComponentCertTypeAdd", resProfileData);
                                            if(resProfileData[0].length > 0) {
                                                int j = 1;
                                                for(CERTIFICATION_TYPE_COMPONENT resProfileItem : resProfileData[0]) {
                                                    String sRequireID = resProfileItem.name + String.valueOf(j);
                                                    String isRequireDisabled = "";
                                                    if(EscapeUtils.CheckTextNull(resProfileItem.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                        || EscapeUtils.CheckTextNull(resProfileItem.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                    {
                                                        isRequireDisabled = "disabled";
                                                    }
                                    %>
                                    <tr>
                                        <td><%= String.valueOf(j)%></td>
                                        <td><%= EscapeUtils.CheckTextNull(resProfileItem.name)%></td>
                                        <td><%= EscapeUtils.CheckTextNull(resProfileItem.prefix)%></td>
                                        <td><%= "1".equals(sessLanguageGlobal) ? EscapeUtils.CheckTextNull(resProfileItem.remark) : EscapeUtils.CheckTextNull(resProfileItem.remarkEn)%></td>
                                        <td>
                                            <label class="switch" for="<%=sRequireID%>">
                                                <input TYPE='checkbox' class='js-switch' <%= isRequireDisabled%> data-switchery='true' <%= resProfileItem.require == true ? "checked" : "" %>
                                                    onclick="onFormFunctionRequire('<%= EscapeUtils.CheckTextNull(resProfileItem.name)%>', '<%= sRequireID%>');" id="<%= sRequireID%>" />
                                                <div class='slider round <%= isRequireDisabled%>'></div>
                                            </label>
                                        </td>
                                        <td>
                                            <%
                                                sRequireID = sRequireID.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                            %>
                                            <span id="span<%=sRequireID%>"></span>
                                            <script>
                                                var vValueType = '<%=EscapeUtils.CheckTextNull(resProfileItem.attributeType)%>';
                                                if(vValueType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_COMPANY)
                                                {
                                                    $("#span<%=sRequireID%>").append(certtype_fm_component_uuid_company);
                                                } else if(vValueType === JS_STR_COMPONENT_DN_VALUE_UID_COMBOBOX_LIST_PERSONAL)
                                                {
                                                    $("#span<%=sRequireID%>").append(certtype_fm_component_uuid_personal);
                                                } else {
                                                    $("#span<%=sRequireID%>").append(certtype_fm_component_text);
                                                }
                                            </script>
                                        </td>
                                        <td>
                                            <a id="idLblTitleProfileLinkDelete<%= String.valueOf(j)%>" style="cursor: pointer;" class="btn btn-info btn-xs" onclick="onFormFunctionDelete('<%= resProfileItem.name%>');"></a>
                                            <script>
                                                $("#idLblTitleProfileLinkDelete"+'<%= String.valueOf(j)%>').append('<i class="fa fa-pencil"></i>' + global_fm_button_delete);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                                    j++;
                                                }
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                            <div id="greenFunction" style="margin: 5px 0 5px 0;"></div>
                        </div>
                    </fieldset>
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleGroupFile"><span></span></legend>
                        <script>$("#idLblTitleGroupFile").text(certtype_group_file_profile);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleFileName" class="control-label col-sm-3" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-9" style="padding-right: 0px;">
                                    <select name="FILE_PROFILE" id="FILE_PROFILE" class="form-control123">
                                        <%
                                            FILE_PROFILE[][] fileTypeInfo = new FILE_PROFILE[1][];
                                            db.S_BO_FILE_PROFILE_COMBOBOX(fileTypeInfo);
                                            if (fileTypeInfo[0].length > 0) {
                                                for (FILE_PROFILE temp1 : fileTypeInfo[0]) {
                                                    String sRemark = "1".equals(sessLanguageGlobal) ? temp1.REMARK : temp1.REMARK_EN;
                                        %>
                                        <option value="<%=temp1.NAME + "###" + temp1.REMARK + "###" + temp1.REMARK_EN%>"><%=temp1.NAME + " - " + sRemark %></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitleFileName").text(certtype_fm_file);</script>
                        </div>
                        <div class="col-sm-4" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleFileRequire" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-6" style="padding-right: 0px;">
                                    <label class="switch" for="idFILE_REQUIRE">
                                        <input type="checkbox" name="idFILE_REQUIRE" id="idFILE_REQUIRE" checked />
                                        <div class="slider round"></div>
                                    </label>
                                </div>
                            </div>
                            <script>$("#idLblTitleFileRequire").text(global_fm_required_input);</script>
                        </div>
                        <div class="col-sm-2" style="padding-left: 0;">
                            <div class="form-group">
                                <input id="btnFileManagerAdd" class="btn btn-info" type="button" onclick="onFormFileManagerAdd('<%= anticsrf%>');" />
                                <script>
                                    document.getElementById("btnFileManagerAdd").value = global_fm_button_New;
                                </script>
                            </div>
                        </div>
                        <script>
                            function onFormFileManagerAdd()
                            {
                                var isFILE_REQUIRE = "0";
                                if ($("#idFILE_REQUIRE").is(':checked'))
                                {
                                    isFILE_REQUIRE = "1";
                                }
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                $.ajax({
                                    type: "post",
                                    url: "../CertificateTypeCommon",
                                    data: {
                                        idParam: 'addfileprofileadd',
                                        idFILE_PROFILE: $("#FILE_PROFILE").val(),
                                        isFILE_REQUIRE: isFILE_REQUIRE
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0")
                                        {
                                            onLoadFileManager();
                                            funSuccNoLoad(token_succ_edit);
                                        } else if (myStrings[0] === "FILECODE_EXISTS") {
                                            funErrorAlert(certtype_file_code_exists);
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
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }
                            function onFormFileRequire(idFieldCode, idRequire)
                            {
                                var isCheck = "0";
                                if ($("#" + idRequire).is(':checked'))
                                {
                                    isCheck = "1";
                                }
                                $.ajax({
                                    type: "post",
                                    url: "../CertificateTypeCommon",
                                    data: {
                                        idParam: 'requirefileprofileadd',
                                        idFILE_PROFILE: idFieldCode,
                                        isFILE_REQUIRE: isCheck
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0") {
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
                            function onFormFileManagerDelete(idFieldCode)
                            {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                $.ajax({
                                    type: "post",
                                    url: "../CertificateTypeCommon",
                                    data: {
                                        idParam: 'deletefileprofileadd',
                                        idFILE_PROFILE_CODE: idFieldCode
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        var myStrings = sSpace(html).split('#');
                                        if (myStrings[0] === "0")
                                        {
                                            onLoadFileManager();
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
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
                                });
                                return false;
                            }
                            function onLoadFileManager()
                            {
                                $.ajax({
                                    type: "post",
                                    url: "../JSONCommon",
                                    data: {
                                        idParam: 'loadfileprofileadd'
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        if (html.length > 0)
                                        {
                                            var obj = JSON.parse(html);
                                            if (obj[0].Code === "0")
                                            {
                                                $("#idTemplateAssignFileProfile").empty();
                                                var contentProps = "";
                                                var sCount = 1;
                                                for (var i = 0; i < obj.length; i++) {
                                                    var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                    var isChecked = "";
                                                    if(obj[i].REQUIRE === "1")
                                                    {
                                                        isChecked = "checked";
                                                    }
                                                    var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                        btn-xs" onclick="onFormFileManagerDelete(\'' + obj[i].NAME + '\');">\n\
                                                        <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                    var sRequire = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' disabled class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' /><div class='slider round'></div></label>";
                                                    contentProps += "<tr>" +
                                                        "<td>" + obj[i].NO + "</td>" +
                                                        "<td>" + obj[i].NAME + "</td>" +
                                                        "<td>" + obj[i].REMARK + "</td>" +
                                                        "<td>" + sRequire + "</td>" +
                                                        "<td>" + sActionCRL + "</td>" +
                                                        "</tr>";
                                                    sCount++;
                                                }
                                                $("#idTemplateAssignFileProfile").append(contentProps);
                                                if (sCount > 0)
                                                {
                                                    $('#greenFileProfile').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseFileProfile', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                        <div class="table-responsive" style="clear: both;">
                            <table id="tblCertUseFileProfile" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableFileSTT"></th>
                                <th id="idLblTitleTableFileName"></th>
                                <th id="idLblTitleTableFileDesc"></th>
                                <th id="idLblTitleTableFileRequire"></th>
                                <th id="idLblTitleTableFileAction"></th>
                                <script>
                                    $("#idLblTitleTableFileSTT").text(global_fm_STT);
                                    $("#idLblTitleTableFileName").text(certtype_file_code);
                                    $("#idLblTitleTableFileDesc").text(global_fm_Description);
                                    $("#idLblTitleTableFileRequire").text(global_fm_required);
                                    $("#idLblTitleTableFileAction").text(global_fm_action);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignFileProfile">
                                    <%
                                        FILE_PROFILE_JSON.Attribute[][] resFileData = new FILE_PROFILE_JSON.Attribute[1][];//(CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                        CommonFunction.getJsonFilePropertiesForCert(strFILE_PROPERTIES, resFileData);
                                        if(resFileData != null) {
                                            session.setAttribute("SessComponentFileProfileAdd", resFileData);
                                            if(resFileData[0].length > 0) {
                                                int j = 1;
                                                for(FILE_PROFILE_JSON.Attribute resFileItem : resFileData[0]) {
                                                    String sRequireID = resFileItem.getName() + String.valueOf(j);
                                    %>
                                    <tr>
                                        <td><%= String.valueOf(j)%></td>
                                        <td><%= EscapeUtils.CheckTextNull(resFileItem.getName())%></td>
                                        <td><%= "1".equals(sessLanguageGlobal) ? EscapeUtils.CheckTextNull(resFileItem.getRemark()) : EscapeUtils.CheckTextNull(resFileItem.getRemarkEn())%></td>
                                        <td>
                                            <label class='switch' for="<%= sRequireID%>">
                                                <input TYPE='checkbox' class='js-switch' data-switchery='true' <%=resFileItem.getIsRequire() == true ? "checked" : "" %>
                                                    onclick="onFormFileRequire('<%= EscapeUtils.CheckTextNull(resFileItem.getName())%>', '<%= sRequireID%>');" id="<%= sRequireID%>" />
                                                <div class='slider round'></div>
                                            </label>
                                        </td>
                                        <td>
                                            <a id="idLblTitleFileLinkDelete<%= String.valueOf(j)%>" style="cursor: pointer;" class="btn btn-info btn-xs" onclick="onFormFileManagerDelete('<%= resFileItem.getName()%>');"></a>
                                            <script>
                                                $("#idLblTitleFileLinkDelete"+'<%= String.valueOf(j)%>').append('<i class="fa fa-pencil"></i>' + global_fm_button_delete);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                                    j++;
                                                }
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                            <div id="greenFileProfile" style="margin: 5px 0 5px 0;"> </div>
                        </div>
                    </fieldset>
                </form>
                <%
                } else {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
                </div>
                <%
                    }
                } else {
                %>
                <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                    <label style="color: red;" id="idLblTitleNoData"></label>
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
        </div>            
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>