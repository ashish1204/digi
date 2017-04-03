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

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>

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
		/* window.open('http://10.102.19.142', '_blank'); */
	}

	/* function sendMail() {
		window.open('mailto:gaurav.dua@capgemini.com');
	} */
</script>





<!-- <script language="javascript" type="text/javascript">
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
</script> -->

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

	</div>

	<ul class="nav nav-tabs marginLeft15px">
		<li class="dropdown"><a class="dropdown-toggle buttonlink"
			data-toggle="dropdown"><b><button class="btn mobileLabMenu">Market
						Trends</button></b></a>
			<ul class="dropdown-menu">
				<li class="active"><a
					href="../../mobileLab/deviceSelectionMatrix/DSGlobalPage">Global</a></li>
				<li><a href="../../mobileLab/deviceSelectionMatrix/DSHomePage">US</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/DSHomeUKPage">UK</a></li>

			</ul></li>
		<li><a href="DSselectDevices.jsp" class="buttonlink"><b><button
						class="btn mobileLabMenu">Device Recommendation</button></b></a></li>
		<li><a class="buttonlink"
			href="../../mobileLab/deviceSelectionMatrix/DScloudDevices"><b><button
						class="btn mobileLabMenu">Device Selection</button></b></a></li>
		<li class="active"><a class="buttonlink"
			href="../../mobileLab/deviceSelectionMatrix/perfectoCloud"><b><button
						class="btn mobileLabMenu">Perfecto Cloud</button></b></a></li>
	</ul>



	<!-- 	<div class="col-sm-8 col-md-10 verticalLine"> -->
	<div class="marginTop50px"></div>
	<div class="font24px txtCenter">
		<label>Available Cloud Devices1</label>
	</div>
	<div class="marginTop15px"></div>

	<div class="row">
		<div class="col-sm-4"></div>
		<div class="col-sm-4">
			<input id="search" class="form-control" type="search"
				placeholder="Search" /> <span class="glyphicon glyphicon-search"></span>
		</div>
		<div class="col-sm-4"></div>
	</div>


	<div class="marginTop15px"></div>
	<div class="container">
		<div class="row col-sm-12">
			<div class="col-sm-1"></div>
			<div class="col-sm-10">
				<c:if test="${not empty message }">
					<h3 class="successMsg">${message }</h3>
				</c:if>
				<%-- <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Number of devices :</b>
								${fn:length(deviceDetails)} <br> <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Available Devices :</b>
								${Available } <br> --%>
				<%-- Total Offline
											Devices : ${offline } <br>Total In Use Devices : ${InUse }
											<br> </br>--%>
				<c:if test="${ not empty perfectoDevice }">

					<table id="table" align="center"
						class="table table-hover table-striped table-bordered table-responsive"
						cellpadding="0" cellspacing="0" data-filter="true"
						data-input="#filterTable-input">
						<thead>
							<tr class="tableHead">

								<!-- <th>S.No</th> -->
								<th>Select</th>
								<th>Device Name</th>
								<th>Device_OS</th>
								<th>OS_Version</th>
								<th>Status</th>
								<th>Tools</th>

							</tr>
						</thead>
						<tbody>

							<c:forEach items="${perfectoDevice}" var="perfectoDevice">
								<tr>
									<td align="center"><input type="checkbox"
										value="${perfectoDevice.deviceId}"
										id="${perfectoDevice.deviceId}" onchange="getButton(this.id);" /></td>
									<td align="left"><c:out
											value="${perfectoDevice.deviceName}" /></td>
									<td align="left"><c:out value="${perfectoDevice.os}" /></td>
									<td align="left"><c:out value="${perfectoDevice.version}" /></td>
									<td align="left"><c:out value="${perfectoDevice.status}" /></td>

									<c:if test="${perfectoDevice.reservation == 'false'}">
										<td align="center"><a
											href="reservePerfectoDevice?deviceId=${perfectoDevice.deviceId}"><button
													class="btn btn-primary" style="background-color: #50CF4D"
													id="but${perfectoDevice.deviceId}" disabled="disabled">
													<b>Reserve</b>
												</button></a></td>
									</c:if>
									<%-- <c:if test="${perfectoDevice.reservation == 'true'}">    
                                               	<td align="center"><a href="releasePerfectoDevice?deviceId=${perfectoDevice.deviceId}"><button class="btn btn-primary" style ="background-color: #FDB45C"; id="but${perfectoDevice.deviceId}" disabled="disabled"><b>Release</b></button></a></td>
                                               </c:if>	 --%>

									<c:if test="${perfectoDevice.reservation == 'true'}">
										<td align="center"><a
											href="releasePerfectoDevice1?deviceId=${perfectoDevice.deviceId}&reservationId=${perfectoDevice.reservationId}"><button
													class="btn btn-primary" style="background-color: #FDB45C"
													; id="but${perfectoDevice.deviceId}" disabled="disabled">
													<b>Release</b>
												</button></a></td>
									</c:if>

								</tr>



							</c:forEach>

						</tbody>
					</table>

				</c:if>
			</div>
			<div class="col-sm-1"></div>



		</div>
	</div>

	<div align="center">


		<div data-role="main" class="ui-content"></div>
	</div>






	<!-- Footer -->
	<div class="perfectoMargin">
		<jsp:include page="../../../common/footer.jsp"></jsp:include>
	</div>
</body>
</html>


