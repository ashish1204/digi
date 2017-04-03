<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Device List</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/css/bootstrap.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/css/main.css">


<script language="Javascript">
	function GoEmpty() {

		var a = document.forms["userForm"]["recentData"].value;

		if (a == "") {
			alert("Please enter file path...");
			return false;
		}
		return true;
	}
</script>

<script
	src="${pageContext.request.contextPath}/vendor/jquery/dist/jquery.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular/angular.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-router/release/angular-ui-router.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-route/angular-route.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/vendor/bootstrap/dist/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/js/app/app.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script type="text/javascript">
	// Function to start seetest URL in New Tab
	function runSeeTest() {
		window.open('http://10.51.25.10:9191', '_blank');
	}

	/* function sendMail() {
		window.open('mailto:gaurav.dua@capgemini.com');
	} */
</script>


</head>


<div>
	<!-- Header and Navigation Bar -->

	<jsp:include page="../../../common/logo.jsp"></jsp:include>
	<div class="marginTop15px"></div>
	<div class="container">
		<div class="">
			<div class="col-sm-4 col-md-2 marginTop50px">
				<h3>Actions</h3>
				<ul class="nav">
					<li><a href="DSindex.jsp">Market Trends</a></li>
					<!-- <li><a
						href="../../mobileLab/deviceSelectionMatrix/DSBasicData"><b><font color="Black"> Market
							Devices</font></b></a></li> -->
					<li><a href="DSselectDevices.jsp">Device Recommendation</a></li>
					<%-- <li class="last"><a>Update <b>US</b> Database:
					</a>
						<form name="userForm" method="post"
							action="../../mobileLab/deviceSelectionMatrix/uploadFileExcelData.obj"
							enctype="multipart/form-data" onSubmit="return GoEmpty(this);">
							&nbsp;&nbsp;&nbsp;
							<center>
								<input type="file" class="btn btn-primary" id="recentData"
									name="recentData"
									style='float: center; width: 205px; margin-top: -10px'>
								<input type="submit" value="UPDATE" class="btn btn-primary"
									style='margin-top: 10px'>
							</center>
						</form></li> --%>
				</ul>
			</div>
			<div class="col-sm-8 col-md-10 verticalLine">
				<div class="marginTop50px">
					<div class="page-header">
						<a href=""></a>
						<h1
							style='text-align: left; font-size: 200%; font-family: Baskerville Old Face; color: black'>
							Basic Device Details</h1>
					</div>
					<table class="table table-striped table-bordered" cellpadding="0"
						cellspacing="0" align="center">

						<tr class="first">
							<th class="first">Sr.No.</th>
							<th>Model_name</th>
							<th>Version_Name</th>
							<th>Type</th>
							<th class="last">Resolution</th>
						</tr>
						<c:if test="${not empty temp}">
							<c:forEach items="${temp}" var="list">
								<tr>
									<td>${list.sr_no}</td>
									<td>${list.dev_name}</td>
									<td>${list.ver_name}</td>
									<td>${list.tp}</td>
									<td>${list.resol}</td>
								</tr>
							</c:forEach>
						</c:if>

					</table>
					<c:if test="${empty temp}">
						<center>
							<b><font color="red" size="2"> No Records found...</font></b>
						</center>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<!-- Footer -->
	<jsp:include page="../../../common/footer.jsp"></jsp:include>
	<!--     <div ng-include="'common/footer.html'" a></div> -->
</div>
</body>
</html>