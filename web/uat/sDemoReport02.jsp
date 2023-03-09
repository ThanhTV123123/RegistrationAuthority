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
            table, tr, td, th
            {
                border: 1px solid black;
                border-collapse:collapse;
            }
            img.button_open{
                content:url('http://code.stephenmorley.org/javascript/collapsible-lists/button-open.png');
                cursor:pointer;
            }
            img.button_closed{
                content: url('http://code.stephenmorley.org/javascript/collapsible-lists/button-closed.png');
                cursor:pointer;
            }
        </style>
        <script>
            function CreateGroup(group_name)
            {
                // Create Button(Image)
                $('td.' + group_name).prepend("<img class='" + group_name + " button_closed'> ");
                // Add Padding to Data
                $('tr.' + group_name).each(function () {
                    var first_td = $(this).children('td').first();
                    var padding_left = parseInt($(first_td).css('padding-left'));
                    $(first_td).css('padding-left', String(padding_left + 25) + 'px');
                });
                RestoreGroup(group_name);

                // Tie toggle function to the button
                $('img.' + group_name).click(function () {
                    ToggleGroup(group_name);
                });
            }

            function ToggleGroup(group_name)
            {
                ToggleButton($('img.' + group_name));
                RestoreGroup(group_name);
            }

            function RestoreGroup(group_name)
            {
                if ($('img.' + group_name).hasClass('button_open'))
                {
                    // Open everything
                    $('tr.' + group_name).show();

                    // Close subgroups that been closed
                    $('tr.' + group_name).find('img.button_closed').each(function () {
                        sub_group_name = $(this).attr('class').split(/\s+/)[0];
                        RestoreGroup(sub_group_name);
                    });
                }

                if ($('img.' + group_name).hasClass('button_closed'))
                {
                    $('tr.' + group_name).hide();
                }
            }

            function ToggleButton(button)
            {
                $(button).toggleClass('button_open');
                $(button).toggleClass('button_closed');
            }

            $(document).ready(function () {
                CreateGroup('group1');
                CreateGroup('group2');
                CreateGroup('sub_group1');
                CreateGroup('sub_group2');
                CreateGroup('sub_sub_group1');
            });
        </script>
    </head>
    <body>
        <script>
            function ShowHide(body_id) {
                var TBody;
                TBody = document.getElementById(body_id);
                if (!TBody)
                    return true;
                if (TBody.style.display === "none") {
                    TBody.style.display = "block";
                }
                else {
                    TBody.style.display = "none";
                }
                return true;
            }
        </script>
        <table>
            <tbody>
                <tr>
                    <th onclick="ShowHide('1')">Area 1</th>
                </tr>
            </tbody>
            <tbody id="1">
                <tr>
                    <td>Test Note</td>
                </tr>
                <tr>
                    <td>Test Note</td>
                </tr>
            </tbody>
            <tbody>
                <tr>
                    <th onclick="ShowHide('2')">Area 2</th>
                </tr>
            </tbody>
            <tbody id="2" style="display: block;">
                <tr>
                    <td>Test Note</td>
                </tr>
                <tr>
                    <td>Test Note</td>
                </tr>
            </tbody>
        </table>
        <div>
            <a href="http://automation-beyond.com/chapters/tutorials/test-reports/">Home</a></div>
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