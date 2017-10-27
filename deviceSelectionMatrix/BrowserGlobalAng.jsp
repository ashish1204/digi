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
</head>
<body ng-controller="BrowserController">
	
<div class="container">
		<div class="row">
			<li class="pull-right">
				<div class="upload">Update Database:</div>

				<div data-ng-controller="BrowserFileCtrl">
					<form method="post" data-ng-submit="handleFile()"
						enctype="multipart/form-data">

						<div class="form-group">
							<select class='form-control' placeholder='choose market'
								data-ng-model="id">
								<option value="" disabled selected>Select a market</option>
								<option value="1">{{marketNames[0]}}</option>

								<option value="2">{{marketNames[1]}}</option>
								<option value="3">{{marketNames[2]}}</option>
							</select>
						</div>
						<!--  	<input type="file" id="fileUpload" data-file-model="fileUpload"> -->
						<div class="form-group">
							<!-- 	<input type="file" id="fileUpload" data-file-model="fileUpload"> -->
							<input type="file" name="file" class="btn btn-primary"
								onchange="angular.element(this).scope().loadFile(this.files)" />
						</div>

						<input type="submit" value="update" />
					</form>
				</div>
			</li>
		</div>
	</div>
	&nbsp;&nbsp;&nbsp;&nbsp;
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
					<%-- height="60" --%>
					chart-labels="labels"
					chart-series="series" chart-options="options"> </canvas>
			</div>
		</div>



		<div class="col-md-4">
			<div class="well">

				<canvas id="bar" class="chart-doughnut" height="325"
					chart-data="dataForPieM" chart-labels="labelsForPieM"
					chart-options="options1"> </canvas>

			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-8">
			<div class="well">

				<canvas id="bar" class="chart-bar" chart-data="dataForTableD"
					<%--  height="60" --%>
					chart-labels="labelsForTableD"
					chart-series="seriesForTableD" chart-options="optionsForTableD">
				</canvas>



			</div>
		</div>
		<div class="col-md-4">
			<div class="well">
				<canvas id="bar" class="chart-doughnut" chart-data="dataForPieD"
					height="325" chart-labels="labelsForPieD" chart-options="optionsD">
				</canvas>
			</div>
		</div>
	</div>

</body>
</html>