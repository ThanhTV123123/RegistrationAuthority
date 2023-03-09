<%-- 
    Document   : DeleteToken
    Created on : Jul 6, 2020, 2:19:07 PM
    Author     : USER
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
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
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = token_title_delete;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../TokenCommon",
                    data: {
                        idParam: 'deletetoken',
                        ID: $("#strID").val(),
                        TOKEN_ID: $("#TOKEN_SN").val(),
                        AGENT_ID_LOG: $("#strstrAGENT_ID_LOGID").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            localStorage.setItem("EDIT_TOKEN", $("#strID").val());
                            funSuccAlert(token_succ_edit, "TokenList.jsp");
                        }
                        else if (arr[0] === JS_EX_CSRF) {
                            funCsrfAlert();
                        }
                        else if (arr[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (arr[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                            RedirectPageLoginNoSess(global_error_wrong_agency);
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshTokenSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "TokenList.jsp";
                        }
                        else
                        {
                            window.location = "TokenList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style type="text/css">
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Token");
            try {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            String SessAgentName = (String) session.getAttribute("SessAgentName");
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        TOKEN[][] rs = new TOKEN[1][];
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
//                ids = seEncript.decrypt(ids);
                if (EscapeUtils.IsInteger(ids) == true) {
                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                    {
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    db.S_BO_TOKEN_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                    if (rs[0].length > 0) {
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                            if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                isAccessAgencyPage = false;
//                            }
                        }
                        if (isAccessAgencyPage == true) {
                            String strNAME_LINK = EscapeUtils.CheckTextNull(rs[0][0].MENU_LINK_NAME);
                            String strLINK_VALUE = EscapeUtils.CheckTextNull(rs[0][0].MENU_LINK_URL);
                            String strLINK_NOTICE = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_URL);
                            String strNOTICE_INFO = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_CONTENT);
                            String strCOLOR_TEXT = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_TEXT_COLOR);
                            String strCOLOR_BKGR = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_BGR_COLOR);
                            if("".equals(strLINK_NOTICE) && "".equals(strNOTICE_INFO) &&
                                "".equals(strCOLOR_TEXT) && "".equals(strCOLOR_BKGR))
                            {
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                ObjectMapper oMapperParse = new ObjectMapper();
                                if (sessGeneralPolicy[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                    {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON))
                                        {
                                            String sValueDefaultJSON = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            if(!"".equals(sValueDefaultJSON))
                                            {
                                                PUSH_TOKEN_EDITED itemParsePush = oMapperParse.readValue(sValueDefaultJSON, PUSH_TOKEN_EDITED.class);
                                                for (PUSH_TOKEN_EDITED.Attribute attribute : itemParsePush.getAttributes()) {
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT))
                                                    {
                                                        strNOTICE_INFO = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL))
                                                    {
                                                        strLINK_NOTICE = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR))
                                                    {
                                                        strCOLOR_BKGR = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR))
                                                    {
                                                        strCOLOR_TEXT = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            int sTOKEN_STATE_ID = rs[0][0].TOKEN_STATE_ID;
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(token_title_delete);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DELETE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                    %>
                    <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
                    <script>document.getElementById("btnSave").value = global_fm_button_delete;</script>
                    <%
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <script>document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" name="strID" id="strID" hidden="true" readonly value="<%= rs[0][0].ID%>">
                <input type="hidden" name="strAGENT_ID_LOG" id="strstrAGENT_ID_LOGID" hidden="true" readonly value="<%= SessUserAgentID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <%
                    if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN))
                        && CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN)) == true){
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" name="TOKEN_SN" id="TOKEN_SN" readonly class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN) %>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenSN").text(token_fm_tokenid);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenVersion" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123" disabled>
                                <%
                                    TOKEN_VERSION[][] rstTOKEN_VERSION = new TOKEN_VERSION[1][];
                                    db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstTOKEN_VERSION);
                                    if (rstTOKEN_VERSION[0].length > 0) {
                                        for (TOKEN_VERSION temp1 : rstTOKEN_VERSION[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID==rs[0][0].TOKEN_VERSION_ID ? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenVersion").text(token_fm_version);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="TOKEN_STATE" id="TOKEN_STATE" class="form-control123" disabled>
                                <%
                                    TOKEN_STATE[][] rstTOKEN_STATE = new TOKEN_STATE[1][];
                                    db.S_BO_TOKEN_STATE_COMBOBOX(sessLanguageGlobal, rstTOKEN_STATE);
                                    if (rstTOKEN_STATE[0].length > 0) {
                                        for (TOKEN_STATE temp1 : rstTOKEN_STATE[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID== sTOKEN_STATE_ID? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenStatus").text(global_fm_Status);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <%
                                if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                            %>
                            <select name="strAGENT_ID" id="strAGENT_ID" class="form-control123" disabled>
                                <%
                                    BRANCH[][] rst = new BRANCH[1][];
                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                    if (rst[0].length > 0) {
                                        for (BRANCH temp1 : rst[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == rs[0][0].BRANCH_ID ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <input TYPE="text" id="strAGENT_IDEdit" style="display: none;" value="<%= String.valueOf(rs[0][0].BRANCH_ID) %>"/>
                            <%
                            } else {
                            %>
                            <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= SessAgentName%>" class="form-control123"/>
                            <input type="text" style="display: none;" name="strAGENT_ID" id="strAGENT_ID" value="<%= SessUserAgentID%>" class="form-control123"/>
                            <input TYPE="text" id="strAGENT_IDEdit" style="display: none;" value="<%= SessUserAgentID%>"/>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <script>$("#idLblTitleAgentName").text(global_fm_Branch);</script>
                </div>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupNotifi"></legend>
                    <script>$("#idLblTitleGroupNotifi").text(token_group_notification);</script>
                    <div class="form-group">
                         <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorText"></label>
                            <input type="text" disabled id="strCOLOR_TEXT" name="strCOLOR_TEXT" disabled class="form-control123 jscolor" value="<%= strCOLOR_TEXT%>"/>
                            <input type="text" id="strCOLOR_TEXTEdit" style="display: none;" class="color" value="<%= strCOLOR_TEXT%>"/>
                        </div>
                        <script>$("#idLblTitleColorText").text(token_fm_colortext);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorBkgr"></label>
                            <input type="text" disabled id="strCOLOR_BKGR" name="strCOLOR_BKGR" disabled class="form-control123 jscolor" value="<%= strCOLOR_BKGR%>"/>
                            <input type="text" id="strCOLOR_BKGREdit" style="display: none;" class="color" value="<%= strCOLOR_BKGR%>"/>
                        </div>
                        <script>$("#idLblTitleColorBkgr").text(token_fm_colorgkgd);</script>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkNote"></label>
                            <textarea type="Text" id="strLINK_NOTICE" readOnly name="strLINK_NOTICE" style="height: 50px;" class="form-control123"><%= strLINK_NOTICE%></textarea>
                            <input TYPE="text" id="strLINK_NOTICEEdit" style="display: none;" value="<%= strLINK_NOTICE%>"/>
                        </div>
                        <script>$("#idLblTitleLinkNote").text(token_fm_noticelink);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleInfoNote"></label>
                            <textarea type="Text" id="strNOTICE_INFO" readOnly name="strNOTICE_INFO" style="height: 50px;" class="form-control123"><%= strNOTICE_INFO%></textarea>
                            <input TYPE="text" id="strNOTICE_INFOEdit" style="display: none;" value="<%= strNOTICE_INFO%>"/>
                        </div>
                        <script>$("#idLblTitleInfoNote").text(token_fm_noticeinfor);</script>
                    </div>
                </fieldset>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupDynamic"></legend>
                    <script>$("#idLblTitleGroupDynamic").text(token_group_dynamic);</script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkName"></label>
                            <textarea type="Text" id="strNAME_LINK" readOnly name="strNAME_LINK" class="form-control123"><%= strNAME_LINK%></textarea>
                            <input TYPE="text" id="sstrNAME_LINKEdit" style="display: none;" value="<%=strNAME_LINK%>"/>
                        </div>
                        <script>$("#idLblTitleLinkName").text(token_fm_linkname);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkValue"></label>
                            <textarea type="Text" id="strLINK_VALUE" readOnly name="strLINK_VALUE" class="form-control123"><%= strLINK_VALUE%></textarea>
                            <input TYPE="text" id="sstrLINK_VALUEEdit" style="display: none;" value="<%=strLINK_VALUE%>"/>
                        </div>
                        <script>$("#idLblTitleLinkValue").text(token_fm_linkvalue);</script>
                    </div>
                </fieldset>
                <%
                    boolean IS_CHANGE_SOPIN = false;
                    String dtIS_CHANGE_SOPIN = "";
                    String userIS_CHANGE_SOPIN = "";
                    String desIS_CHANGE_SOPIN = "";
                    String attrIS_CHANGE_SOPIN = "";
                    String stateIS_CHANGE_SOPIN = "";
                    int itateIS_CHANGE_SOPIN = 0;
                    String sRole = session.getAttribute("RoleID_ID").toString().trim();
                    TOKEN[][] rsFunction = new TOKEN[1][];
                    db.S_BO_TOKEN_GET_ATTR(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rsFunction);
                    if (rsFunction[0].length > 0) {
                        for(int j = 0; j< rsFunction[0].length; j++)
                        {
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN)
                            {
                                IS_CHANGE_SOPIN = true;
                                dtIS_CHANGE_SOPIN = rsFunction[0][j].CREATED_DT;
                                userIS_CHANGE_SOPIN = rsFunction[0][j].CREATED_BY;
                                desIS_CHANGE_SOPIN = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_CHANGE_SOPIN = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_CHANGE_SOPIN = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_CHANGE_SOPIN = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                        }
                    }
                    int j=0;
                %>
                <%
                    if(IS_CHANGE_SOPIN == true)
                    {
                %>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleRequestEdit"></legend>
                    <script>$("#idLblTitleRequestEdit").text(token_group_request_edit);</script>
                    <style type="text/css">
                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                        .btn{margin-bottom: 0px;}
                    </style>
                    <div class="table-responsive">
                    <table class="table table-striped projects" id="mtToken">
                        <thead>
                        <th id="idLblTitleTableSST"></th>
                        <th id="idLblTitleTableRequestType"></th>
                        <th id="idLblTitleTableStatus"></th>
                        <th id="idLblTitleTableCreateDate"></th>
                        <th id="idLblTitleTableCreateUser"></th>
                        <%
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <th id="idLblTitleTableAction"></th>
                        <%
                            }
                        %>
                        </thead>
                        <tbody>
                            <%
                                if(IS_CHANGE_SOPIN == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_CHANGE_SOPIN%></td>
                                <td><%= stateIS_CHANGE_SOPIN%>
                                </td>
                                <td><%= dtIS_CHANGE_SOPIN%></td>
                                <td><%= userIS_CHANGE_SOPIN%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_CHANGE_SOPIN == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline" style="cursor: pointer;" onclick="popupCancel(<%=attrIS_CHANGE_SOPIN%>);" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_CHANGE_SOPIN == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline" style="cursor: pointer;" onclick="popupCancel(<%=attrIS_CHANGE_SOPIN%>);" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                        <script>
                            function popupCancel(idID)
                            {
                                swal({
                                    title: "",
                                    text: token_confirm_cancel_request,
                                    imageUrl: "../Images/icon_warning.png",
                                    imageSize: "45x45",
                                    showCancelButton: true,
                                    closeOnConfirm: true,
                                    allowOutsideClick: false,
                                    confirmButtonText: login_fm_buton_OK,
                                    cancelButtonText: global_button_grid_cancel,
                                    cancelButtonColor: "#199DC0"
                                },
                                function () {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    setTimeout(function () {
                                        $.ajax({
                                            type: "post",
                                            url: "../TokenCommon",
                                            data: {
                                                idParam: 'cancelrequest',
                                                TokenID: $("#strID").val(),
                                                id: idID
                                            },
                                            cache: false,
                                            success: function (html) {
                                                var myStrings = sSpace(html);
                                                var arr = myStrings.split('#');
                                                if (arr[0] === "0")
                                                {
                                                    funSuccLocalAlert(token_succ_cancel_request);
                                                }
                                                else if (arr[0] === JS_EX_CSRF)
                                                {
                                                    funCsrfAlert();
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
                                                    funErrorAlert(global_errorsql);
                                                }
                                                $(".loading-gif").hide();
                                                $('#over').remove();
                                            }
                                        });
                                        return false;
                                    }, JS_STR_ACTION_TIMEOUT);
                                });
                            }
                        </script>
                        </tbody>
                    </table>
                    </div>
                    <script>
                        $("#idLblTitleTableSST").text(global_fm_STT);
                        $("#idLblTitleTableRequestType").text(global_fm_requesttype);
                        $("#idLblTitleTableStatus").text(global_fm_Status);
                        $("#idLblTitleTableCreateDate").text(global_fm_date_create);
                        $("#idLblTitleTableCreateUser").text(global_fm_user_create);
                        $("#idLblTitleTableAction").text(global_fm_action);
                    </script>
                </fieldset>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY)%>" />
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateUser").text(global_fm_user_create);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT)%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateDate").text(global_fm_date_create);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                    </script>
                </div>
            </form>
        </div>
        <%
                    }
                }
            }
        }
        %>
            <script>
                $(document).ready(function () {
                    document.getElementById("strCOLOR_TEXT").disabled = true;
                    document.getElementById("strCOLOR_BKGR").disabled = true;
                });
            </script>
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
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>
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
