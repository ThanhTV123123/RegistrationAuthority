<%-- 
    Document   : RoleForAdd
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
    if (session.getAttribute("sessRoleSetListPermision") != null) {
        sRoleSetPermision = session.getAttribute("sessRoleSetListPermision").toString().trim();
        session.setAttribute("sessRoleSetListPermision", null);
    } else {
        sRoleSetPermision = EscapeUtils.CheckTextNull(request.getParameter("idPerForRole"));
    }
    if (!"".equals(sRoleSetPermision)) {
%>
<fieldset class="scheduler-border" id="idRoleForAdd">
    <legend class="scheduler-border"><%= "1".equals(session.getAttribute("sessVN").toString().trim()) ? Definitions.CONFIG_ROLESET_FM_FUNCTION : Definitions.CONFIG_ROLESET_FM_FUNCTION_LAOS %></legend>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_LOCK)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsLock" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_islock);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_UNLOCK)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsUnlock" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isunlock);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_SOPIN)) {
    %>
    <div class="col-sm-6">  
        <input TYPE="checkbox" id="IsSOPIN" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_issopin);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_PUSH)) {
    %>
    <div class="col-sm-6">  
        <input TYPE="checkbox" id="IsPush" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_ispush);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_INITIALIZE)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsInitialize" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isinit);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DYNAMIC)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsDynamic" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_isdynamic);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ACTIVE)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsActive" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_isactive);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_EDIT_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="EditCertificate" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_editcert);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_APPROVE_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="IsEnrollCers" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_approvecert);</script>    
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DELETE_REQUEST)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="DeleteRequest" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
            <script>document.write(funrole_fm_deleterequest);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ADD_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="AddRenewal" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_addrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_DELETE_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="DeleteRenewal" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
            <script>document.write(funrole_fm_deleterenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_ACCESS_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="AccessRenewal" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox" style="padding-right: 15%;">
        <script>document.write(funrole_fm_accessrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_IMPORT_RENEWAL)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="ImportRenewal" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
        <script>document.write(funrole_fm_importrenewal);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_REVOKE_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="RevokeCert" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
        <script>document.write(funrole_fm_revoke_cert);</script>
        </label>
    </div>
    <%}%>
    <%
        if (comRole.CheckRoleSetFunctions(sRoleSetPermision, Definitions.CONFIG_FUNROLE_EXPORT_CERT)) {
    %>
    <div class="col-sm-6">
        <input TYPE="checkbox" id="ExportCert" checked/>&nbsp;&nbsp;
        <label class="control-labelcheckbox">
        <script>document.write(funrole_fm_export_cert);</script>
        </label>
    </div>
    <%}%>
</fieldset>
<%
    }
%>