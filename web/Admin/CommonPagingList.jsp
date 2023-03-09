<%-- 
    Document   : CommonPagingList
    Created on : Aug 7, 2017, 11:57:00 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.utility.LoadParamSystem"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.utility.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    int iSwRws = 10;
    int iTotSrhRcrds = 5;
    Config cogCommon = new Config();
    String Count_SearchHistory = cogCommon.GetPropertybyCode(Definitions.CONFIG_COUNT_SEARCH_MAX).trim();
    String striSwRws = cogCommon.GetPropertybyCode(Definitions.CONFIG_PAGING_ISWRWS).trim();
    String striTotSrhRcrds = cogCommon.GetPropertybyCode(Definitions.CONFIG_PAGING_ITOTSRHRCRDS).trim();
    if (striSwRws != null && !"NULL".equals(striSwRws.toUpperCase()) && !"".equals(striSwRws)) {
        iSwRws = Integer.parseInt(striSwRws);
    }
    if (striTotSrhRcrds != null && !"NULL".equals(striTotSrhRcrds.toUpperCase()) && !"".equals(striTotSrhRcrds)) {
        iTotSrhRcrds = Integer.parseInt(striTotSrhRcrds);
    }
%>