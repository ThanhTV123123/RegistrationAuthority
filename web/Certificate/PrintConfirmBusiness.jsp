<%-- 
    Document   : PrintConfirmBusiness
    Created on : Mar 31, 2020, 6:10:46 PM
    Author     : USER
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
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
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
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
                localStorage.setItem("PrintRegisterPersonal", null);
                localStorage.setItem("PrintRegisterBusiness", null);
            });
            function printex() {
                $('#contentPrintCert1').printArea({
                    popWd: 750,
                    popHt: 900,
                    mode: "popup",
                    popClose: false
                });
            }
            function PrintConfirmPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=850,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function popupPrintConfirm(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printconfirminfo',
                        id: id,
                        sDate: $("#sPrintDayConfirm").val(),
                        PRINT_FULLNAME: $("#CONFIRM_PRINT_FULLNAME").val(),
                        PRINT_ADDRESS_BILLING: $("#PRINT_ADDRESS_CONFIRM").val(),
                        PRINT_TAXCODE: $("#CONFIRM_PRINT_TAXCODE").val(),
                        PRINT_CMND: $("#CONFIRM_PRINT_CMND").val(),
                        PRINT_REPRESENTATIVE: $("#CONFIRM_PRINT_REPRESENTATIVE").val(),
                        PRINT_ROLE: $("#CONFIRM_PRINT_ROLE").val(),
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
                            PrintConfirmPreview(myStrings[1]);
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
            function popupWordConfirm(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'wordconfirminfo',
                        id: id,
                        sDate: $("#sPrintDayConfirm").val(),
                        PRINT_FULLNAME: $("#CONFIRM_PRINT_FULLNAME").val(),
                        PRINT_ADDRESS_BILLING: $("#PRINT_ADDRESS_CONFIRM").val(),
                        PRINT_TAXCODE: $("#CONFIRM_PRINT_TAXCODE").val(),
                        PRINT_CMND: $("#CONFIRM_PRINT_CMND").val(),
                        PRINT_REPRESENTATIVE: $("#CONFIRM_PRINT_REPRESENTATIVE").val(),
                        PRINT_ROLE: $("#CONFIRM_PRINT_ROLE").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            var f = document.myname;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfileword&name=' + myStrings[2];
                            f.submit();
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
            String strEFFECTIVE_DT = "";
            String strMSTORMNS = "";
            String strCMNDORHC = "";
            String strADDRESS = "";
            String strEMAIL = "";
            String strPHONE = "";
            String strEMAIL_CONTACT = "";
            String strPHONE_CONTACT = "";
            try {
                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_GET_INFO(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NEWTEL)) {
                            if(rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF) {
                                strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].PERSONAL_NAME));
                            } else if(rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].PERSONAL_NAME));
                            } else if(rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].COMPANY_NAME));
                            }
                        } else {
                            strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].COMPANY_NAME));
                            if("".equals(strCOMPANY)) {
                                strCOMPANY = EscapeUtils.CheckTextNull((rs[0][0].PERSONAL_NAME));
                            }
                        }
                        PREFIX_UUID[][] sessPrefix;
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDEnterprise");
                        strMSTORMNS = rs[0][0].ENTERPRISE_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].ENTERPRISE_ID, sessPrefix);
                        sessPrefix = (PREFIX_UUID[][]) session.getAttribute("sessPrefixUIDPersonal");
                        strCMNDORHC = rs[0][0].PERSONAL_ID;// CommonReferServlet.filterPrefixUIDAuto(rs[0][0].PERSONAL_ID, sessPrefix);
                        strEMAIL_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].EMAIL_CONTRACT));
                        strPHONE_CONTACT = EscapeUtils.CheckTextNull((rs[0][0].PHONE_CONTRACT));
                        String sSubjectDN = EscapeUtils.CheckTextNull((rs[0][0].SUBJECT));
                        strEMAIL = CommonFunction.getEmailInDN(sSubjectDN);
                        strADDRESS = CommonFunction.getStateOrProvinceInDN(sSubjectDN);
                        if(!"".equals(CommonFunction.getLocationInDN(sSubjectDN)))
                        {
                            strADDRESS = CommonFunction.getLocationInDN(sSubjectDN) + ", " + strADDRESS;
                        }
                        strPHONE = CommonFunction.getPhoneInDN(sSubjectDN);
                        strORGANI = CommonFunction.getORGANIZATIONInDN(sSubjectDN.replace("\"", "&quot;"));
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        strEFFECTIVE_DT = EscapeUtils.CheckTextNull((rs[0][0].EFFECTIVE_DT));
                        String sDay = "";
                        String sMount = "";
                        String sYear = "";
                        if(!"".equals(strEFFECTIVE_DT)) {
                            sDay = strEFFECTIVE_DT.substring(0,2);
                            sMount = strEFFECTIVE_DT.substring(3,5);
                            sYear = strEFFECTIVE_DT.substring(6,10);
                        }
                        String sREPRESENTATIVE = "";
                        String sPOSITION = "";
                        if (isAccessAgencyPage == true) {
                            String sPrefileContact = EscapeUtils.CheckTextNull(rs[0][0].PROFILE_CONTACT_INFO);
                            if(!"".equals(sPrefileContact)){
                                ObjectMapper oMapperParse = new ObjectMapper();
                                ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrefileContact, ProfileContactInfoJson.class);
                                if(profileContact != null) {
                                    sREPRESENTATIVE = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                    sPOSITION = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                }
                            }
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitlePrintConfirmEdits"></span></h2>
            <script>$("#idLblTitlePrintConfirmEdits").text(global_fm_button_print_confirm);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnPrintConfirmCert" class="btn btn-info" type="button" onclick="popupPrintConfirm('<%=ids%>', '<%= anticsrf%>');" />
                    <input id="btnWordConfirmCert" class="btn btn-info" style="display: none;" type="button" onclick="popupWordConfirm('<%=ids%>', '<%= anticsrf%>');" />
                </li>
                <script>
                    document.getElementById("btnPrintConfirmCert").value = global_fm_button_print;
                    document.getElementById("btnWordConfirmCert").value = global_fm_button_export_word;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" id="sPrintDayConfirm" name="sPrintDayConfirm" readonly="true">
                <script>
                    $(document).ready(function () {
                        var vDay = '<%=sDay%>';
                        var vMount = '<%=sMount%>';
                        var vYear = '<%=sYear%>';
                        var sDayPrint = global_fm_report_date;
                        if(vDay !== '' && vMount !== '' && vYear !== '') {
                            sDayPrint = global_fm_report_print_date;
                            sDayPrint = sDayPrint.replace('[DD]',vDay);
                            sDayPrint = sDayPrint.replace('[MM]',vMount);
                            sDayPrint = sDayPrint.replace('[YYYY]',vYear);
                        }
                        $("#sPrintDayConfirm").val(sDayPrint);
                    });
                </script>
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleContactConfirmBusi"></legend>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintConfirmFullname"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONFIRM_PRINT_FULLNAME" id="CONFIRM_PRINT_FULLNAME" value="<%= EscapeUtils.escapeHtmlDN(strCOMPANY)%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleContactConfirmBusi").text(certlist_group_add_bussiness_info);
                            $("#idLblTitlePrintConfirmFullname").text(global_print2_fullname_business);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintConfirmMST"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="CONFIRM_PRINT_TAXCODE" id="CONFIRM_PRINT_TAXCODE" value="<%=strMSTORMNS%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePrintConfirmMST").text(global_fm_enterprise_id);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePrintCMND"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="CONFIRM_PRINT_CMND" id="CONFIRM_PRINT_CMND" value="<%=strCMNDORHC%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitlePrintCMND").text(global_fm_personal_id);
                        </script>
                    </div>
<!--                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleConfirmEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_EMAIL" id="PRINT_EMAIL" value="<=strEMAIL_CONTACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleConfirmEmail").text(global_fm_email_receive);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleConfirmPhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="PRINT_PHONE" id="PRINT_PHONE" value="<=strPHONE_CONTACT%>"
                                    class="form-control123" onblur="autoTrimTextField('PRINT_PHONE', this.value);"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleConfirmPhone").text(global_fm_phone);</script>
                    </div>-->
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleConfirmRepresentative" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONFIRM_PRINT_REPRESENTATIVE" id="CONFIRM_PRINT_REPRESENTATIVE" value="<%=sREPRESENTATIVE%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleConfirmRepresentative").text(branch_fm_representative);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;clear: both;">
                        <div class="form-group">
                            <label id="idLblTitleConfirmRole" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CONFIRM_PRINT_ROLE" id="CONFIRM_PRINT_ROLE" value="<%=sPOSITION%>" class="form-control123"/>
                            </div>
                        </div>
                        <script>$("#idLblTitleConfirmRole").text(global_fm_role);</script>
                    </div>
                    <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;clear: both;">
                        <label class="control-label123" id="idLblTitlePrintAddressConfirm"></label>
                        <input type="text" name="PRINT_ADDRESS_CONFIRM" id="PRINT_ADDRESS_CONFIRM" value="<%=strADDRESS%>" class="form-control123">
                        <script>
                            $("#idLblTitlePrintAddressConfirm").text(token_fm_address);
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
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
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
