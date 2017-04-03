<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="routerApp" >

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
	src="${pageContext.request.contextPath}/vendor/jquery/dist/jquery.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular/angular.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-router/release/angular-ui-router.js"></script>

	<script src="${pageContext.request.contextPath}/vendor/chart/dist/Chart.js"></script>
	
	<script
	src="${pageContext.request.contextPath}/vendor/ang-chart/dist/angular-chart.js"></script>
	
	
	<script src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/vendor/bootstrap/dist/js/bootstrap.js"></script>

<script src="${pageContext.request.contextPath}/integratedQALabs/mobileLab/deviceSelectionMatrix/App.js"></script>


<!-- <script type="text/javascript">


       // Function to start seetest URL in New Tab
      function runSeeTest() {
    	  
		window.open('http://10.102.22.86', '_blank');
	}

</script>

 -->
</head>

<body>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">	<img class="capgeminiLogo"
				src="${pageContext.request.contextPath}/styles/images/capgemini.png"></a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="#">Page 1</a></li>
      <li><a href="#">Page 2</a></li>
      <li><a href="#">Page 3</a></li>
    </ul>
  </div>
</nav>
  
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
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown" style="background-color: #f3f3f3;"><button
					class="btn mobileLabMenu">
					<b>Market Trends <span class="caret"></span>
				</button>
				</b> </a>
			<ul class="dropdown-menu">
				<li class="active"><a
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

		<li class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown"><b>Device Recommendation<span
					class="caret"></span></b></a>
			<ul class="dropdown-menu">
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceGlobal">Global</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUS">US</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUK">UK</a></li>

			</ul></li>
			
	
			

		<li class="active dropdown" ><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown"><b>Browser Trends<span
					class="caret"></span></b></a>
			<ul class="dropdown-menu" >
				<!-- <li data-ng-controller="GlobalController"><a
					ui-sref="global">Global</a></li> -->
				<!-- <li><a
					ui-sref="us">US</a></li>
				<li ><a
					ui-sref="uk">UK</a></li> -->
				<li data-ng-controller="GlobalController"><a href="javascript:void(0)"
				
					data-ng-click="getGlobalData()">Global</a></li>
				<li data-ng-controller="USController"><a href="javascript:void(0)" data-ng-click="getUSData()"
					>US</a></li>
				<li data-ng-controller="UKController"><a
					href="javascript:void(0)" data-ng-click="getUKData()">UK</a></li>

			</ul></li>
			
			
	
			
			
		<!-- <li>
				<a  class="buttonlink" href="../../mobileLab/deviceSelectionMatrix/DScloudDevices"><b><button class="btn mobileLabMenu" >Device Selection</button></b></a>
			</li>
			<li>
				<a  class="buttonlink" href="../../mobileLab/deviceSelectionMatrix/perfectoCloud"><b><button class="btn mobileLabMenu" >Perfecto Cloud</button></b></a>
			</li> -->

		<li class="pull-right">
			<div class="upload">Update Global Database:</div>

			<form name="userForm" method="post" class="pull-left"
				action="../../mobileLab/deviceSelectionMatrix/uploadFileExcelDataGlobal.obj"
				enctype="multipart/form-data" onSubmit="return GoEmpty(this);">
				<input type="file" class="btn btn-primary" id="recentData"
					name="recentData"> <input type="submit" value="UPDATE"
					class="btn btn-primary updateButt">
			</form>
		</li>
	</ul>
<%-- 	<canvas id="bar" class="chart chart-bar"
  chart-data="data12" chart-labels="labels"> chart-series="series"
</canvas>
	 --%>
	
	
	<div class=container>
	<div ui-view></div> 
	</div>
	



</body>
</html>