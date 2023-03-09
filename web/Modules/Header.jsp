<%--
    Document   : Header
    Created on : Dec 31, 2015, 3:22:26 PM
    Author     : DELL
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .cssLogoMenuLeft{
        max-height: 70px;
        min-height: 50px;
        max-width: 200px;
        min-width: 100px;
    }
    .carousel-inner>.item>a>img,.carousel-inner>.item>img,.thumbnail a>img,.thumbnail>img{max-width: 232px;}
</style>
<%
    String sLogo = EscapeUtils.CheckTextNull(session.getAttribute("sessLogoBranch").toString());
    if(!"".equals(sLogo)){
%>
<div class="navbar nav_title" style="border: 0;">
    <a href="../Admin/Home.jsp"><img class="img-responsive cssLogoMenuLeft11" id="idLogoPageHeader" src="data:image/jpg;base64, <%= sLogo %>" /></a>
</div>
<script>
    var image = document.getElementById('idLogoPageHeader');
    image.title = global_title_logo;
    image.attributes = global_title_logo;
//    image.src = "../" + LinkLogoPage;
</script>
<%
    } else {
%>
<div class="navbar nav_title" style="border: 0;">
    <a href="../Admin/Home.jsp"><img class="img-responsive cssLogoMenuLeft11" id="idLogoPageHeader" /></a>
</div>
<script>
    var image = document.getElementById('idLogoPageHeader');
    image.title = global_title_logo;
    image.attributes = global_title_logo;
    image.src = "../" + LinkLogoMenuPage;
</script>
<%
    }
%>