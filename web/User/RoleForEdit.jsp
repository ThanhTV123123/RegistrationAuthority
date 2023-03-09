<%-- 
    Document   : RoleForEdit
    Created on : Dec 11, 2017, 5:44:12 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.utility.EscapeUtils"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    CommonFunction comRole = new CommonFunction();
    String sRoleSetPermision = "";
    String sRoleSetOfUser = "";
    int sIsCheckRule = 1;
    if (session.getAttribute("sessRoleSetListPermisionEdit") != null) {
        sRoleSetPermision = session.getAttribute("sessRoleSetListPermisionEdit").toString().trim().split("###")[0];
        sRoleSetOfUser = session.getAttribute("sessRoleSetListPermisionEdit").toString().trim().split("###")[1];
        session.setAttribute("sessRoleSetListPermisionEdit", null);
        sIsCheckRule = 0;
    } else {
        String sParamForRole = EscapeUtils.CheckTextNull(request.getParameter("idPerForRoleEdit"));
        sRoleSetPermision = sParamForRole.split("###")[0];
        sRoleSetOfUser = sParamForRole.split("###")[1];
    }
    if (!"".equals(sRoleSetPermision) && !"".equals(sRoleSetOfUser)) {
%>
<fieldset class="scheduler-border">
    <legend class="scheduler-border"><%= "1".equals(session.getAttribute("sessVN").toString().trim()) ? Definitions.CONFIG_ROLESET_FM_FUNCTION : Definitions.CONFIG_ROLESET_FM_FUNCTION_LAOS%></legend>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_LOCK)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsLock" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_LOCK) : "checked"%> />&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_islock);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_UNLOCK)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsUnlock" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_UNLOCK) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isunlock);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_SOPIN)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsSOPIN" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_SOPIN) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_issopin);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_PUSH)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsPush" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_PUSH) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_ispush);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_INITIALIZE)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsInitialize" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_INITIALIZE) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isinit);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DYNAMIC)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsDynamic" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_DYNAMIC) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isdynamic);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ACTIVE)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsActive" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_ACTIVE) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_isactive);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_EDIT_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="EditCertificate" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_EDIT_CERT) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_editcert);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_APPROVE_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsEnrollCers" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_APPROVE_CERT) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_approvecert);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DELETE_REQUEST)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="DeleteRequest" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_DELETE_REQUEST) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_deleterequest);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ADD_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="AddRenewal" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_ADD_RENEWAL) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_addrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DELETE_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="DeleteRenewal" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_DELETE_RENEWAL) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_deleterenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ACCESS_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="AccessRenewal" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_ACCESS_RENEWAL) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_accessrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_IMPORT_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="ImportRenewal" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_IMPORT_RENEWAL) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_importrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_REVOKE_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="RevokeCert" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_REVOKE_CERT) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_revoke_cert);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_EXPORT_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="ExportCert" <%= sIsCheckRule == 1 ? CommonFunction.CheckRoleSet(sRoleSetOfUser, Definitions.CONFIG_FUNROLE_EXPORT_CERT) : "checked"%>/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_export_cert);</script>
        </label>
    </div>
    <%}%>
</fieldset>
<%
    }
%>