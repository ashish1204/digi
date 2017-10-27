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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/css/digi.css">

<script language="Javascript">
	function GoEmpty() {

		var a = document.forms["userForm"]["recentData"].value;

		if (a == "") {
			alert("Please select file...");
			return false;
		}
		return true;
	}
</script>
<script type="text/javascript">

function getButton(id){
	var type="but"+id;
	
	if(document.getElementById(id).checked){
		
	document.getElementById(type).disabled = false;
	}
	else{
	
		document.getElementById(type).disabled = true;
	}
	

}

$(document).ready(function(){

$('#search').keyup(function () {
	
    var searchitem = $('#search').val();
    if (searchitem == '' || searchitem == null || searchitem == undefined)           {
        $('#table tbody tr').show();
    }
    else {
    	var count= 0;
       searchitem = searchitem.toUpperCase();
        $('#table tbody tr').hide();
        $('#table tbody tr').each(function () {
        	
            if ($(this).text().toUpperCase().indexOf(searchitem) > -1) {
            	count++;
                $(this).show();
            }
        });
    }
   $('#totaldevice').html(count);
});
});
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
		window.open('http://10.102.19.142', '_blank');
	}

	/* function sendMail() {
		window.open('mailto:gaurav.dua@capgemini.com');
	} */
</script>


<script language="javascript" type="text/javascript">
	function dropdownlist(listindex) {

		document.formname.subcategory.options.length = 0;
		switch (listindex) {

		case "iOS":
			document.formname.subcategory.options[0] = new Option(
					"Select Version", "");
			document.formname.subcategory.options[1] = new Option("iOS 7",
					"iOS 7");
			document.formname.subcategory.options[2] = new Option("iOS 8",
					"iOS 8");
			document.formname.subcategory.options[3] = new Option("iOS 9",
					"iOS 9");

			break;

		case "Android":
			document.formname.subcategory.options[0] = new Option(
					"Select Version", "");
			document.formname.subcategory.options[1] = new Option("2.2", "2.2");
			document.formname.subcategory.options[2] = new Option("2.3", "2.3");
			document.formname.subcategory.options[3] = new Option("3.x", "3.x");
			document.formname.subcategory.options[4] = new Option("4.1.x",
					"4.1.x");
			document.formname.subcategory.options[5] = new Option("4.2.x",
					"4.2.x");
			document.formname.subcategory.options[6] = new Option("4.3", "4.3");
			document.formname.subcategory.options[7] = new Option("4.4", "4.4");
			document.formname.subcategory.options[8] = new Option("5.0", "5.0");
			document.formname.subcategory.options[9] = new Option("5.1", "5.1");
			document.formname.subcategory.options[10] = new Option("6.0", "6.0");

			break;

		case "BlackBerry OS":
			document.formname.subcategory.options[0] = new Option(
					"Select Version", "");
			document.formname.subcategory.options[1] = new Option(
					"BlackBerry 7", "BlackBerry 7");
			document.formname.subcategory.options[2] = new Option(
					"BlackBerry 10", "BlackBerry 10");
			document.formname.subcategory.options[3] = new Option("Tablet OS",
					"Tablet OS");

			break;

		case "Windows Phone OS":
			document.formname.subcategory.options[0] = new Option(
					"Select Version", "");
			document.formname.subcategory.options[1] = new Option(
					"Microsoft Windows 8", "Microsoft Windows 8");
			document.formname.subcategory.options[2] = new Option(
					"Microsoft Windows 10", "Microsoft Windows 10");
			document.formname.subcategory.options[3] = new Option(
					"Microsoft Windows RT", "Microsoft Windows RT");
			document.formname.subcategory.options[4] = new Option(
					"Internet Tablet OS 2007", "Internet Tablet OS 2007");
			document.formname.subcategory.options[5] = new Option(
					"Internet Tablet OS 2008", "Internet Tablet OS 2008");

			break;

		}
		return true;
	}
</script>
<script language="Javascript">
	function FieldsEmpty() {

		var a = document.forms["formname"]["category"].selectedIndex;
		var b = document.forms["formname"]["subcategory"].selectedIndex;
		var c = document.forms["formname"]["optionnm"].selectedIndex;
		var d = document.forms["formname"]["n"].value;
		if (a == 0 || b == 0 || c == 0 || d == "") {
			alert("All Fields are Mandatory");
			return false;
		} else if (isNaN(d)) {
			alert("Please enter numeric value as no. of Devices....");
			return false;
		}
		document.forms["formname"]["n"].value = d;
		return true;
	}
</script>

<title>Dynamic Drop Down List</title>

</head>
<body>


	<div>
		<!-- Header and Navigation Bar -->

		<jsp:include page="../../../common/logo.jsp"></jsp:include>
		<div class="marginTop90px"></div>

		<!--   </ul> -->
	</div>

	<div class="container">
		<div class="row borderBottom">
			<div class="col-md-4 col-lg-4">
				<label class="font24px">Mobile Lab</label>
			</div>
			<div class="col-md-3 col-lg-4"></div>
			<div class="col-md-5 col-lg-4">
				<a href="../../mobileLab/mobileLab.jsp"><button
						class="btn btn-primary sideButton">Help</button></a>
				<button type="button" class="btn btn-primary pull-right"
					onclick="runSeeTest()" title="Start Testing">
					Start Testing <span class="glyphicon glyphicon-plus"></span>
				</button>
				<a href="../../mobileLab/downloadExcelUtility"><button
						type="button" class="btn btn-primary sideButton">
						Download TestPlan <img
							src="${pageContext.request.contextPath}/styles/images/download.png"
							height="20" width="20" title="Download TestPlan">
					</button> </a>
			</div>

		</div>



		<ul class="nav nav-tabs marginLeft15px">
			<li class="dropdown"><a class="dropdown-toggle buttonlink"
				data-toggle="dropdown"><b><button class="btn mobileLabMenu">
							Market Trends <span class="caret"></span>
						</button></b></a>
				<ul class="dropdown-menu">
					<li class="active"><a
						href="../../mobileLab/deviceSelectionMatrix/DSGlobalPage">Global</a></li>
					<li><a href="../../mobileLab/deviceSelectionMatrix/DSHomePage">US</a></li>
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/DSHomeUKPage">UK</a></li>

				</ul></li>
			<li class="active"><a href="RecommendDevice" class="buttonlink"><b><button
							class="btn mobileLabMenu">Device Recommendation</button></b></a></li>
			<li><a class="buttonlink"
				href="../../mobileLab/deviceSelectionMatrix/DScloudDevices"><b><button
							class="btn mobileLabMenu">Device Selection</button></b></a></li>
		</ul>



		<div class="marginTop50px"></div>
		<div class="top-bar">
			<a href=""></a>
			<div class="font24px txtCenter">
				<label>Select Device Recommendation Options</label>
			</div>

		</div>
		<br>

		<div class="row col-sm-12">
			<div class="col-sm-2"></div>
			<div class="col-sm-8">
				<form id="formname" class="form-horizontal" name="formname"
					method="post"
					action="../../mobileLab/deviceSelectionMatrix/DSfinalSelection"
					onSubmit="return FieldsEmpty(this);">
					<div class="form-group">
						<label for="run_name" class="col-sm-2 control-label">Select
							OS :</label>
						<div class="col-sm-8">
							<select class="form-control" name="category" id="category"
								onchange="javascript: dropdownlist(this.options[this.selectedIndex].value);">
								<option value="">Select OS</option>
								<option value="iOS">iOS</option>
								<option value="Android">Android</option>
								<option value="Windows Phone OS">Windows Phone OS</option>
								<option value="BlackBerry OS">BlackBerry OS</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="project_name" class="col-sm-2 control-label">Select
							Version : </label>
						<div class="col-sm-8">
							<script type="text/javascript" language="JavaScript">
								document.write('<select class="form-control" name="subcategory"><option value="">Select Version</option></select>')
						</script>
							<noscript>
								<select name="subcategory" id="subcategory">
									<option value="">Select Version</option>
								</select>
							</noscript>
						</div>
					</div>
					<div class="form-group">
						<label for="build_id" class="col-sm-2 control-label">Select
							Option : </label>
						<div class="col-sm-8">
							<select class="form-control" name="optionnm" id="optionnm">
								<option value="">Select Option</option>
								<option value="Only Selected Version">Only Selected
									Version</option>
								<option value="Selected and Higher Versions">Selected
									and Higher Versions</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="run_name" class="col-sm-2 control-label">No of
							Devices :</label>
						<div class="col-sm-8">
							<input class="form-control" type="text" name="n">
						</div>
					</div>
					<div class="marginTop15px"></div>
					<div class="form-group">

						<div class="col-sm-2 col-sm-offset-5">
							<a href="#"> <input type="submit" name="ds" value="Devices"
								class="btn btn-primary"></a>
						</div>
					</div>
				</form>
			</div>
			<div class="col-sm-2"></div>
		</div>



		<div class="row col-sm-12">
			<div class="marginTop15px"></div>
			<div class="col-sm-2"></div>
			<div class="col-sm-8">
				<label>Selected OS : </label>${category}<br> <label>Selected
					Version : </label>${subcategory}<br> <label>Selected Option :
				</label>${optionnm}<br> <label>No. of Devices : </label>${n}
			</div>
			<div class="col-sm-2"></div>
		</div>



		<div class="row col-sm-12">
			<div class="col-sm-2"></div>
			<div class="col-sm-8">
				<div class="marginTop15px"></div>
				<div class="font24px txtCenter">Recommended Devices</div>
				<div class="marginTop15px"></div>
				<table class="table table-striped table-bordered" cellpadding="0"
					cellspacing="0">

					<tr class="first tableHead">
						<th class="first">Model_name</th>
						<th>Version_Name</th>
						<th class="last">Type</th>
					</tr>
					<tr>
						<td colspan=4 align="center">Smartphones</td>
					</tr>
					<c:if test="${not empty tempASP1}">
						<c:forEach items="${tempASP1}" var="key">
							<c:forEach items="${key}" var="listASP">
								<tr>
									<td>${listASP.dev_name}</td>
									<td>${listASP.ver_name}</td>
									<td>${listASP.tp}</td>
								</tr>
							</c:forEach>
						</c:forEach>
					</c:if>
					<tr>
						<td colspan=4 align="center">Tablets</td>
					</tr>
					<c:if test="${not empty tempATB1}">
						<c:forEach items="${tempATB1}" var="key">
							<c:forEach items="${key}" var="listATB">
								<tr>
									<td>${listATB.dev_name}</td>
									<td>${listATB.ver_name}</td>
									<td>${listATB.tp}</td>
								</tr>
							</c:forEach>
						</c:forEach>
					</c:if>
					<tr>
						<td colspan=4 align="center">Suggestions</td>
					</tr>
					<c:if test="${not empty tempSUG1}">
						<c:forEach items="${tempSUG1}" var="key">
							<c:forEach items="${key}" var="listSUG">
								<tr>
									<td>${listSUG.dev_name}</td>
									<td>${listSUG.ver_name}</td>
									<td>${listSUG.tp}</td>
								</tr>
							</c:forEach>
						</c:forEach>
					</c:if>
				</table>

				<c:if test="${empty tempASP1}">
					<c:if test="${empty tempATB1}">
						<center>
							<b><font color="red" size="2"> No Records found...</font></b>
						</center>
					</c:if>
				</c:if>
			</div>
			<div class="col-sm-2"></div>
		</div>

	</div>




	<div class="marginTop15px"></div>
	<!-- Footer -->
	<jsp:include page="../../../common/footer.jsp"></jsp:include>
	<!--     <div ng-include="'common/footer.html'" a></div> -->

</body>
</html>


