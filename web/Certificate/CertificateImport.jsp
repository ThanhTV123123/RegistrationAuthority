<%-- 
    Document   : CertificateImport
    Created on : Aug 21, 2019, 10:00:18 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = cogCommon.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = certimport_title_list;
            $(document).ready(function () {
                $('#demo1').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#demo2').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
            });
            function calUpload(idCSRF)
            {
                var input1 = document.getElementById('input-file');
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                    {
                        $.ajax({
                            type: "post",
                            url: '../TokenCommon',
                            data: {
                                idParam: 'checkcsrfforcertificateimport',
                                CERTIFICATION_AUTHORITY: document.formimport.CERTIFICATION_AUTHORITY.value,
                                CERTIFICATION_TYPE: document.formimport.CERTIFICATION_TYPE.value,
//                                CERTIFICATION_PURPOSE: document.formimport.CERTIFICATION_PURPOSE.value,
//                                PKI_FORMFACTOR: document.formimport.PKI_FORMFACTOR.value,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    file1 = input1.files[0];
                                    var data1 = new FormData();
                                    data1.append('file', file1);
                                    $.ajax({
                                        type: 'POST',
                                        url: '../RegisterCertificateImport',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            var arr = sSpace(html).split('###');
                                            if (arr[0] === "0")
                                            {
                                                $("#idViewResult").css("display","");
                                                $('#idSumInsert').text(arr[1]);
                                                $('#idSumError').text(arr[2]);
                                                goToByScroll("idViewResult");
                                                if(arr[3] !== "")
                                                {
                                                    $("#idShowError").css("display","");
                                                } else{
                                                    $("#idShowError").css("display","none");
                                                }
                                                funSuccNoLoad(token_succ_import);
                                            }
                                            else if (arr[0] === JS_EX_EXCEL_NOT_SIZE)
                                            {
                                                funErrorAlert(certimport_error_not_size + arr[2]);
                                            }
                                            else if (arr[0] === JS_EX_EXCEL_NOT_CA)
                                            {
                                                funErrorAlert(certimport_error_not_ca);
                                            }
                                            else if (arr[0] === JS_EX_EXCEL_FORMAT_INVALIE)
                                            {
                                                funErrorAlert(certimport_error_not_format_file);
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
                                                if (arr[1] === JS_EX_SPECIAL)
                                                {
                                                    funErrorAlert(global_error_file_special);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                {
                                                    funErrorAlert(certimport_file_format_invalid);
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
                                }
                                else
                                {
                                    funCsrfAlert();
                                }
                            }
                        });
                    }
                    else
                    {
                        funErrorAlert(token_error_import_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
            function downloadErrorFile()
            {
                $.ajax({
                    type: "post",
                    url: "../DownloadFileCSR",
                    data: {
                        idParam: "savefileimportregistercert"
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var f = document.formimportResult;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfileimporttokenerror&name=' + arr[1];
                            f.submit();
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN) {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === "1") {
                            funErrorAlert(global_error_export_excel);
                        }
                        else if (arr[0] === "2") {
                            funErrorAlert(global_succ_NoResult);
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
            function downloadSampleCert(idType)
            {
                var vRequestType = $("#CERTIFICATION_TYPE").val();
                var idParamCall;
                if(vRequestType === JS_STR_SERVICE_TYPE_ID_REGISTRATION) {
                    idParamCall = "downfilesampleregistercert";
                } else if(vRequestType === JS_STR_SERVICE_TYPE_ID_RENEWAL) {
                    idParamCall = "downfilesamplerenewcert";
                } else if(vRequestType === JS_STR_SERVICE_TYPE_ID_CHANGEINFO) {
                    idParamCall = "downfilesamplechangecert";
                } else if(vRequestType === JS_STR_SERVICE_TYPE_ID_SUSPEND) {
                    idParamCall = "downfilesamplessuspend";
                } else {
                    funErrorAlert(cert_fm_type_request + global_error_invalid);
                    return false;
                }
//                if(idType === "CNDN") {
//                    idParamCall = "downfilesamplestaff";
//                } else if(idType === "CN") {
//                    idParamCall = "downfilesamplepersonal";
//                } else if(idType === "DN") {
//                    idParamCall = "downfilesamplepersonal";
//                } else {
//                    idParamCall = "downfilesampleregistercert";
//                }
                var f = document.formimport;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=' + idParamCall;
                f.submit();
            }
        </script>
        <style>
            .projects th{font-weight: bold;}
            .x_panel {
                padding:10px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
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
                        document.getElementById("idNameURL").innerHTML =certimport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                try {
                                    String strCADefault="";
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
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certimport_title_import);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" class="btn btn-info" onClick="calUpload('<%=anticsrf%>');"><script>document.write(global_fm_button_import);</script></button>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimport" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_browse_file);</script></label>
                                                <input type="file" id="input-file" accept=".xlsx,.xls,.csv" style="width: 100%;"
                                                    class="btn btn-default btn-file select_file">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleType" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_TYPE" id="CERTIFICATION_TYPE" class="form-control123"
                                                                onchange="LOAD_CERTIFICATION_TYPE(this.value);">
                                                                <%
                                                                    SERVICE_TYPE[][] rsType = new SERVICE_TYPE[1][];
                                                                    db.S_BO_SERVICE_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (int i = 0; i < rsType[0].length; i++) {
                                                                            if(rsType[0][i].ID == Definitions.CONFIG_SERVICE_TYPE_ID_REGISTRATION
                                                                                || rsType[0][i].ID == Definitions.CONFIG_SERVICE_TYPE_ID_RENEWAL
                                                                                || rsType[0][i].ID == Definitions.CONFIG_SERVICE_TYPE_ID_CHANGEINFO)
                                                                            {
                                                                %>
                                                                <option value="<%= String.valueOf(rsType[0][i].ID)%>"><%=rsType[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                                <option value="5"><script>document.write(certlist_title_suspend);</script></option>
                                                            </select>
                                                        </div>
                                                        <script>
                                                            function LOAD_CERTIFICATION_TYPE(obj) {
                                                                if(obj === JS_STR_SERVICE_TYPE_ID_REGISTRATION)
                                                                {
                                                                    $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
                                                                    $("#CERTIFICATION_AUTHORITY").attr("disabled", false);
                                                                } else {
                                                                    $("#CERTIFICATION_AUTHORITY").attr("disabled", true);
                                                                    $("select#CERTIFICATION_AUTHORITY").prop('selectedIndex', 0);
                                                                }
                                                            }
                                                        </script>
                                                    </div>
                                                    <script>$("#idLblTitleType").text(cert_fm_type_request);</script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label id="idLblTitleCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123">
                                                                <%
//                                                                    String sFristCA = "";
                                                                    CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                                    if (rssProfile[0].length > 0) {
                                                                        int intCAIDDefault = CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                                        for (int i = 0; i < rssProfile[0].length; i++) {
//                                                                            if(rssProfile[0][i].ID == intCAIDDefault)
//                                                                            {
//                                                                                if("".equals(sFristCA)) {
//                                                                                    sFristCA = String.valueOf(rssProfile[0][i].ID);
//                                                                                }
//                                                                            }
                                                                %>
                                                                <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                                    + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT) + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].NAME)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <script>$("#idLblTitleCA").text(global_fm_ca);</script>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                                <label class="control-label"><script>document.write(token_fm_import_sample);</script></label>
                                                &nbsp;<a style="cursor: pointer;" onclick="return downloadSampleCert('ALL');"><script>document.write(global_fm_down);</script></a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="x_panel" id="idViewResult" style="display: none;">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tokenimport_fm_result);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimportResult" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding-bottom: 10px;margin: 0;color: #000000;">
                                                <script>document.write(global_fm_combox_success);</script>: <label id="idSumInsert"></label>
                                            </div>
                                            <div class="form-group" style="padding: 0px;margin: 0;color: #000000; display: none;" id="idShowError">
                                                <script>document.write(certimport_fm_error);</script> <label id="idSumError"></label>&nbsp;&nbsp;
                                                <script>document.write(error_content_link_download);</script> <a onclick="downloadErrorFile();" style="cursor: pointer; text-decoration: underline; color: blue;"><script>document.write(error_content_link_out);</script></a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <%
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
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
        <%        } else {
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
