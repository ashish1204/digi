
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
<!-- <link rel="icon" href="favicon.ico" type="image/x-icon" /> -->
<%-- <link rel="stylesheet" type="text/css" id="theme" href="${pageContext.request.contextPath}/styles/css/theme-default.css"/>  --%>
<!-- <link href="https://fortawesome.github.io/Font-Awesome/assets/font-awesome/css/font-awesome.css" rel="stylesheet">  -->


<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>



<style>
table.scroll {
	width: 100%; /* Optional */
	/* border-collapse: collapse; */
	border-spacing: 0;
	/* border: 2px solid black; */
}

table.scroll tbody, table.scroll thead {
	display: block;
}

thead tr th {
	height: 30px;
	line-height: 30px;
	/*text-align: left;*/
}

table.scroll tbody {
	height: 500px;
	overflow-y: auto;
	overflow-x: hidden;
}

tbody td {
	width: 130px;
	/*   border-right: 1px solid black;*/
}

thead th {
	width: 130px;
}

thead th:last-child {
	width: 130px;
}

tbody td:last-child, thead th:last-child {
	border-right: none;
}

#select1, #Status {
	width: 75px;
}
</style>
<script type="text/javascript">
var $table = $('table.scroll'),
$bodyCells = $table.find('tbody tr:first').children(),
colWidth;

//Adjust the width of thead cells when window resizes
$(window).resize(function() {
// Get the tbody columns width array
colWidth = $bodyCells.map(function() {
	
    return $(this).width();
}).get();
//alert("width: "+colWidth[0]);
// Set the width of thead columns
$table.find('thead tr').children().each(function(i, v) {
	
    $(v).width(colWidth[i]);
}); 
$table.find('tbody tr').children().each(function(i, v) {
	
    $(v).width(colWidth[i]);
});
}).resize(); // Trigger resize handler
</script>



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
function getconfirm(id){
	if(document.getElementById(id).checked){
	$.ajax({
		data:{
			id:id
		},
        url: "../../mobileLab/deviceSelectionMatrix/confirmmail" ,
        succes: function () { }
    });
	}
	else{
	}
}
</script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>

<script>
	
	
	
function changeVersion(option)
{
	$('#table tbody tr').each(function() {
	    var data = $(this).find("td").eq(3).html();  
	   // alert(data);
	    
	    
	    
	    
	});
	
	}

	

 function changeStatus(option)
{
	
	//alert("os in changestatus : "+document.getElementById("select1").value);
	//changeSelectOption(document.getElementById("select1").value,1);
	 if(option == 'Available'){
			
			$('#table tbody tr').each(function() {
			    var data = $(this).find("td").eq(2).html();  
			    var data1=$(this).find("td").eq(4).html();
			    
			    //alert("cell data : "+data);
			    var osOpt = document.getElementById("select1").value;
			    
			    
			    if (osOpt == "Device_OS"){
			    if(data1 == 'Available'){
			    	document.getElementById("txt").innerHTML= "containing only available";
			    	$(this).show();
			    }
			    else{
			    	document.getElementById("txt").innerHTML= "no result";
			    	$(this).hide();
			    }
			   }
			   else if (osOpt == "iOS"){
				   if(data == 'iOS' && data1 == "Available" ){
					   document.getElementById("txt").innerHTML= "ios n available";
				    	$(this).show();
				    }
				   else{
					   document.getElementById("txt").innerHTML= "no ios n available";
				    	$(this).hide();
				    }
			   }
			   else if (osOpt == "Android"){
				   if(data == 'Android' && data1 == "Available" ){
				    	//document.getElementById("txt").innerHTML= "Android n available";
				    	$(this).show();
				    }
				   else{
					   document.getElementById("txt").innerHTML= "no Android n available";
				    	$(this).hide();
				    }
			   }
			   else{
				   
				   document.getElementById("txt").innerHTML= "user selected"+osOpt+" - no result for available";
			   }
			});
			
	
	}
	 else if(option == 'In Use'){
			
			$('#table tbody tr').each(function() {
			    var data = $(this).find("td").eq(2).html();  
			    var data1=$(this).find("td").eq(4).html();
			    
			    //alert("cell data : "+data);
			    var osOpt = document.getElementById("select1").value;
			    
			    
			    if (osOpt == "Device_OS"){
			    if(data1 == 'In Use'){
			    	//document.getElementById("txt").innerHTML= "only in use";
			    	$(this).show();
			    }
			    else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
			   }
			   else if (osOpt == "iOS"){
				   if(data == 'iOS' && data1 == "In Use" ){
					  // document.getElementById("txt").innerHTML= "ios n inuse";
				    	$(this).show();
				    }
				   else{
				    	//alert("containing not ios");
				    	$(this).hide();
				    }
			   }
			   else if (osOpt == "Android"){
				   if(data == 'Android' && data1 == "In Use" ){
					   //document.getElementById("txt").innerHTML= "Android n in use";
				    	$(this).show();
				    }
				   else{
				    	//alert("containing not ios");
				    	$(this).hide();
				    }
			   }
			   else{
				  // document.getElementById("txt").innerHTML= "user selected"+osOpt+" - no result for in use";
			   }
			});
			
	
	}
	
	 else if(option == 'Create'){
			
			$('#table tbody tr').each(function() {
			    var data = $(this).find("td").eq(2).html();  
			    var data1=$(this).find("td").eq(4).html();
			    //alert(data1);
			    //alert("cell data : "+data);
			    var osOpt = document.getElementById("select1").value;
			    
			    
			    if (osOpt == "Device_OS"){
			    if(data1 == 'Create'){
			    	//document.getElementById("txt").innerHTML= "only in use";
			    	$(this).show();
			    }
			    else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
			   }
			   else if (osOpt == "iOS"){
				   if(data == 'iOS' && data1 == "Create" ){
					  // document.getElementById("txt").innerHTML= "ios n inuse";
				    	$(this).show();
				    }
				   else{
				    	//alert("containing not ios");
				    	$(this).hide();
				    }
			   }
			   else if (osOpt == "Android"){
				   if(data == 'Android' && data1 == "Create" ){
					   //document.getElementById("txt").innerHTML= "Android n in use";
				    	$(this).show();
				    }
				   else{
				    	//alert("containing not ios");
				    	$(this).hide();
				    }
			   }
			   else{
				  // document.getElementById("txt").innerHTML= "user selected"+osOpt+" - no result for in use";
			   }
			});
			
	
	}

}
</script>

<script>

function changeSelectOption(option,id){
	/* if(id == '1')
		id="selectTool1"; */
	
	if(option == 'iOS'){
		
		$('#table tbody tr').each(function() {
		    var data = $(this).find("td").eq(2).html();  
		    var data1=$(this).find("td").eq(4).html();
		    
		    //alert("cell data : "+data);
		    var statusOpt = document.getElementById("Status").value;
		    //alert("+++++++++++++"+statusOpt);
		    
		    if (statusOpt == "Status"){
		    if(data == 'iOS'){
		    	//alert("containing only ios");
		    	$(this).show();
		    }
		    else{
		    	//alert("containing not ios");
		    	$(this).hide();
		    }
		   }
		   else if (statusOpt == "Available"){
			   
			   if(data == 'iOS' && data1 == "Available" ){
			    	//alert("containing ios n available");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		   else if (statusOpt == "In Use"){
			   if(data == 'iOS' && data1 == "In Use" ){
			    	//alert("containing ios n inuse");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		   else if (statusOpt == "Create"){
			   if(data == 'iOS' && data1 == "Create" ){
			    	//alert("containing ios n inuse");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		});
		
		
		
		/* var opt= document.getElementById(id);
		for(var i = opt.options.length - 1 ; i >= 0 ; i--)
	    {
	        opt.remove(i);
	    }  */
		//var selectId=document.getElementById(id);
		var newOption = document.createElement("option");
		newOption.text = 'Device_Version';
		newOption.value = 'Device_Version';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '10-9'
		newOption.value = '10-9';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '9-8';
		newOption.value = '9-8';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '8-7';
		newOption.value = '8-7';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '7-6';
		newOption.value = '7-6';
		selectId.appendChild(newOption);
		
		
		
	}

	else if(option == 'Android'){
		
		/* var opt= document.getElementById(id);
		alert(opt);
		for(var i = opt.options.length - 1 ; i >= 0 ; i--)
	    {
	        opt.remove(i);
	    } */
		
		$('#table tbody tr').each(function() {
		    var data = $(this).find("td").eq(2).html();  
		    var data1=$(this).find("td").eq(4).html(); 
		    var statusOpt = document.getElementById("Status").value;
		    //alert("+++++++++++++"+statusOpt);
		    
		    if (statusOpt == "Status"){
		    if(data == 'Android'){
		    	//alert("containing only android");
		    	$(this).show();
		    }
		    else{
		    	//alert("containing not ios");
		    	$(this).hide();
		    }
		   }
		   else if (statusOpt == "Available"){
			   if(data == 'Android' && data1 == "Available"){
			    	//alert("containing android n available");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		   else if (statusOpt == "In Use"){
			   if(data == 'Android' && data1 == "In Use"){
			    	//alert("containing android n inuse");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		   else if (statusOpt == "Create"){
			   if(data == 'Android' && data1 == "Create"){
			    	//alert("containing android n inuse");
			    	$(this).show();
			    }
			   else{
			    	//alert("containing not ios");
			    	$(this).hide();
			    }
		   }
		});
		
		
		var selectId=document.getElementById(id);
		var newOption = document.createElement("option");
		newOption.text = 'Device_Version';
		newOption.value = 'Device_Version';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '6';
		newOption.value = '6';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '5';
		newOption.value = '5';
		selectId.appendChild(newOption);
		var newOption = document.createElement("option");
		newOption.text = '4';
		newOption.value = '4';
		selectId.appendChild(newOption);
	}
	
	else if(option='Device_OS')
		{
		$('#table tbody tr').each(function() {
		    var data = $(this).find("td").eq(2).html();  
		    //alert("cell data : "+data);
		    if(data == 'android'){
		    	//alert("containing android");
		    	$(this).hide();
		    }
		    else{
		    	//alert("containing not android");
		    	$(this).show();
		    }
		});
		
		var opt= document.getElementById(id);
		for(var i = opt.options.length - 1 ; i >= 0 ; i--)
	    {
	        opt.remove(i);
	    }
		var selectId=document.getElementById(id);
		var newOption = document.createElement("option");
		newOption.text = 'Device_Version';
		newOption.value = 'Device_Version';
		selectId.appendChild(newOption);
		}
	
	
	
}


</script>


<script src="${pageContext.request.contextPath}/js/sorttable.js"></script>
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
		window.open('http://10.102.22.86', '_blank');
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
		<div class="marginTop65px"></div>

		<div class="row borderBottom">
			<div class="col-md-4 col-lg-4 marginTop15px">
				<label class="font24px">Mobile Lab</label>
			</div>
			<div class="col-md-3 col-lg-4"></div>
			<div class="col-md-5 col-lg-4 marginTop15px">

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




		<ul class="nav nav-tabs marginLeft15px marginTop15px">
			<li class="dropdown"><a class="dropdown-toggle buttonlink"
				data-toggle="dropdown"><b>Market Trends <span class="caret"></span></b>
			</a>
				<ul class="dropdown-menu">
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/DSGlobalPage">Global</a></li>
					<li><a href="../../mobileLab/deviceSelectionMatrix/DSHomePage">US</a></li>
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/DSHomeUKPage">UK</a></li>

				</ul></li>
			<li><a class="dropdown-toggle buttonlink"
				href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
				data-toggle="dropdown"><b>User Trends <span class="caret"></span></b>
			</a>
				<ul class="dropdown-menu">
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/DSUserTrendsPage">Application1</a></li>


				</ul></li>
			<li class="active dropdown"><a
				class="dropdown-toggle buttonlink" data-toggle="dropdown"><b><button
							class="btn mobileLabMenu">
							Device Recommendation<span class="caret"></span>
						</button></b></a>
				<ul class="dropdown-menu">
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceGlobal">Global</a></li>
					<li class="active"><a
						href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUS">US</a></li>
					<li><a
						href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUK">UK</a></li>

				</ul></li>
				
				
				<li class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown"><b>Browser Trends<span
					class="caret"></span></b></a>
			<ul class="dropdown-menu">
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/BrowserGlobalPage">Global</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/BrowserUSPage">US</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/BrowserUKPage">UK</a></li>

			</ul></li>
			<!-- <li><a class="buttonlink"
					href="../../mobileLab/deviceSelectionMatrix/DScloudDevices"><b><button class="btn mobileLabMenu">Device Selection</button></b></a></li> -->
		</ul>


		<div class="marginTop50px"></div>
		<div class="top-bar">
			<a href=""></a>
			<div class="font24px txtCenter">
				<label>Device Recommendation</label>
			</div>

		</div>
		<br>
		<div class="row col-md-12">
			<div class="col-sm-0"></div>
			<div class="col-sm-12">
				<c:if test="${not empty message}">
					<div class="alert alert-success alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<strong>Success!</strong> ${message}
						<script>speak('${message }')</script>
					</div>
				</c:if>



				<p id="txt"></p>
				<c:if test="${ not empty devList1 }">
					<!-- <div id="table-wrapper">
					    <div id="table-scroll">  -->
					<table id="table" align="left"
						class="table datatable table-hover table-striped table-bordered table-responsive"
						cellpadding="0" cellspacing="0">
						<thead>
							<tr class="tableHead">


								<th>Device_Name</th>
								<th>Device_OS</th>
								<th>Device_Version</th>
								<th>Device_ID</th>
								<th>Device_Location</th>
								<th>Category</th>
								<th>Resolution</th>
								<th>Status</th>
								<th>Source</th>
								<th>Reservation_Time</th>
								<th>Tool</th>
								<th>Get Notification</th>
							</tr>
							<!--  <tr><th colspan="8" class="tableHead txtCenter">Tier1</th></tr> -->
						</thead>

						<tbody>

							<c:forEach items="${devList1}" var="deviceList1">

								<c:if test="${deviceList1.reservation == 'false'}">


									<tr>

										<%--    <td align="center"><input type="checkbox"  value="${deviceList1.device_id}" id="${deviceList1.device_id}" onchange="getButton(this.id);"/></td>
                                                                                  <input type="hidden" name="deviceid" value="${deviceList1.device_id}">
                                                                                  <input type="hidden" name="deviceName" value="${deviceList1.name}">
                                                                                  <input type="hidden" name="vendor" value="${deviceList1.vendor}"> --%>

										<td align="left"><label> <c:out
													value="${deviceList1.name}" /></label></td>
										<td align="left"><c:out value="${deviceList1.os}" /></td>
										<td align="left"><c:out value="${deviceList1.version}" /></td>
										<td align="left" style="word-wrap: break-word;"><label>
												<c:out value="${deviceList1.device_id}" />
										</label></td>
										<td align="left"><c:out
												value="${deviceList1.agentlocation}" /></td>
										<td align="left"><c:out
												value="${deviceList1.devicecategory}" /></td>
										<td align="left"><c:out value="${deviceList1.resolution}" /></td>
										<td align="left"><c:out value="${deviceList1.status}" /></td>

										<c:if test="${deviceList1.vendor == 'seetest'}">
											<td align="left">Private</td>
										</c:if>
										<c:if test="${deviceList1.vendor == 'perfecto_partner'}">
											<td align="left">Hosted</td>
										</c:if>
										<c:if test="${deviceList1.vendor == 'Emulator'}">
											<td align="left">Emulator</td>
										</c:if>
										<form name="time" action="reserveDeviceUS" method="post">
											<td><input type="number" min="1" max="3" value="1"
												name="time" /></td>

											<c:if test="${deviceList1.status == 'Available'}">
												<td align="center"><input type="submit"
													class="btn btn-primary" style="background-color: #50CF4D"
													value="Reserve"> <input type="hidden"
													name="deviceid" value="${deviceList1.device_id}"> <input
													type="hidden" name="deviceName" value="${deviceList1.name}">
													<input type="hidden" name="vendor"
													value="${deviceList1.vendor}"></td>
											</c:if>
										</form>
										<c:if test="${deviceList1.vendor == 'Emulator'}">
											<td align="center"><a href="" class="btn btn-info">Emulator</a></td>
										</c:if>

										<td align="center">-</td>
										<input type="hidden" name="deviceid"
											value="${deviceList1.device_id}">
										<input type="hidden" name="deviceName"
											value="${deviceList1.name}">
										<input type="hidden" name="vendor"
											value="${deviceList1.vendor}">




									</tr>



								</c:if>



								<c:if test="${deviceList1.reservation == 'true'}">

									<tr>
										<%--   <td align="center"><input type="checkbox"  value="${deviceList1.device_id}" id="${deviceList1.device_id}" onchange="getButton(this.id);"/></td>
                                                                                         <input type="hidden" name="deviceid" value="${deviceList1.device_id}">
                                                                                         <input type="hidden" name="deviceName" value="${deviceList1.name}">
                                                                                        <input type="hidden" name="vendor" value="${deviceList1.vendor}"> --%>

										<td align="left"><label><c:out
													value="${deviceList1.name}" /></label></td>
										<td align="left"><c:out value="${deviceList1.os}" /></td>
										<td align="left"><c:out value="${deviceList1.version}" /></td>
										<td align="left"><label> <c:out
													value="${deviceList1.device_id}" /></label></td>
										<td align="left"><c:out
												value="${deviceList1.agentlocation}" /></td>
										<td align="left"><c:out
												value="${deviceList1.devicecategory}" /></td>
										<td align="left"><c:out value="${deviceList1.resolution}" /></td>
										<td align="left"><c:out value="${deviceList1.status}" /></td>


										<c:if test="${deviceList1.vendor == 'seetest'}">
											<td align="left">Private</td>
										</c:if>
										<c:if test="${deviceList1.vendor == 'perfecto_partner'}">
											<td align="left">Hosted</td>
										</c:if>
										<c:if test="${deviceList1.vendor == 'Emulator'}">
											<td align="left">Emulator</td>
										</c:if>

										<form name="time" action="releaseDeviceUS" method="post">
											<td align="left">End
												Time:${deviceList1.reservation_time}</td>



											<c:if test="${deviceList1.reservation == 'true'}">
												<td align="center"><input type="submit"
													class="btn btn-primary" style="background-color: #FDB45C"
													value="Release"> <input type="hidden"
													name="deviceid" value="${deviceList1.device_id}"> <input
													type="hidden" name="deviceName" value="${deviceList1.name}">
													<input type="hidden" name="vendor"
													value="${deviceList1.vendor}"></td>
											</c:if>
										</form>
										<td align="center"><input type="checkbox"
											value="${deviceList1.device_id}"
											id="${deviceList1.device_id}" onchange="getButton(this.id);" /></td>
										<input type="hidden" name="deviceid"
											value="${deviceList1.device_id}">
										<input type="hidden" name="deviceName"
											value="${deviceList1.name}">
										<input type="hidden" name="vendor"
											value="${deviceList1.vendor}">
									</tr>


								</c:if>


							</c:forEach>

						</tbody>

					</table>

					<!-- </div>	 
									</div>  -->

				</c:if>






			</div>
			<div class="col-sm-0"></div>
		</div>


	</div>

	<!-- <div class="marginTop50px"></div> -->
	<!-- Footer -->

	<jsp:include page="../../../common/footer.jsp"></jsp:include>
	<!--     <div ng-include="'common/footer.html'" a></div> -->




	<!-- START SCRIPTS -->
	<!-- START PLUGINS -->
	<%--  <script type="text/javascript" src="${pageContext.request.contextPath}/styles/js/plugins/jquery/jquery.min.js"></script>  --%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/jquery/jquery-ui.min.js"></script> --%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/bootstrap/bootstrap.min.js"></script>    --%>
	<!-- END PLUGINS -->
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.slimscroll.min.js"></script>
       <script type="text/javascript" src="${pageContext.request.contextPath}//styles/js/jquery.slimscroll.js"></script> --%>
	<!-- THIS PAGE PLUGINS -->
	<script type='text/javascript'
		src='${pageContext.request.contextPath}/js/plugins/icheck/icheck.min.js'></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js"></script>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/datatables/jquery.dataTables.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/tableExport.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/jquery.base64.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/html2canvas.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/jspdf/libs/sprintf.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/jspdf/jspdf.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/tableexport/jspdf/libs/base64.js"></script>
	<!-- END PAGE PLUGINS -->

	<!-- START TEMPLATE -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/settings.js"></script>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/actions.js"></script>
	<!-- END TEMPLATE -->
	<!-- END SCRIPTS -->












</body>
</html>





