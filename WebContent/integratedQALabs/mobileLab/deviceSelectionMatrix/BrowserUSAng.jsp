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
		<div class=col-md-6>
			<canvas id="bar" class="chart-bar" chart-data="data"
				chart-labels="labels" chart-series="series" chart-options="options">


			</canvas>

		</div>

		<div class=col-md-6>

			<canvas id="bar" class="chart-doughnut"
				chart-data="dataForPieM" chart-labels="labelsForPieM"
				chart-options="options1"> </canvas>
		</div>


	</div>

	<div class="row">
		<div class=col-md-6>
			<canvas id="bar" class="chart-bar" chart-data="dataForTableD"
				chart-labels="labelsForTableD" chart-series="seriesForTableD"
				chart-options="optionsForTableD"> </canvas>

		</div>


		<div class=col-md-6>
			<canvas id="bar" class="chart-doughnut"
				chart-data="dataForPieD" chart-labels="labelsForPieD"
				chart-options="optionsD"> </canvas>



		</div>
	</div>



</body>
</html>