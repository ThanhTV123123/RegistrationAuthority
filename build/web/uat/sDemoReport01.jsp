<%-- 
    Document   : sDemoReport01
    Created on : Aug 24, 2018, 4:03:30 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        <title>JSP Page</title>
        <style>
            table {
                width: 100%;
            }

            td {
                border: solid 1px #ccc;
                padding: 5px 10px;
            }

            td span {
                display: inline-block;
                width: 20px;
                text-align: center;
                cursor: pointer;
            }

            .parent-row {
                background-color: #999;
            }

            .child-row {
                background-color: #f1f1f1;
            }

            .child-child-row td:first-child {
                text-indent: 40px;
            }
        </style>
        <script>
            $(document).ready(function () {
                $('.parent-row').click(function () {
                    var $element = $(this).next();
                    while (!$element.hasClass('parent-row')) {
                        $element.toggle();
                        if ($element.next().length > 0) {
                            $element = $element.next();
                        }
                        else {
                            return;
                        }
                    }
                });

                $('.child-row.has-children').click(function () {
                    var $element = $(this).next();
                    while ($element.hasClass('child-child-row')) {
                        $element.toggle();
                        if ($element.next().length > 0) {
                            $element = $element.next();
                        }
                        else {
                            return;
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <table>
            <tr class="parent-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>

            <tr class="parent-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>            
        </table>
    </body>
</html>

<!--<tr class="parent-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>

            <tr class="child-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>

            <tr class="parent-row has-children">
                <td><span class="toggle">+</span><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>

            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>
            <tr class="child-row">
                <td><a href="javascript:;">Heading</a></td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
                <td>value</td>
            </tr>-->