<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
<body>

	<div class="row">
		<li class="pull-right">
			<div class="upload">Update US Database:</div>

			<form name="userForm" method="post" class="pull-left"
				<%-- data-ng-controller='updateController' --%>
 				action="../../mobileLab/deviceSelectionMatrix/uploadFileExcelData"
				enctype="multipart/form-data" onSubmit="return GoEmpty(this);">
				<input type="file" class="btn btn-primary" id="recentData"
					name="recentData"> <input type="submit" value="UPDATE"
					class="btn btn-primary updateButt">
			</form>
		</li>
	</div>

	<div class="row">
		<div class="col-md-8">
			<h3>Most Used Android Version: {{bigVerAnd}}</h3>
			<h3>Value: {{bigAnd}}</h3>
		</div>

		<div class="col-md-4">
			<h3>Most Used IOS Version: {{bigVerIOS}}</h3>
			<h3>Value: {{bigIOS}}</h3>
		</div>
		</div>


		<div class="row">
			<div class="col-md-8">
				<div class="well">
					<canvas id="bar" class="chart-bar" chart-data="data"
						chart-labels="labels" chart-series="series"
						chart-options="options"> </canvas>
				</div>

			</div>
			<div class="col-md-4">
				<div class="well">
					<canvas id="bar" class="chart-doughnut" chart-data="dataForPieIOS"
						height="325" chart-labels="labelsForPieIOS"
						chart-options="optionsIOS"> </canvas>
				</div>
			</div>
		</div>




		<div class="row">
			<div class="col-md-8">
				<div class="well">
					<canvas id="bar" class="chart-bar" chart-data="dataOS"
						chart-labels="labelsOS" chart-series="seriesOS"
						chart-options="optionsOS"> </canvas>

				</div>
			</div>
			<div class="col-md-4">
				<div class="well">
					<canvas id="bar" class="chart-doughnut"
						chart-data="dataForPieAndroid" height="325"
						chart-labels="labelsForPieAndroid" chart-options="optionsAndroid">
					</canvas>

				</div>
			</div>
		</div>
</body>
</html>