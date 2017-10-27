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
</head>
<body>

	<div class="row">
		<li class="pull-right">
			<div class="upload">Update US Database:</div>

			<form name="userForm" method="post" class="pull-left"
				action="../../mobileLab/deviceSelectionMatrix/uploadFileExcelDataBrowserUS"
				enctype="multipart/form-data" onSubmit="return GoEmpty(this);">
				<input type="file" class="btn btn-primary" id="recentData"
					name="recentData"> <input type="submit" value="UPDATE"
					class="btn btn-primary updateButt">
			</form>
		</li>
	</div>
	
		
<div class="row">
<div class="col-md-8">
<h3>Most Used Browser Version for Mobile: {{bigVerMob}} </h3>
<h3>Value: {{bigMob}}</h3>
</div>

<div class="col-md-4">
<h3>Most Used Browser Version for Desktop: {{bigDeskName}}</h3>
<h3>Value: {{bigDesk}}</h3>
</div>
</div>
	
	<div class="row">
		<div class="col-md-8">
			<div class="well">
				<canvas id="bar" class="chart-bar" chart-data="data"
					chart-labels="labels" chart-series="series" chart-options="options">


				</canvas>
			</div>

		</div>
		<div class="col-md-4">
			<div class="well">
				<canvas id="bar" class="chart-doughnut" chart-data="dataForPieM" height=325
					chart-labels="labelsForPieM" chart-options="options1"> </canvas>
			</div>
		</div>
	</div>




	<div class="row">
		<div class="col-md-8">
			<div class="well">
				<canvas id="bar" class="chart-bar" chart-data="dataForTableD"
					chart-labels="labelsForTableD"
					chart-series="seriesForTableD" chart-options="optionsForTableD">
				</canvas>

			</div>
		</div>
		<div class="col-md-4">
			<div class="well"> 
				<canvas id="bar" class="chart-doughnut" chart-data="dataForPieD" height="325"
					chart-labels="labelsForPieD" chart-options="optionsD"> </canvas>


			</div>
		</div>
	</div>



	
</body>
</html>