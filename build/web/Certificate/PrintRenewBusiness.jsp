<%-- 
    Document   : PrintRenewBusiness
    Created on : Aug 7, 2018, 1:32:24 PM
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
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
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
//            document.title = certlist_title_print_register;
            $(document).ready(function () {
                $('#INFO_DATE_GRANT').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                localStorage.setItem("PrintRenewPersonal", null);
                localStorage.setItem("PrintRenewBusiness", null);
//                if(localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST") !== null && localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_RENEWCERTLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "../Admin/Home.jsp";
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
                if (!JSCheckEmptyField($("#INFO_REPRESEN").val()))
                {
                    $("#INFO_REPRESEN").focus();
                    funErrorAlert(policy_req_empty + global_fm_representative_legal);
                    return false;
                }
                if (!JSCheckEmptyField($("#INFO_ROLE").val()))
                {
                    $("#INFO_ROLE").focus();
                    funErrorAlert(policy_req_empty + global_fm_role);
                    return false;
                }
                if (!JSCheckEmptyField($("#INFO_CMND").val()))
                {
                    $("#INFO_CMND").focus();
                    funErrorAlert(policy_req_empty + global_fm_personal_id);
                    return false;
                }
                if (!JSCheckEmptyField($("#CONTACT_FULLNAME").val()))
                {
                    $("#CONTACT_FULLNAME").focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }
                if (!JSCheckEmptyField($("#CONTACT_ROLE").val()))
                {
                    $("#CONTACT_ROLE").focus();
                    funErrorAlert(policy_req_empty + global_fm_role);
                    return false;
                }
//                var sCheckCheckDKDN = "0";
//                if ($("#CheckDKDN").is(':checked')) {
//                    sCheckCheckDKDN = "1";
//                }
//                var sCheckCheckDT = "0";
//                if ($("#CheckDT").is(':checked')) {
//                    sCheckCheckDT = "1";
//                }
//                var sCheckCheckTL = "0";
//                if ($("#CheckTL").is(':checked')) {
//                    sCheckCheckTL = "1";
//                }
                var sChoose = document.getElementById("idSessIsDate").value;
                var sCheckCheckDKDN = "0";
                var sCheckCheckDT = "0";
                var sCheckCheckTL = "0";
                if(sChoose === "1"){
                    sCheckCheckDKDN = "1";
                    sCheckCheckDT = "0";
                    sCheckCheckTL = "0";
                }
                if(sChoose === "2"){
                    sCheckCheckDKDN = "0";
                    sCheckCheckDT = "1";
                    sCheckCheckTL = "0";
                }
                if(sChoose === "3"){
                    sCheckCheckDKDN = "0";
                    sCheckCheckDT = "0";
                    sCheckCheckTL = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterbusiness',
                        id: id,
                        sDate: global_fm_report_date,
                        INFO_NAME: $("#INFO_NAME").val(),
                        INFO_ORGANI: $("#INFO_ORGANI").val(),
                        INFO_ID: $("#INFO_ID").val(),
                        INFO_DATE_GRANT: $("#INFO_DATE_GRANT").val(),
                        INFO_ORGANI_GRANT: $("#INFO_ORGANI_GRANT").val(),
                        INFO_TAXCODE: $("#INFO_TAXCODE").val(),
                        INFO_ADDRESS: $("#INFO_ADDRESS").val(),
                        INFO_EMAIL: $("#INFO_EMAIL").val(),
                        INFO_PHONE: $("#INFO_PHONE").val(),
                        INFO_REPRESEN: $("#INFO_REPRESEN").val(),
                        INFO_ROLE: $("#INFO_ROLE").val(),
                        INFO_CMND: $("#INFO_CMND").val(),
                        CONTACT_FULLNAME: $("#CONTACT_FULLNAME").val(),
                        CONTACT_ROLE: $("#CONTACT_ROLE").val(),
                        CONTACT_EMAIL: $("#CONTACT_EMAIL").val(),
                        CONTACT_PHONE: $("#CONTACT_PHONE").val(),
                        CheckDKDN: sCheckCheckDKDN,
                        CheckDT: sCheckCheckDT,
                        CheckTL: sCheckCheckTL,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
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
                        else if (myStrings[0] === JS_EX_NO_DATA)
                        {
                            funErrorAlert(global_no_print_data);
                        }
                        else {
                            funErrorAlert(global_errorsql);
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function closeForm()
            {
                if(localStorage.getItem("sessCertToPrint") === "1")
                {
                    localStorage.setItem("sessCertToPrint", null);
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
                else
                {
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
        if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
            String strCOMPANY = "";
            String strORGANI = "";
            String strMSTORMNS = "";
            String strADDRESS = "";
            String strEMAIL = "";
            String strPHONE = "";
            String strEMAIL_CONTACT = "";
            String strPHONE_CONTACT = "";
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_GET_INFO(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].COMPANY_NAME));
                        PREFIX_UUID[][] sessPrefix;
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDEnterprise");
                        strMSTORMNS = rs[0][0].ENTERPRISE_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].ENTERPRISE_ID, sessPrefix);
//                        strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].TAX_CODE));
//                        if("".equals(strMSTORMNS)) {
//                            strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].BUDGET_CODE));
//                        }
//                        if("".equals(strMSTORMNS)) {
//                            strMSTORMNS = EscapeUtils.CheckTextNull((rs[0][0].DECISION));
//                        }
                        strEMAIL_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].EMAIL_CONTRACT));
                        strPHONE_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].PHONE_CONTRACT));
                        String sSubjectDN = EscapeUtils.CheckTextNull((rs[0][0].SUBJECT));
                        strEMAIL = CommonFunction.getEmailInDN(sSubjectDN);
                        strADDRESS = CommonFunction.getLocationInDN(sSubjectDN) + ", " + CommonFunction.getStateOrProvinceInDN(sSubjectDN);
                        strPHONE = CommonFunction.getPhoneInDN(sSubjectDN);
                        strORGANI = CommonFunction.getORGANIZATIONInDN(sSubjectDN.replace("\"", "&quot;"));
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                            if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                isAccessAgencyPage = false;
//                            }
                        }
                        if (isAccessAgencyPage == true) {
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitlePrintEdits"></span></h2>
            <script>$("#idLblTitlePrintEdits").text(certlist_title_print_register);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnPrintCert" class="btn btn-info" type="button" onclick="popupPrint('<%=ids%>', '<%= anticsrf%>');" />
                    <!--<input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />-->
                </li>
                <script>
//                    document.getElementById("btnClose").value = global_fm_button_back;
                    document.getElementById("btnPrintCert").value = global_fm_button_print;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupBusiness"></legend>
                    <script>$("#idLblTitleGroupBusiness").text(certlist_group_add_bussiness_info);</script>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCompany" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_NAME" id="INFO_NAME" readonly value="<%= EscapeUtils.escapeHtmlDN(strCOMPANY)%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleCompany").text(global_fm_company);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoGrant" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_ORGANI" id="INFO_ORGANI" readonly value="<%= strORGANI %>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoGrant").text(global_fm_O_notrefix);</script>
                    </div>
                    <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;">
                            <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheckDKDN" checked><span id="idLblTitleRadioDKDN"></span></label>
                        </div>
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;">
                            <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheckGPDT"><span id="idLblTitleRadioGPDT"></span></label>
                        </div>
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;">
                            <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheckQDTL"><span id="idLblTitleRadioQDTL"></span></label>
                        </div>
                        <input type="text" style="display: none;" id="idSessIsDate" value="1" name="idSessIsDate" />
                        <script>
                            $("#idLblTitleRadioDKDN").text(global_fm_checkbox_gcndk);
                            $("#idLblTitleRadioGPDT").text(global_fm_checkbox_GPDT);
                            $("#idLblTitleRadioQDTL").text(global_fm_checkbox_QDTL);
                        </script>
                    </div>
                    <script>
                        $('.radio-inline').on('click', function () {
                            var s = $(this).find('input').attr('id');
                            if (s === 'nameCheckDKDN')
                            {
                                $("#idSessIsDate").val('1');
                            }
                            if (s === 'nameCheckGPDT')
                            {
                                $("#idSessIsDate").val('2');
                            }
                            if (s === 'nameCheckQDTL')
                            {
                                $("#idSessIsDate").val('3');
                            }
                        });
                    </script>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoID" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_ID" id="INFO_ID" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoID").text(global_fm_ID);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoOrganiDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_DATE_GRANT" id="INFO_DATE_GRANT" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoOrganiDate").text(global_fm_date_grant);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoOrganiGrant" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_ORGANI_GRANT" id="INFO_ORGANI_GRANT" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoOrganiGrant").text(global_fm_organi_grant);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoTaxCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_TAXCODE" value="<%= strMSTORMNS %>" readonly id="INFO_TAXCODE" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoTaxCode").text(global_fm_enterprise_id);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoAddress" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_ADDRESS" value="<%= strADDRESS %>" readonly id="INFO_ADDRESS" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoAddress").text(global_fm_address);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_EMAIL" value="<%= strEMAIL %>" id="INFO_EMAIL" readonly class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoEmail").text(global_fm_email);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleInfoPhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_PHONE" value="<%= strPHONE %>" readonly id="INFO_PHONE" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleInfoPhone").text(global_fm_phone + "/" + global_fm_fax);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleInfoRole"></label>
                                <label class="CssRequireField" id="idLblNoteInfoRole"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_ROLE" id="INFO_ROLE" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleInfoRole").text(global_fm_role);
                            $("#idLblNoteInfoRole").text(global_fm_require_label);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleInfoRepresen"></label>
                                <label class="CssRequireField" id="idLblNoteInfoRepresen"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_REPRESEN" id="INFO_REPRESEN" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleInfoRepresen").text(global_fm_representative_legal);
                            $("#idLblNoteInfoRepresen").text(global_fm_require_label);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleInfoCMND"></label>
                                <label class="CssRequireField" id="idLblNoteInfoCMND"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="INFO_CMND" id="INFO_CMND" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleInfoCMND").text(global_fm_CMND + "/ " + global_fm_HC + "/ " + global_fm_CitizenId);
                            $("#idLblNoteInfoCMND").text(global_fm_require_label);
                        </script>
                    </div>
                </fieldset>

                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleContactBusi"></legend>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleFullname"></label>
                                <label class="CssRequireField" id="idLblNoteFullname"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONTACT_FULLNAME" id="CONTACT_FULLNAME" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleContactBusi").text(certlist_group_add_bussiness_contact);
                            $("#idLblTitleFullname").text(global_fm_fullname);
                            $("#idLblNoteFullname").text(global_fm_require_label);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleContatcRole"></label>
                                <label class="CssRequireField" id="idLblNoteContatcRole"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONTACT_ROLE" id="CONTACT_ROLE" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleContatcRole").text(global_fm_role);
                            $("#idLblNoteContatcRole").text(global_fm_require_label);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONTACT_EMAIL" readonly value="<%= strEMAIL_CONTACT %>" id="CONTACT_EMAIL" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmail").text(global_fm_email);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitlePhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONTACT_PHONE" readonly value="<%= strPHONE_CONTACT %>" 
                                    id="CONTACT_PHONE" class="form-control123" onblur="autoTrimTextField('CONTACT_PHONE', this.value);"/>
                            </div>
                        </div>
                        <script>$("#idLblTitlePhone").text(global_fm_phone);</script>
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
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
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
