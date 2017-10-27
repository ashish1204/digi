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

<!-- <script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script> -->

<script src="${pageContext.request.contextPath}/js/highchart.js"></script>
<script src="${pageContext.request.contextPath}/js/exporting.js"></script>

<script language="Javascript">

       function GoEmpty(){

              var a=document.forms["userForm"]["recentData"].value;
           
           if (a=="")
             {
             alert("Please select file...");
             return false;
             }
           return true;
       }
       

</script>
<script language="Javascript">

var data = ${chartData};  var chromeData = ${chromeData};  var firefoxData = ${firefoxData}; 
var safariData = ${safariData};  var ieData = ${ieData}; var operaData=${operaData};

var dataD = ${chartDataD};  var chromeDataD = ${chromeDataD};  var firefoxDataD = ${firefoxDataD}; 
var safariDataD = ${safariDataD};  var ieDataD = ${ieDataD}; var operaDataD=${operaDataD};

var mobileVersionData= ${mobileVersionData};

var DesktopVersionData = ${DesktopVersionData};

/* var chartData_ar = ${chartData_ar};
var chartData_os = ${chartData_os}; var iosData = ${iosData};  var androidData = ${androidData}; var windowData = ${windowData}; 
var chartData_iosr = ${chartData_iosr}; */
window.onload = function () {
    // Create the first chart
    Highcharts.chart('container', {
    		
    	        chart: {
    	            type: 'column'
    	        },
    	       
    	        xAxis: {
    	            categories: data ,
    	            crosshair: true
    	        },
    	        yAxis: {
    	            min: 0,
    	             title: {
    	                text: 'Percentage'
    	            } 
    	        },
    	        tooltip: {
    	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
    	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
    	                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
    	            footerFormat: '</table>',
    	            shared: true,
    	            useHTML: true
    	        },
    	        plotOptions: {
    	            column: {
    	                pointPadding: 0.2,
    	                borderWidth: 0
    	            }
    	        },
    	        series: [{
    	            name: 'Chrome',
    	            data: chromeData

    	        }, {
    	            name: 'Firefox',
    	            data: firefoxData

    	        }, {
    	            name: 'Safari',
    	            data: safariData

    	        }, {
    	            name: 'IE',
    	            data: ieData

    	        }, {
    	            name: 'Opera',
    	            data: operaData

    	        }]
    	    });
    	  

   Highcharts.getOptions().plotOptions.pie.colors = (function () {
        var colors = [],
            base = Highcharts.getOptions().colors[0],
            i;

        for (i = 0; i < 10; i += 1) {
            // Start out with a darkened base color (negative brighten), and end
            // up with a much brighter color
            colors.push(Highcharts.Color(base).brighten((i - 7) / 12).get());
        }
        return colors;
    }());

   
   Highcharts.chart('container2', {
		
       chart: {
           type: 'column'
       },
      
       xAxis: {
           categories: dataD ,
           crosshair: true
       },
       yAxis: {
           min: 0,
            title: {
               text: 'Percentage'
           } 
       },
       tooltip: {
           headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
           pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
               '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
           footerFormat: '</table>',
           shared: true,
           useHTML: true
       },
       plotOptions: {
           column: {
               pointPadding: 0.2,
               borderWidth: 0
           }
       },
       series: [{
           name: 'Chrome',
           data: chromeDataD

       }, {
           name: 'Firefox',
           data: firefoxDataD

       }, {
           name: 'Safari',
           data: safariDataD

       }, {
           name: 'IE',
           data: ieDataD

       }, {
           name: 'Opera',
           data: operaDataD

       }]
   });
 
   
   Highcharts.chart('container3', {
       chart: {
           plotBackgroundColor: null,
           plotBorderWidth: null,
           plotShadow: false,
           type: 'pie'
       },
      
       tooltip: {
           pointFormat: '{name}: <b>{point.percentage:.1f}%</b>'
       },
       plotOptions: {
           pie: {
               allowPointSelect: true,
               cursor: 'pointer',
               dataLabels: {
                   enabled: true
               },
               showInLegend: true,
               
           }
           },
     
       series: [{
          
           colorByPoint: true,
           data: mobileVersionData
       }]
   });
   
   
   Highcharts.chart('container4', {
       chart: {
           plotBackgroundColor: null,
           plotBorderWidth: null,
           plotShadow: false,
           type: 'pie'
       },
      
       tooltip: {
           pointFormat: '{name}: <b>{point.percentage:.1f}%</b>'
       },
       plotOptions: {
           pie: {
               allowPointSelect: true,
               cursor: 'pointer',
               dataLabels: {
                   enabled: true
               },
               showInLegend: true
           }
       },
       series: [{
          
           colorByPoint: true,
           data: DesktopVersionData
       }]
   });


};

   
   
   /*  Highcharts.chart('container2', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
       
        tooltip: {
            pointFormat: '{name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true
                },
                showInLegend: true,
                
            }
            },
      
        series: [{
           
            colorByPoint: true,
            data: chartData_ar
        }]
    });
    
    Highcharts.chart('container3', {
		
        chart: {
            type: 'column'
        },
       
        xAxis: {
            categories: data ,
            crosshair: true
        },
        yAxis: {
            min: 0,
             title: {
                text: 'Percentage'
            } 
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: 'iOS',
            data: iosData

        }, {
            name: 'Android',
            data: androidData

        }, {
            name: 'Windows',
            data: windowData

        }]
    });
  
    
    
    Highcharts.chart('container4', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
       
        tooltip: {
            pointFormat: '{name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true
                },
                showInLegend: true
            }
        },
        series: [{
           
            colorByPoint: true,
            data: chartData_iosr
        }]
    }); */


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
    	  
		window.open('http://10.102.22.86', '_blank');
	}

       /* function sendMail() {
              window.open('mailto:gaurav.dua@capgemini.com');
       } */
</script>


</head>
<body>



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
			
			
			
			

		<li class="active dropdown"><a class="dropdown-toggle buttonlink"
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





	<%-- <ul class="nav nav-tabs" style="margin-left: 15px; ">
			
				<li class="active" class="dropdown"><a style="padding: 0px !important;" class="dropdown-toggle"
					data-toggle="dropdown"><b><button class="btn " style="    /* color: #fff; */ background-color: #fff;  height: 50px; padding: 0 5px;/* border-color: #2e6da4;*/">Market
								Trends</button></b></a>
					<ul class="dropdown-menu">
						<li class="active"><a
							href="../../mobileLab/deviceSelectionMatrix/DSGlobalPage">Global</a></li>
						<li><a
							href="../../mobileLab/deviceSelectionMatrix/DSHomePage">US</a></li>
						<li><a
							href="../../mobileLab/deviceSelectionMatrix/DSHomeUKPage">UK</a></li>

					</ul></li>
				<li><a href="DSselectDevices.jsp" style="padding: 0px !important;"><b><button class="btn " style="    /* color: #fff; */ background-color: #fff; height: 50px; padding: 0 5px;/* border-color: #2e6da4;*/">Device Recommendation</button></b></a></li>
				<li><a style="padding: 0px !important;"
					href="../../mobileLab/deviceSelectionMatrix/DScloudDevices"><b><button class="btn " style="    /* color: #fff; */  padding: 0 5px; background-color: #fff; height: 50px;/* border-color: #2e6da4;*/">Device Selection</button></b></a></li>
					
				<li style=" float: right;">
					<div style="float:left; margin: 20px 5px 0 5px;">Update Global Database: </div>
							<form name="userForm" method="post" style="float:left;"
								action="../../mobileLab/deviceSelectionMatrix/uploadFileExcelDataGlobal.obj"
								enctype="multipart/form-data" onSubmit="return GoEmpty(this);">
							
								<div>
									<input type="file" class="btn btn-primary" id="recentData"
										name="recentData"
										style='float:left; margin: 3px; width: 205px; margin: 10px 3px;'>
									<input type="submit" style="float:left; margin: 10px 20px 0px 5px;" value="UPDATE" class="btn btn-primary"
										>
								</div>
							</form>
				</li>
			</ul> --%>


	<div class="container">
		<div class="row">
			<div class="marginTop15px"></div>
			<div class="col-md-7 col-lg-7 graphPartition">
				<h3 class="labelForGraph">Mobile Browser Distribution Details</h3>


				<div id="container" class="graphDiv"></div>

				<!-- <div id="chart" class="chart" style="float: left;"></div> -->

				<c:if test="${empty temp}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			
			
			 <div class="col-md-5 col-lg-5">
				<h3 class="labelForGraph">Mobile Browser Version Distribution</h3>



				<div id="container3" class="graphDiv"></div>


				<c:if test="${empty mobVersion}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			</div>
			
				<div class="row">
			<div class="col-md-12 col-lg-12">
				<div class="marginTop15px"></div>
				<div class="horizontalLine"></div>
			</div>
		</div>
			
				<div class="row">
			<div class="marginTop15px"></div>
			<div class="col-md-7 col-lg-7 graphPartition">
				<div class="marginTop15px"></div>
				<h3 class="labelForGraph">Desktop Browser Distribution Details</h3>
				<div id="container2" class="graphDiv"></div>

				<c:if test="${empty tempD}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			
			<div class="col-md-5 col-lg-5">
				<div class="marginTop15px"></div>
				<h3 class="labelForGraph">Desktop Browser Version Distribution</h3>
				<div id="container4" class="graphDiv"></div>
				<c:if test="${empty desktopBrowserVersion}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			<div class="marginTop15px"></div>
		</div>
		<div class="row">
			<div class="col-md-12 col-lg-12">
				<div class="marginTop15px"></div>
			</div>
		</div>
	</div>



			<%-- <div class="col-md-5 col-lg-5">
				<h3 class="labelForGraph">Android OS distribution by version</h3>



				<div id="container2" class="graphDiv"></div>


				<c:if test="${empty temp2}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>

		</div>
		<div class="row">
			<div class="col-md-12 col-lg-12">
				<div class="marginTop15px"></div>
				<div class="horizontalLine"></div>
			</div>
		</div>

		<div class="row">
			<div class="marginTop15px"></div>
			<div class="col-md-7 col-lg-7 graphPartition">
				<div class="marginTop15px"></div>
				<h3 class="labelForGraph">OS Distribution Details</h3>
				<div id="container3" class="graphDiv"></div>

				<c:if test="${empty temp1}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			<div class="col-md-5 col-lg-5">
				<div class="marginTop15px"></div>
				<h3 class="labelForGraph">iOS distribution by version</h3>
				<div id="container4" class="graphDiv"></div>
				<c:if test="${empty temp3}">
					<center>
						<b><font color="red" size="2"> No Records found...</font></b>
					</center>
				</c:if>
			</div>
			<div class="marginTop15px"></div>
		</div>
		<div class="row">
			<div class="col-md-12 col-lg-12">
				<div class="marginTop15px"></div> --%>
	



	<!-- <div class="marginTop15px"></div> -->


	<div>
		<!-- Footer -->
		<jsp:include page="../../../common/footer.jsp"></jsp:include>
		<!--     <div ng-include="'common/footer.html'" a></div> -->
	</div>
</body>
</html>
