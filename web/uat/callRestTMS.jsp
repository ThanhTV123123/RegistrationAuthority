<%-- 
    Document   : sDemo01
    Created on : Apr 9, 2018, 2:17:38 PM
    Author     : THANH-PC
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <script>
            function restAccessToken() {
                var formData = '{"userName": "DL_01","passWord": "12345678"}';
                $.ajax({
                    url: 'https://137.59.41.6:9086/RegistrationAuthority/tmsra/restapi/getTokenForTMSRA',
                    type: 'POST',
                    data : formData,
                    dataType: 'json',
                    contentType: "application/json",
                    async : false,
                    success: function(response, textStatus, jqXHR) {
                        console.log(response);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                        console.log(textStatus);
                        console.log(errorThrown);
                    }
                });
            }
        </script>
        <style>
            .projects th{font-weight: bold;}.navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 10px 10px;border-bottom:0px;
            }
            .level0{
                background: red;
            }
            .collapse_abc .toggle_abc {
                background: url("../Images/collapse.gif");
            }
            .expand_abc .toggle_abc {
                background: url("../Images/expand.gif");
            }
            .toggle_abc {
                height: 9px;
                width: 9px;
                display: inline-block;   
            }.x_panel {
                padding:15px 17px 0 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body class="nav-md">
        <div class="container body">
            <input type="button" value="Call Rest" onclick="restAccessToken();"/>
            <!--<script src="../style/jquery.min.js"></script>-->
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
        </div>
    </body>
</html>
