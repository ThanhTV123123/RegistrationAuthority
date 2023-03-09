<%-- 
    Document   : TokenImport
    Created on : Jun 28, 2018, 8:54:33 AM
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
            Config conf = new Config();
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
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
            document.title = tokenimport_title_list;
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
                $("#idBtnUpload").prop("disabled",true);
//                $("#idViewResult").css("display", "");
//                $("#idSumInsert").val("12");
//                $("#idSumUpdate").val("0");
//                $("#idSumError").val("2");
//                $('#idSumInsert').text("a");
//                $('#idSumUpdate').text("ad");
//                $('#idSumError').text("124");
            });
            function calUploadCheck(idCSRF)
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
                                idParam: 'checkcsrf',
                                TOKEN_VERSION: document.formimport.TOKEN_VERSION.value,
                                BranchID: document.formimport.BranchID.value,
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
                                        url: '../TokenImport',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            var arr = sSpace(html).split('###');
                                            if (arr[0] === "0")
                                            {
                                                console.log("arr[4]: " + arr[4]);
                                                if(arr[4] !== "")
                                                {
                                                    $("#idViewResult").css("display","");
    //                                                $('#idSumInsert').text(arr[1]);
    //                                                $('#idSumUpdate').text(arr[3]);
                                                    $('#idSumError').text(arr[2]);
                                                    $("#idShowError").css("display","");
                                                    funErrorAlert(token_error_check_import);
                                                    goToByScroll("idViewResult");
                                                } else {
                                                    $("#idViewResult").css("display","none");
                                                    $("#idShowError").css("display","none");
                                                    $("#idBtnUpload").prop("disabled",false);
                                                    console.log("idBtnUpload: disabled");
                                                    funSuccNoLoad(token_succ_check_import);
                                                }
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
                                                else if (arr[1] === "NO_FORMAT")
                                                {
                                                    funErrorAlert(token_error_import_format);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                {
                                                    funErrorAlert(token_error_no_tokenid);
                                                }
                                                else if (arr[1] === JS_EX_CSV_NO_SOPIN)
                                                {
                                                    funErrorAlert(token_error_no_sopin);
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
                                } else if (arr[0] === "10")
                                {
                                    funErrorAlert(global_req_all);
                                } else if (arr[0] === "1")
                                {
                                    funErrorAlert(token_fm_agent + global_error_invalid);
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
            
            function calUploadInsert()
            {
                document.getElementById('input-file').value = '';
                $.ajax({
                    type: "post",
                    url: "../TokenCommon",
                    data: {
                        idParam: "importtokenbundle"
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0") {
                            $("#idBtnUpload").prop("disabled",true);
//                            funSuccNoLoad(token_succ_setup + ". UUID: " + arr[1]);
                            funSuccNoLoad(token_succ_setup);
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
            
            function downloadErrorFile()
            {
                $.ajax({
                    type: "post",
                    url: "../DownloadFileCSR",
                    data: {
                        idParam: "savefileimporterror"
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
            function downloadSampleToken()
            {
                var f = document.formimport;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downfilesamplesim';
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
                session.setAttribute("sessTokenImportBundle", null);
                session.setAttribute("sessTokenImportFailed", null);
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
                        document.getElementById("idNameURL").innerHTML = tokenimport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                try {
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tokenimport_title_import);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <button type="button" id="idBtnCheck" class="btn btn-info" onClick="calUploadCheck('<%=anticsrf%>');"><script>document.write(global_fm_button_check);</script></button>
                                                <button id="idBtnUpload" type="button" class="btn btn-info" onClick="calUploadInsert('<%=anticsrf%>');"><script>document.write(global_fm_button_setup);</script></button>
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
                                                <!--onchange="calUpload('<= anticsrf%>');"--> 
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-3" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"><script>document.write(token_fm_version);</script></label>
                                                        <div class="col-sm-9" style="padding-right: 0px;">
                                                            <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123">
                                                                <%
                                                                    TOKEN_VERSION[][] rstState = new TOKEN_VERSION[1][];
                                                                    db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstState);
                                                                    if (rstState[0].length > 0) {
                                                                        for (TOKEN_VERSION temp1 : rstState[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"><script>document.write(token_fm_agent);</script></label>
                                                        <div class="col-sm-10" style="padding-right: 0px;">
                                                            <select name="BranchID" id="BranchID" class="form-control123">
                                                                <%
//                                                                    BRANCH[][] rsNoNull = new BRANCH[1][];
                                                                    try {
//                                                                        db.S_BO_PARENT_BRANCH_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                                                        BRANCH[][] rsNoNull = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                        if (rsNoNull[0].length > 0) {
                                                                            for (int i = 0; i < rsNoNull[0].length; i++) {
                                                                                if(String.valueOf(rsNoNull[0][i].PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                    if(rsNoNull[0][i].ID != 1) {
                                                                                        String sValueParent = String.valueOf(rsNoNull[0][i].ID);
                                                                %>
                                                                <option value="<%=sValueParent%>"><%= rsNoNull[0][i].NAME + " - " + rsNoNull[0][i].REMARK%></option>
                                                                <%
                                                                                }
                                                                            }
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
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                                <label class="control-label"><script>document.write(token_fm_import_sample);</script></label> <a style="cursor: pointer;" onclick="return downloadSampleToken();"><script>document.write(global_fm_down);</script></a>
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
<!--                                            <div class="form-group" style="padding-bottom: 10px;margin: 0;color: #000000;">
                                                <script>document.write(token_succ_import_insert_replace);</script> <label id="idSumInsert"></label>
                                            </div>
                                            <div class="form-group" style="padding-bottom: 10px;margin: 0;color: #000000;">
                                                <script>document.write(token_succ_import_update_replace);</script> <label id="idSumUpdate"></label>
                                            </div>-->
                                            <div class="form-group" style="padding: 0px;margin: 0;color: #000000; display: none;" id="idShowError">
                                                <script>document.write(token_succ_import_error_replace);</script> <label id="idSumError"></label>&nbsp;&nbsp;
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
