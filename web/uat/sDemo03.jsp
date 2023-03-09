<%-- 
    Document   : sDemo03
    Created on : May 9, 2018, 11:20:50 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script>
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function onClick()
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                setTimeout(function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                }, 5000);
            }
        </script>
        <style>
            #over {
                background: #000;
                position: fixed;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                opacity: 0.8;
                z-index: 999;
            }
        </style>
    </head>
    <body>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <input type="button" id="demo1" value="Loadding" onclick="onClick();"/>
    </body>
</html>
