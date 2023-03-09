<%-- 
    Document   : CertificateTypeNew
    Created on : Oct 10, 2019, 11:11:34 AM
    Author     : THANH-PC
--%>

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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = city_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function onBlurToUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + city_fm_code);
                    return false;
                } else {
                    if (JSCheckSpaceField(document.myname.citycode.value))
                    {
                        document.myname.citycode.focus();
                        funErrorAlert(city_fm_code + global_req_no_space);
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
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CertificateTypeCommon",
                    data: {
                        idParam: 'addcertificatetype',
                        citycode: document.myname.citycode.value,
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
                            funSuccAlert(certtype_succ_add, "CertificateTypeList.jsp");
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
        </script>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                session.setAttribute("SessComponentCertTypeAdd", null);
                session.setAttribute("SessComponentFileProfileAdd", null);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
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
                        document.getElementById("idNameURL").innerHTML = certtype_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certtype_title_add);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" onclick="closeForm();" type="button" />
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
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"><script>document.write(certtype_fm_code);</script>
                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-6" style="padding-right: 0px;">
                                                        <input type="text" oninput="onBlurToUppercase(this);" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>" name="citycode" class="form-control123" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"><script>document.write(global_fm_remark_vn);</script>
                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-6" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"><script>document.write(global_fm_remark_en);</script>
                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                    </label>
                                                    <div class="col-sm-6" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <fieldset class="scheduler-border" style="clear: both;padding-top: 10px;">
                                                <legend class="scheduler-border"><span><script>document.write(certlist_title_detail);</script></span></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(certtype_component_field_code);</script>
                                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input class="form-control123" id="CERT_FIELDCODE" name="CERT_FIELDCODE"/>
                                                        </div>
                                                    </div>
                                                    <!--<script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>-->
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(global_fm_prefix);</script>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input class="form-control123" id="CERT_PREFIX" name="CERT_PREFIX"/>
                                                        </div>
                                                    </div>
                                                    <!--<script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>-->
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(global_fm_remark_vn);</script>
                                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input class="form-control123" id="CERT_DESC_VN" name="CERT_DESC_VN"/>
                                                        </div>
                                                    </div>
                                                    <!--<script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>-->
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(global_fm_remark_en);</script>
                                                            <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <input class="form-control123" id="CERT_DESC_EN" name="CERT_DESC_EN"/>
                                                        </div>
                                                    </div>
                                                    <!--<script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>-->
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(global_fm_required_input);</script>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <label class="switch" for="idCERT_REQUIRE">
                                                                <input type="checkbox" name="idCERT_REQUIRE" id="idCERT_REQUIRE" checked />
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <!--<script>$("#idLblTitleAPIFunction").text(global_fm_Function);</script>-->
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(certtype_component_attributetype);</script>
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
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(certtype_component_cntype);</script>
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
                                                                                var sActionCRL = '<a style="cursor: pointer;" class="btn btn-info\n\
                                                                                    btn-xs" onclick="onFormFunctionDelete(\'' + obj[i].NAME + '\');">\n\
                                                                                    <i class="fa fa-pencil"></i> ' + global_fm_button_delete + '</a>';
                                                                                var sRequire = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' disabled class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' /><div class='slider round'></div></label>";
                                                                                contentProps += "<tr>" +
                                                                                    "<td>" + obj[i].NO + "</td>" +
                                                                                    "<td>" + obj[i].NAME + "</td>" +
                                                                                    "<td>" + obj[i].PREFIX + "</td>" +
                                                                                    "<td>" + obj[i].REMARK + "</td>" +
                                                                                    "<td>" + sRequire + "</td>" +
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
                                                <div class="table-responsive">
                                                <table id="tblCertUseFunction" class="table table-bordered table-striped projects">
                                                    <thead>
                                                    <th id="idLblTitleTableFunctionSTT"></th>
                                                    <th id="idLblTitleTableFunctionName"></th>
                                                    <th id="idLblTitleTableFunctionPrefix"></th>
                                                    <th id="idLblTitleTableFunctionDesc"></th>
                                                    <th id="idLblTitleTableFunctionRequire"></th>
                                                    <th id="idLblTitleTableFunctionAction"></th>
                                                    <script>
                                                        $("#idLblTitleTableFunctionSTT").text(global_fm_STT);
                                                        $("#idLblTitleTableFunctionName").text(certtype_component_field_code);
                                                        $("#idLblTitleTableFunctionPrefix").text(global_fm_prefix);
                                                        $("#idLblTitleTableFunctionRequire").text(global_fm_required);
                                                        $("#idLblTitleTableFunctionDesc").text(global_fm_Description);
                                                        $("#idLblTitleTableFunctionAction").text(global_fm_action);
                                                    </script>
                                                    </thead>
                                                    <tbody id="idTemplateAssignFunction">
                                                    </tbody>
                                                </table>
                                                <div id="greenFunction" style="margin: 5px 0 5px 0;"> </div>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><span><script>document.write(certtype_group_file_profile);</script></span></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-3" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(certtype_fm_file);</script>
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
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleAPIFunction" class="control-label col-sm-6" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <script>document.write(global_fm_required_input);</script>
                                                        </label>
                                                        <div class="col-sm-6" style="padding-right: 0px;">
                                                            <label class="switch" for="idFILE_REQUIRE">
                                                                <input type="checkbox" name="idFILE_REQUIRE" id="idFILE_REQUIRE" checked />
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
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
                                                <div class="table-responsive">
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
                                                    </tbody>
                                                </table>
                                                <div id="greenFileProfile" style="margin: 5px 0 5px 0;"> </div>
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