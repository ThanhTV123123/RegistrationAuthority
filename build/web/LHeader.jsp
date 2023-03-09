<%-- 
    Document   : LHeader
    Created on : Oct 17, 2016, 10:39:18 AM
    Author     : THANH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="header-two-123">
    <div class="container">
        <div class="col-md-5">
            <div class="col-sm-5" style="padding: 0px;">
                <img id="idLogoPageHeader" class="img-responsive" src="Images/TrustedHub.png" />
            </div>
        </div>
        <div class="col-md-7" style="text-align: right;">
            <div class="form-group" style="padding: 0px;">
                <h4 style="color: #1F9EBF; font-weight: bold; font-size: 16px;"> HOTLINE: <script>document.write(header_hotline);</script> </h4>
                <p style="color: #000000;">
                    <img onclick="loginEN('1');" style="width: 18px;height: 18px;cursor: pointer;" title="Vietnamese" src="Images/vn_flag.png" /> | 
                    <img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('0');" src="Images/en_flag.png" />
                </p>
            </div>
        </div>
        <div style="clear: both;"></div>
    </div>
</div>
<script>
    function loginEN(id)
    {
        if (id === "1")
        {
            localStorage.setItem("VN", "0");
        }
        else
        {
            localStorage.setItem("VN", "1");
        }
        location.reload();
    }
</script>
