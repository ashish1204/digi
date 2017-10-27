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
<script src="${pageContext.request.contextPath}/js/main.js">

</script>

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

<script
	src="${pageContext.request.contextPath}/vendor/chart/dist/Chart.js"></script>

<script
	src="${pageContext.request.contextPath}/vendor/ang-chart/dist/angular-chart.js"></script>


<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap.js"></script>
<script
	src="${pageContext.request.contextPath}/vendor/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/vendor/bootstrap/dist/js/bootstrap.js"></script>

<script
	src="${pageContext.request.contextPath}/integratedQALabs/mobileLab/deviceSelectionMatrix/App.js"></script>


</head>

<body  ng-app="routerApp" data-ng-controller="mainController" >


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


<div class="row borderBottom">
	<ul class="list-inline marginLeft15px marginTop15px ">
		<li class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown" style="background-color: #f3f3f3;"><button
					class="btn mobileLabMenu">
					<b>Device Trends <span class="caret"></span>
				</button> </b> </a>
			<ul class="dropdown-menu">
		<!-- 		<li data-ng-controller="MarketGlobalController"><a
					href="javascript:void(0)" data-ng-click="getMarketGlobalData()">{{marketNames[0]}}</a></li>
 
 -->
 
  <li   data-ng-click="getDevicesData(marketIds[0])"  ><a href="javascript:void(0)">{{marketNames[0]}}</a></li> 
 
 
				<li  data-ng-click="getDevicesData(marketIds[1])"><a
					href="javascript:void(0)">{{marketNames[1]}}</a></li>

				<li data-ng-click="getDevicesData(marketIds[2])"><a
					href="javascript:void(0)">{{marketNames[2]}}</a></li>

			</ul></li>
			
			
			<li class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown"> <button class="btn mobileLabMenu"><b>Browser Trends<span class="caret"></span></button></b></a>
			<ul class="dropdown-menu">
				<!-- <li data-ng-controller="GlobalController"><a
					ui-sref="global">Global</a></li> -->
				<!-- <li><a
					ui-sref="us">US</a></li>
				<li ><a
					ui-sref="uk">UK</a></li> -->
				<li ng-click="getBrowserData(marketIds[0])"><a
					href="javascript:void(0)" >{{marketNames[0]}}</a></li>
					
				<li ng-click="getBrowserData(marketIds[1])"><a
					href="javascript:void(0)">{{marketNames[1]}}</a></li>
				<li ng-click="getBrowserData(marketIds[2])"><a
					href="javascript:void(0)">{{marketNames[2]}}</a></li>


			</ul></li>
			
		<li  class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown">  <button class="btn mobileLabMenu"><b>User Trends <span class="caret"></span>
			</button>
			</b>
		</a>
			<ul class="dropdown-menu">
				<li ng-click="getUserTrendsData()"><a
					href="javascript:void(0)">Trends</a></li>

			</ul></li>

		<li class="dropdown"><a class="dropdown-toggle buttonlink"
			href="${pageContext.request.contextPath}/digitalQAService/accessibilityTesting/viewAllocatedBatch"
			data-toggle="dropdown"><button class="btn mobileLabMenu"><b>Device Recommendation<span
					class="caret"></span> </button> </b></a>
			<ul class="dropdown-menu">
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceGlobal">{{marketNames[0]}}</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUS">{{marketNames[1]}}</a></li>
				<li><a
					href="../../mobileLab/deviceSelectionMatrix/RecommendDeviceUK">{{marketNames[2]}}</a></li>

			</ul></li>


		
	</ul>
	</div>
	
	
<!-- <li class="pull-right">
			<div class="upload">Update Database:</div>

            <div ng-controller="Ctrl" class="pull-left">
            <input type="file" name="file" class="btn btn-primary"  
                   onchange="angular.element(this).scope().loadFile(this.files)" /> 
                   
                   <input type="button"  value="Import Data" class="btn btn-primary updateButt"  
                   ng-click="handleFile()" />
                   </div>  
                   
			</li> -->
	<%-- 
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
	 --%>

		
	
	<div class=container>
		<div ui-view></div>
		</div>
		<!-- <div class="marginTop50px"></div> -->
	<!-- Footer -->

	<jsp:include page="../../../common/footer.jsp"></jsp:include>
	<!--     <div ng-include="'common/footer.html'" a></div> -->

		
</body>
</html>