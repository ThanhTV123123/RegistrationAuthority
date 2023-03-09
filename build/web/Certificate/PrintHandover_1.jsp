<%-- 
    Document   : PrintHandover
    Created on : Aug 3, 2018, 1:40:44 PM
    Author     : THANH-PC
--%>

<%@page import="java.net.URLEncoder"%>
<%-- 
    Document   : CertView
    Created on : Jul 27, 2018, 11:42:16 AM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
//            document.title = certlist_title_print_hadover;
            $(document).ready(function () {
                $('#TO_DATE').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
//                $('.loading-gif').hide();
//                if(localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST") !== null && localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "RenewCertList.jsp";
//                }
            });
            function printex() {
                $('#contentPrintCert1').printArea({
                    popWd: 750,
                    popHt: 900,
                    mode: "popup",
                    popClose: false
                });
            }
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=850,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function popupPrint(id, idCSRF)
            {
                /*if (!JSCheckEmptyField($("#BRANCH_FULLNAME").val()))
                {
                    $("#BRANCH_FULLNAME").focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }*/
                if (JSCheckEmptyField($("#BRANCH_PHONE").val()))
                {
                    if (!JSCheckFormatPhoneNew_EditOne($("#BRANCH_PHONE")))
                    {
                        $("#BRANCH_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
        
//                if (!JSCheckEmptyField($("#TO_COMPANY").val()))
//                {
//                    $("#TO_COMPANY").focus();
//                    funErrorAlert(policy_req_empty + global_fm_company);
//                    return false;
//                }

                /*if (!JSCheckEmptyField($("#TO_FULLNAME").val()))
                {
                    $("#TO_FULLNAME").focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }*/
//                if (!JSCheckEmptyField($("#TO_CMND").val()))
//                {
//                    $("#TO_CMND").focus();
//                    funErrorAlert(policy_req_empty + global_fm_CMND + '/' + global_fm_HC);
//                    return false;
//                }
//                if (!JSCheckEmptyField($("#TO_DATE").val()))
//                {
//                    $("#TO_DATE").focus();
//                    funErrorAlert(policy_req_empty + global_fm_cmnd_date);
//                    return false;
//                }
                /*if (!JSCheckEmptyField($("#TO_ADDRESS").val()))
                {
                    $("#TO_ADDRESS").focus();
                    funErrorAlert(policy_req_empty + global_fm_address);
                    return false;
                }
                if (!JSCheckEmptyField($("#TO_PHONE").val()))
                {
                    $("#TO_PHONE").focus();
                    funErrorAlert(policy_req_empty + global_fm_phone);
                    return false;
                } else {
                    if (!JSCheckFormatPhoneNew_EditOne($("#TO_PHONE")))
                    {
                        $("#TO_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }*/
                
                if (JSCheckEmptyField($("#TO_PHONE").val()))
                {
                    if (!JSCheckFormatPhoneNew_EditOne($("#TO_PHONE")))
                    {
                        $("#TO_PHONE").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
        
//                $('body').append('<div id="over"></div>');
//                $(".loading-gif").show();
                var vBRANCH_FULLNAME = $("#BRANCH_FULLNAME").val();
                if(sSpace(vBRANCH_FULLNAME) === "")
                {
                    vBRANCH_FULLNAME = "................................................";
                }
                var vBRANCH_ADDRESS = $("#BRANCH_ADDRESS").val();
                if(sSpace(vBRANCH_ADDRESS) === "")
                {
                    vBRANCH_ADDRESS = "................................................";
                }
                var vBRANCH_PHONE = $("#BRANCH_PHONE").val();
                if(sSpace(vBRANCH_PHONE) === "")
                {
                    vBRANCH_PHONE = "................................................";
                }
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printhadover',
                        id: id,
                        FROM_COMPANY: $("#FROM_COMPANY").val(),
                        BRANCH_FULLNAME: vBRANCH_FULLNAME,
                        BRANCH_ADDRESS: vBRANCH_ADDRESS,
                        BRANCH_PHONE: vBRANCH_PHONE,
//                        TO_COMPANY: $("#TO_COMPANY").val(),
                        TO_FULLNAME: $("#TO_FULLNAME").val(),
                        TO_CMND: $("#TO_CMND").val(),
                        TO_DATE: $("#TO_DATE").val(),
                        TO_PLACE: $("#TO_PLACE").val(),
                        TO_ADDRESS: $("#TO_ADDRESS").val(),
                        TO_PHONE: $("#TO_PHONE").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
//                            $(".loading-gif").hide();
//                            $('#over').remove();
                            PrintPreview(myStrings[1]);
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
                                funErrorAlert(certprofile_exists_code);
                            }
                            else {
                                funErrorAlert(global_errorsql);
                            }
                        }
//                        $(".loading-gif").hide();
//                        $('#over').remove();
                    }
                });
                return false;
            }
            function closeForm()
            {
//                localStorage.setItem("TransferPage", "list");
                if(localStorage.getItem("TransferPage") === "detail")
                {
                    window.location = "CertView.jsp?id=" + document.getElementById("sID").value;
                } else {
                    $.ajax({
                        type: "post",
                        url: "../UserCommon",
                        data: {
                            idParam: 'backformpage',
                            idSession: 'RefreshRenewCertSess'
                        },
                        cache: false,
                        success: function (html) {
                            var arr = sSpace(html);
                            if (arr === "0")
                            {
                                window.location = "RenewCertList.jsp";
                            }
                            else
                            {
                                window.location = "RenewCertList.jsp";
                            }
                        }
                    });
                    return false;
                }
            }
        </script>
        <style>
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 0px 10px;border-bottom:0px;
            }
        </style>
    </head>
    <body>
        <%         
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
        %>
<!--        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>-->
        <div class="x_panel">
            <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
//                                    String strCertificate = "";
                String strIsCert = "0";
                try {
                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
//                                            ids = seEncript.decrypt(ids);
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                        if (rs[0].length > 0) {
                            boolean isAccessAgencyPage = true;
                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                                if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                    isAccessAgencyPage = false;
//                                }
                            }
                            if (isAccessAgencyPage == true) {
                                String pBRANCH_NAME = "";
                                String pBRANCH_ADDRESS = "";
                                String pBRANCH_PHONE = "";
                                int Branch_ID = rs[0][0].BRANCH_ID;
                                BRANCH[][] rsBranch = new BRANCH[1][];
                                db.S_BO_BRANCH_DETAIL(String.valueOf(Branch_ID), rsBranch);
                                if(rsBranch[0].length > 0)
                                {
//                                    pBRANCH_NAME = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
//                                    if(!"1".equals(session.getAttribute("sessVN").toString().trim()))
//                                    {
//                                        pBRANCH_NAME = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK_EN);
//                                    }
//                                    pBRANCH_ADDRESS = EscapeUtils.CheckTextNull(rsBranch[0][0].ADDRESS);
//                                    pBRANCH_PHONE = EscapeUtils.CheckTextNull(rsBranch[0][0].MSISDN);
                                }
                                pBRANCH_NAME = "";
            %>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEditsHandover"></span></h2>
                <script>$("#idLblTitleEditsHandover").text(certlist_title_print_hadover);</script>
                <ul class="nav navbar-right panel_toolbox">
                    <li>
                        <input id="btnPrintCertHandover" class="btn btn-info" type="button" onclick="popupPrint('<%=ids%>', '<%= anticsrf%>');" />
                    </li>
                    <script>
                        document.getElementById("btnPrintCertHandover").value = global_fm_button_print;
                    </script>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <form name="mynameHandover" method="post" class="form-horizontal">
                    <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= URLEncoder.encode(seEncript.encrypt(String.valueOf(rs[0][0].ID)))%>">
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleGroupSender"></legend>
                        <script>$("#idLblTitleGroupSender").text(certlist_group_sender);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCompany" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="FROM_COMPANY" id="FROM_COMPANY" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_COMPANYNAME%>"
                                        value="<%= pBRANCH_NAME%>" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleCompany").text(global_fm_company);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleAgentName"></label>
                                    <!--<label class="CssRequireField" id="idLblNoteAgentName"></label>-->
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="BRANCH_FULLNAME" id="BRANCH_FULLNAME" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleAgentName").text(global_fm_fullname);
//                                $("#idLblNoteAgentName").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleAgentAddress" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="BRANCH_ADDRESS" id="BRANCH_ADDRESS" value="<%= pBRANCH_ADDRESS%>" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleAgentAddress").text(global_fm_address);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleAgentPhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="BRANCH_PHONE" id="BRANCH_PHONE" value="<%= pBRANCH_PHONE%>"
                                        class="form-control123" onblur="autoTrimTextField('BRANCH_PHONE', this.value);"/>
                                </div>
                            </div>
                            <script>$("#idLblTitleAgentPhone").text(global_fm_phone);</script>
                        </div>
                    </fieldset>
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleGroupReceive"></legend>
                        <script>$("#idLblTitleGroupReceive").text(certlist_group_receiver);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleFullname"></label>
                                    <!--<label class="CssRequireField" id="idLblNoteFullname"></label>-->
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_FULLNAME" id="TO_FULLNAME" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleFullname").text(global_fm_fullname);
//                                $("#idLblNoteFullname").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_CMND" id="TO_CMND" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleCMND").text(global_fm_personal_id);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_DATE" value="" id="TO_DATE" class="form-control123">
                                </div>
                            </div>
                            <script>$("#idLblTitleDate").text(global_fm_cmnd_date);</script>
                        </div>
                        <style>
                            .aba[readonly]{background-color:#ffffff;opacity:1}
                        </style>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitlePlace" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select name="TO_PLACE" id="TO_PLACE" class="form-control123">
                                        <option value="" id="idLblTitlePlaceAll"></option>
                                        <%
                                            CITY_PROVINCE[][] rsCity = new CITY_PROVINCE[1][];
                                            try {
                                                db.S_BO_PROVINCE_COMBOBOX(sessLanguageGlobal, rsCity);
                                                if (rsCity[0].length > 0) {
                                                    for (int i = 0; i < rsCity[0].length; i++) {
                                        %>
                                        <option value="<%=EscapeUtils.CheckTextNull(rsCity[0][i].REMARK) %>"><%=EscapeUtils.CheckTextNull(rsCity[0][i].REMARK) %></option>
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
                                    </select>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitlePlace").text(global_fm_place);
                                $("#idLblTitlePlaceAll").text(global_fm_combox_empty);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitleAddress"></label>
                                    <!--<label class="CssRequireField" id="idLblNoteAddress"></label>-->
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_ADDRESS" id="TO_ADDRESS" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleAddress").text(global_fm_address);
//                                $("#idLblNoteAddress").text(global_fm_require_label);
                            </script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                    <label id="idLblTitlePhone"></label>
                                    <!--<label class="CssRequireField" id="idLblNotePhone"></label>-->
                                </label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input type="text" name="TO_PHONE" id="TO_PHONE" onblur="autoTrimTextField('TO_PHONE', this.value);" class="form-control123">
                                </div>
                            </div>
                            <script>
                                $("#idLblTitlePhone").text(global_fm_phone);
//                                $("#idLblNotePhone").text(global_fm_require_label);
                            </script>
                        </div>
                    </fieldset>
                </form>
            </div>
            <%
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
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
        <!--<script src="../js/moment.min.js"></script>-->
        <script src="../js/daterangepicker.js"></script>
    </body>
</html>
