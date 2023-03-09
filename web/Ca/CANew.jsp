<%-- 
    Document   : CANew
    Created on : May 2, 2018, 9:57:02 AM
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
            document.title = ca_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + ca_fm_code);
                    return false;
                }
//                else {
//                    if (JSCheckSpaceField(document.myname.citycode.value))
//                    {
//                        document.myname.citycode.focus();
//                        funErrorAlert(ca_fm_code + global_req_no_space);
//                        return false;
//                    }
//                }
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
                if (!JSCheckEmptyField(document.myname.OCSP.value))
                {
                    document.myname.OCSP.focus();
                    funErrorAlert(policy_req_empty + ca_fm_OCSP);
                    return false;
                }
                else
                {
                    if (!JSCheckFormatURL(document.myname.OCSP.value)) {
                        document.myname.OCSP.focus();
                        funErrorAlert(global_req_format_http + ca_fm_OCSP);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.CRL.value))
                {
                    document.myname.CRL.focus();
                    funErrorAlert(policy_req_empty + ca_fm_CRL);
                    return false;
                }
                else
                {
                    if (!JSCheckFormatURL(document.myname.CRL.value)) {
                        document.myname.CRL.focus();
                        funErrorAlert(global_req_format_http + ca_fm_CRL);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.CRLPath.value))
                {
                    document.myname.CRLPath.focus();
                    funErrorAlert(policy_req_empty + ca_fm_CRLPath);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.URI.value))
                {
                    document.myname.URI.focus();
                    funErrorAlert(policy_req_empty + ca_fm_URI);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.certificate.value))
                {
                    document.myname.certificate.focus();
                    funErrorAlert(policy_req_empty + ca_fm_Cert_01);
                    return false;
                }
                var snameCheckOCSP = "0";
                if ($("#nameCheckOCSP").is(':checked')) {
                    snameCheckOCSP = "1";
                }
                var sNameUniqueDN = "0";
                if ($("#nameUniqueDN").is(':checked')) {
                    sNameUniqueDN = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CACommon",
                    data: {
                        idParam: 'addca',
                        citycode: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        OCSP: document.myname.OCSP.value,
                        CRL: document.myname.CRL.value,
                        CRLPath: document.myname.CRLPath.value,
                        strCertificate: document.myname.certificate.value,
                        URI: document.myname.URI.value,
                        nameCheckOCSP: snameCheckOCSP,
                        nameUniqueDN: sNameUniqueDN,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        $("#idUrlFile").val(sSpace(html));
                        var myStrings = $("#idUrlFile").val().split('#');
                        $("#idUrlFile").val('');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(ca_succ_add, "CAList.jsp");
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
                                funErrorAlert(ca_exists_code);
                            } else if (myStrings[1] === "2") {
                                funErrorAlert(ca_exists_name);
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
                        idSession: 'SessRefreshCA'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CAList.jsp";
                        }
                        else
                        {
                            window.location = "CAList.jsp";
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
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
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
                        document.getElementById("idNameURL").innerHTML = ca_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(ca_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" onclick="closeForm();" type="button" />
                                                <input id="idUrlFile" name="idUrlFile" type="text" style="display: none;"/>
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
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCode"></label>
                                                        <label id="idLblNoteCode"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" oninput="onBlurToUppercase(this);" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>"
                                                            name="citycode" class="form-control123" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCode").text(ca_fm_code);
                                                    $("#idLblNoteCode").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark"></label>
                                                        <label id="idLblNoteRemark"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark").text(global_fm_remark_vn);
                                                    $("#idLblNoteRemark").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleRemark_EN"></label>
                                                        <label id="idLblNoteRemark_EN"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                                                    $("#idLblNoteRemark_EN").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleOCSP"></label>
                                                        <label id="idLblNoteOCSP"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" class="form-control123" maxlength="100" id="OCSP" name="OCSP">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleOCSP").text(ca_fm_OCSP);
                                                    $("#idLblNoteOCSP").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCRLPath"></label>
                                                        <label id="idLblNoteCRLPath"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" class="form-control123" maxlength="300" id="CRLPath" name="CRLPath"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCRLPath").text(ca_fm_CRLPath);
                                                    $("#idLblNoteCRLPath").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCRL"></label>
                                                        <label id="idLblNoteCRL"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" class="form-control123" maxlength="300" id="CRL" name="CRL"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCRL").text(ca_fm_CRL);
                                                    $("#idLblNoteCRL").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleURI"></label>
                                                        <label id="idLblNoteURI"class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" class="form-control123" maxlength="300" id="URI" name="URI"/>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleURI").text(ca_fm_URI);
                                                    $("#idLblNoteURI").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCheckOCSP"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <label class="switch" for="nameCheckOCSP">
                                                            <input TYPE="checkbox" id="nameCheckOCSP" name="nameCheckOCSP" />
                                                            <div class="slider round"></div>
                                                        </label>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCheckOCSP").text(ca_fm_CheckOCSP);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;clear: both;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleUniqueDN"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <label class="switch" for="nameUniqueDN">
                                                            <input TYPE="checkbox" id="nameUniqueDN" name="nameUniqueDN" />
                                                            <div class="slider round"></div>
                                                        </label>
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleUniqueDN").text(ca_fm_unique_DN);
                                                </script>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;clear: both;">
                                                <label class="control-label123" id="idLblTitleCert"></label>
                                                <label class="CssRequireField" id="idLblNoteCert"></label>
                                                <textarea name="certificate" style="height: 85px;" id="certificate" class="form-control123"></textarea>
                                                <div style="width: 100%; height: 5px;"></div>
                                                <INPUT class="form-control123" id="input-file" NAME="xls_filename" accept=".cer,.txt,.pem"
                                                    TYPE="file" onchange="UploadCertificate(this);" />
                                                <div style="width: 100%; height: 4px;"></div>
                                                <a id="idAShow1" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCTS('<%=anticsrf%>');"><script>document.write(global_fm_detail);</script></a>&nbsp;
                                                <a id="idAHide1" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTS1();"><script>document.write(global_fm_hide);</script></a>
                                            </div>
                                            <script>
                                                $("#idLblTitleCert").text(ca_fm_Cert_01);
                                                $("#idLblNoteCert").text(global_fm_require_label);
                                            </script>
                                            <div id="idViewCSR1" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;">
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                                                    <script>$("#idLblTitleGroupCert").text(ca_group_cert);</script>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleCompany1"></label>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <textarea id="idCompany1" class="form-control123" readonly="true" name="idCompany1" style="height: 750px;"></textarea>
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleCompany1").text(global_fm_company);
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleIssuer1"></label>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <textarea id="idIssuer1" class="form-control123" readonly="true" name="idIssuer1" style="height: 120px;"></textarea>
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleIssuer1").text(global_fm_issue);
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleValid1"></label>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input id="idValid1" class="form-control123" type="text" readonly="true" />
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleValid1").text(global_fm_valid);
                                                        </script>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <label id="idLblTitleExpired1"></label>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input id="idExpired1" class="form-control123" type="text" readonly="true" />
                                                            </div>
                                                        </div>
                                                        <script>
                                                            $("#idLblTitleExpired1").text(global_fm_dateend);
                                                        </script>
                                                    </div>
                                                </fieldset>
                                            </div>
                                            <div id="idViewCSRNoData1" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;">
                                                <fieldset class="scheduler-border">
                                                    <legend class="scheduler-border" id="idLblTitleGroupCert1"></legend>
                                                    <script>$("#idLblTitleGroupCert1").text(ca_group_cert);</script>
                                                    <div class="form-group" style="padding: 0px;margin: 0;">
                                                        <label class="control-label123" id="idLblTitleNoCert"></label>
                                                        <script>$("#idLblTitleNoCert").text(ca_req_info_cert);</script>
                                                    </div>
                                                </fieldset>
                                            </div>
                                            <script>
                                                function UploadCertificate(input1)
                                                {
                                                    if (input1.value !== '')
                                                    {
                                                        var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                                                        if(checkFileName === "cer" || checkFileName === "txt" || checkFileName === "CER"
                                                            || checkFileName === "TXT" || checkFileName === "pem" || checkFileName === "PEM")
                                                        {
                                                            $('body').append('<div id="over"></div>');
                                                            $(".loading-gif").show();
                                                            file1 = input1.files[0];
                                                            var data1 = new FormData();
                                                            data1.append('file', file1);
                                                            $.ajax({
                                                                url: "../UploadFile",
                                                                data: data1,
                                                                cache: false,
                                                                contentType: false,
                                                                processData: false,
                                                                type: 'POST',
                                                                enctype: "multipart/form-data",
                                                                success: function (html) {
                                                                    var myStrings = sSpace(html).split('###');
                                                                    if (myStrings[0] === "0")
                                                                    {
                                                                        $("textarea#certificate").val(myStrings[1]);
                                                                    }
                                                                    else if (myStrings[0] === JS_EX_LOGIN)
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
                                                        }
                                                        else
                                                        {
                                                            funErrorAlert(global_req_cer_format);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        funErrorAlert(global_req_file);
                                                    }
                                                }
                                                function popupViewCTS(idCSRF)
                                                {
                                                    if (document.myname.certificate.value === "")
                                                    {
                                                        document.myname.certificate.focus();
                                                        funErrorAlert(policy_req_empty + ca_fm_Cert_01);
                                                        return false;
                                                    }
                                                    $('body').append('<div id="over"></div>');
                                                    $(".loading-gif").show();
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../CertificateParse",
                                                        data: {
                                                            sCert: $("#certificate").val(),
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            $("#idUrlFile").val(sSpace(html));
                                                            var myStrings = $("#idUrlFile").val().split('###');
                                                            $("#idUrlFile").val('');
                                                            if (myStrings[0] === "0")
                                                            {
                                                                $("#idCompany1").val(myStrings[1].replace(/, /g, "\n"));
                                                                $("#idIssuer1").val(myStrings[2].replace(/, /g, "\n"));
                                                                $("#idValid1").val(myStrings[3]);
                                                                $("#idExpired1").val(myStrings[4]);
                                                                document.getElementById('idViewCSR1').style.display = '';
                                                                document.getElementById('idViewCSRNoData1').style.display = 'none';
                                                                document.getElementById('idAHide1').style.display = '';
                                                                document.getElementById('idAShow1').style.display = 'none';
                                                            }
                                                            else if (myStrings[0] === "1") {
                                                                $("#idCompany1").val('');
                                                                $("#idIssuer1").val('');
                                                                $("#idValid1").val('');
                                                                $("#idExpired1").val('');
                                                                document.getElementById('idViewCSR1').style.display = 'none';
                                                                document.getElementById('idViewCSRNoData1').style.display = '';
                                                                document.getElementById('idAHide1').style.display = '';
                                                                document.getElementById('idAShow1').style.display = 'none';
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
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function popupHideCTS1()
                                                {
                                                    $("#idCompany1").val('');
                                                    $("#idIssuer1").val('');
                                                    $("#idValid1").val('');
                                                    $("#idExpired1").val('');
                                                    document.getElementById('idViewCSR1').style.display = 'none';
                                                    document.getElementById('idViewCSRNoData1').style.display = 'none';
                                                    document.getElementById('idAHide1').style.display = 'none';
                                                    document.getElementById('idAShow1').style.display = '';
                                                }
                                            </script>
                                            
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