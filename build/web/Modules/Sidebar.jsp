<%-- 
    Document   : Sidebar
    Created on : Dec 31, 2015, 3:24:55 PM
    Author     : DELL
--%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="collapse navbar-collapse navbar-ex1-collapse">
    <ul class="nav navbar-nav side-nav" style="background: #179CBF;">
        <li>
            <a href="javascript:;" data-toggle="collapse" data-target="#demo4" 
               class="collapsed" aria-expanded="false">
                <i class="fa fa-fw fa-arrows-v"></i>Danh mục quản trị<i class="fa fa-fw fa-caret-down"></i>
            </a>
            <ul id="demo4" class="collapse in" aria-expanded="true">
                <li data-toggle="collapse1" data-target="#demo11">
                    <a href="UserList.jsp" style="color: #ffffff;">Quản lý nhân viên</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo13">
                    <a href="BranchList.jsp" style="color: #ffffff;">Quản lý chi nhánh</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo13">
                    <a href="IssueList.jsp" style="color: #ffffff;">Quản lý nhà phát hành</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo14">
                    <a href="CompanyList.jsp" style="color: #ffffff;">Quản lý doanh nghiệp</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo14">
                    <a href="EmailList.jsp" style="color: #ffffff;">Quản lý email kích hoạt</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo14">
                    <a href="EmailConfig.jsp" style="color: #ffffff;">Cấu hình email server</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="javascript:;" data-toggle="collapse" data-target="#demo5" 
               class="collapsed" aria-expanded="true">
                <i class="fa fa-fw fa-arrows-v"></i>Hợp đồng - Hóa đơn<i class="fa fa-fw fa-caret-down"></i>
            </a>
            <ul id="demo5" class="collapse in" aria-expanded="true">
                <li data-toggle="collapse1" data-target="#demo51">
                    <a href="AgreementList.jsp" style="color: #ffffff;">Danh sách hợp đồng</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo52">
                    <a href="AgreementNew.jsp" style="color: #ffffff;">Thêm mới hợp đồng</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo52">
                    <a href="AccountingList.jsp" style="color: #ffffff;">Hạch toán hóa đơn</a>
                </li>
                <li data-toggle="collapse1" data-target="#demo52">
                    <a href="BillPaymentList.jsp" style="color: #ffffff;">Thanh toán hóa đơn</a>
                </li>
            </ul>
        </li>
    </ul>
</div>