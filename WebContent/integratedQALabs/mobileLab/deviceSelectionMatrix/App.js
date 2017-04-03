/**
 * 
 */

var routerApp = angular.module('routerApp', ['ui.router','chart.js']);
routerApp.config(function($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise('/global');
	
	
	$stateProvider
	.state('global', {
		url: '/global',
		templateUrl: 'BrowserGlobalAng.jsp',
		controller:"GlobalController"
	})

	.state('us',{
		url: '/us',
		templateUrl: 'BrowserUSAng.jsp',
		controller:"USController"
	}) 

	.state('uk',{
		url: '/uk',
		templateUrl: 'BrowserUKAng.jsp',
		controller:"UKController"
	});

});


routerApp.controller("GlobalController",function($scope,$http, $state)
		{

	$scope.labels = [];

	(function(){
		//	search();

	})();

	$scope.getGlobalData = function(){


		$state.go('global')

	}


	$scope.search=function ()
	{

		var months=[];
		var chromeData=[];
		var firefoxData=[];
		var safariData=[];
		var ieData=[];
		var operaData=[];
		var versions=[];
		var values=[];
		var versionsD=[];
		var valuesD=[];
		var monthsD=[];
		var chromeDataD=[];
		var firefoxDataD=[];
		var safariDataD=[];
		var ieDataD=[];
		var operaDataD=[];
		$http.get('http://localhost:8080/digiDEMO/integratedQALabs/mobileLab/deviceSelectionMatrix/BrowserGlobalPage').success(function (data) {


			for (var key in data.tableM) {
				if (data.tableM.hasOwnProperty(key)) {
					months.push(data.tableM[key].month);
					chromeData.push(data.tableM[key].chromeData);
					firefoxData.push(data.tableM[key].firefoxData);
					safariData.push(data.tableM[key].safariData);
					ieData.push(data.tableM[key].ieData);
					operaData.push(data.tableM[key].operaData);
					//var val = data[key].month;
					//console.log(val);
				}
			}

			
			

			for (var key in data.tableD) {
				if (data.tableD.hasOwnProperty(key)) {
					monthsD.push(data.tableD[key].month);
					chromeDataD.push(data.tableD[key].chromeData);
					firefoxDataD.push(data.tableD[key].firefoxData);
					safariDataD.push(data.tableD[key].safariData);
					ieDataD.push(data.tableD[key].ieData);
					operaDataD.push(data.tableD[key].operaData);
					//var val = data[key].month;
					//console.log(val);
				}
			}


			for(var key in data.pieM)
			{
				if (data.pieM.hasOwnProperty(key)) {
					versions.push(data.pieM[key].version);
					values.push(data.pieM[key].value);
				}

			}


			for(var key in data.pieD)
			{
				if (data.pieD.hasOwnProperty(key)) {
					versionsD.push(data.pieD[key].version);
					valuesD.push(data.pieD[key].value);
				}

			}

			$scope.labels=months;
			$scope.labelsForTableD=monthsD;
			
			$scope.labelsForPieM=versions;
			$scope.dataForPieM=values;

			$scope.labelsForPieD=versionsD;
			$scope.dataForPieD=valuesD;

			$scope.series = ['Chrome', 'Firefox','Safari','IE','Opera'];
			$scope.seriesForTableD = ['Chrome', 'Firefox','Safari','IE','Opera'];
			$scope.colorD=['#FF0000','#800000','#FFFF00','#FF5733','#3633FF']


			$scope.options={legend:{display:true,position:'bottom'},title:{display:true,text:'Mobile Browser Distribution Details',fontSize:25,fontWeight:'bold'}};
			
			$scope.optionsForTableD={legend:{display:true,position:'bottom'},title:{display:true,text:'Desktop Browser Distribution Details',fontSize:25,fontWeight:'bold'}};

			$scope.options1= { legend: { display: true, position :'bottom'},title :{display:true,text:'Mobile Browser Version Details',fontSize:25,fontWeight:'bold'} };/*,cutoutPercentage: 40*/

			$scope.optionsD= { legend: { display: true, position :'bottom'},title :{display:true,text:'Desktop Browser Version Details',fontSize:25,fontWeight:'bold'} };


			$scope.data = [
				chromeData,
				firefoxData,
				safariData,
				ieData,
				operaData
				];
			$scope.dataForTableD = [
				chromeDataD,
				firefoxDataD,
				safariDataD,
				ieDataD,
				operaDataD
				];


		});

	};



	//console.log("HiASDFF");

	$scope.search();
		})




		////US CONTROLLER

		routerApp.controller("USController",function($scope,$http, $state)
				{

			$scope.labels = [];


			(function(){
				//search();
				//pieM();
			})();


			$scope.getUSData = function(){
				//alert('getUSData');


				$state.go('us');

			};


			$scope.search=function ()
			{
				var months=[];
				var chromeData=[];
				var firefoxData=[];
				var safariData=[];
				var ieData=[];
				var operaData=[];
				var versions=[];
				var values=[];
				var versionsD=[];
				var valuesD=[]
				var monthsD=[];
				var chromeDataD=[];
				var firefoxDataD=[];
				var safariDataD=[];
				var ieDataD=[];
				var operaDataD=[];
				$http.get('http://localhost:8080/digiDEMO/integratedQALabs/mobileLab/deviceSelectionMatrix/BrowserUSPage').success(function (data) {

					//console.log("yes"+data);
					console.log("endf");
					//	console.log(data);




					for (var key in data.tableM) {
						if (data.tableM.hasOwnProperty(key)) {
							months.push(data.tableM[key].month);
							chromeData.push(data.tableM[key].chromeData);
							firefoxData.push(data.tableM[key].firefoxData);
							safariData.push(data.tableM[key].safariData);
							ieData.push(data.tableM[key].ieData);
							operaData.push(data.tableM[key].operaData);
						}
					}
					

					for (var key in data.tableD) {
						if (data.tableD.hasOwnProperty(key)) {
							monthsD.push(data.tableD[key].month);
							chromeDataD.push(data.tableD[key].chromeData);
							firefoxDataD.push(data.tableD[key].firefoxData);
							safariDataD.push(data.tableD[key].safariData);
							ieDataD.push(data.tableD[key].ieData);
							operaDataD.push(data.tableD[key].operaData);
						}
					}
					
					

					for(var key in data.pieM)
					{
						if (data.pieM.hasOwnProperty(key)) {
							versions.push(data.pieM[key].version);
							values.push(data.pieM[key].value);
						}

					}

					for(var key in data.pieD)
					{
						if (data.pieD.hasOwnProperty(key)) {
							versionsD.push(data.pieD[key].version);
							valuesD.push(data.pieD[key].value);
						}

					}


					$scope.labels=months;
					$scope.labelsForTableD=monthsD;
					
					
					$scope.labelsForPieM=versions;
					$scope.dataForPieM=values;
					
					$scope.labelsForPieD=versionsD;
					$scope.dataForPieD=valuesD;
					
					
					$scope.options1= { legend: { display: true, position :'bottom'},title :{display:true,text:'Mobile Browser Version Details',fontSize:25,fontWeight:'bold'} };/*,cutoutPercentage: 40*/ 

					$scope.optionsD= { legend: { display: true, position :'bottom'},title :{display:true,text:'Desktop Browser Version Details',fontSize:25,fontWeight:'bold'} };
					

					$scope.series = ['Chrome', 'Firefox','Safari','IE','Opera'];
					$scope.seriesForTableD = ['Chrome', 'Firefox','Safari','IE','Opera'];

					$scope.options={legend:{display:true,position:'bottom'},title:{display:true,text:'Mobile Browser Distribution Details',fontSize:25,fontWeight:'bold'}};

					$scope.optionsForTableD={legend:{display:true,position:'bottom'},title:{display:true,text:'Desktop Browser Distribution Details',fontSize:25,fontWeight:'bold'}};
					
					
					$scope.data = [
						chromeData,
						firefoxData,
						safariData,
						ieData,
						operaData
						];
					
					$scope.dataForTableD = [
						chromeDataD,
						firefoxDataD,
						safariDataD,
						ieDataD,
						operaDataD
						];

				});
			};


			$scope.search();

				})






				//UKCONTROLLER

				routerApp.controller("UKController",function($scope,$http, $state)
						{

					$scope.labels = [];

					$scope.getUKData = function(){
						//alert('getUKData');
						$state.go('uk');
					}


					$scope.search= function() 
					{
						var months=[];
						var chromeData=[];
						var firefoxData=[];
						var safariData=[];
						var ieData=[];
						var operaData=[];
						var versions=[];
						var values=[];
						var versionsD=[];
						var valuesD=[];
						var monthsD=[];
						var chromeDataD=[];
						var firefoxDataD=[];
						var safariDataD=[];
						var ieDataD=[];
						var operaDataD=[];

						//	console.log("in ");
						$http.get('http://localhost:8080/digiDEMO/integratedQALabs/mobileLab/deviceSelectionMatrix/BrowserUKPage').success(function (data) {


							for (var key in data.tableM) {
								if (data.tableM.hasOwnProperty(key)) {
									months.push(data.tableM[key].month);
									chromeData.push(data.tableM[key].chromeData);
									firefoxData.push(data.tableM[key].firefoxData);
									safariData.push(data.tableM[key].safariData);
									ieData.push(data.tableM[key].ieData);
									operaData.push(data.tableM[key].operaData);
									//var val = data[key].month;
									//console.log(val);
								}
							}

							
							for (var key in data.tableD) {
								if (data.tableD.hasOwnProperty(key)) {
									monthsD.push(data.tableD[key].month);
									chromeDataD.push(data.tableD[key].chromeData);
									firefoxDataD.push(data.tableD[key].firefoxData);
									safariDataD.push(data.tableD[key].safariData);
									ieDataD.push(data.tableD[key].ieData);
									operaDataD.push(data.tableD[key].operaData);
									//var val = data[key].month;
									//console.log(val);
								}
							}

							
							

							for(var key in data.pieM)
							{
								if (data.pieM.hasOwnProperty(key)) {
									versions.push(data.pieM[key].version);
									values.push(data.pieM[key].value);
								}

							}
							
							for(var key in data.pieD)
							{
								if (data.pieD.hasOwnProperty(key)) {
									versionsD.push(data.pieD[key].version);
									valuesD.push(data.pieD[key].value);
								}

							}



							$scope.labels=months;
							$scope.labelsForTableD=monthsD;
							
							
							$scope.labelsForPieM=versions;
							$scope.dataForPieM=values;
							
							$scope.labelsForPieD=versionsD;
							$scope.dataForPieD=valuesD;
							
							
							$scope.options1= { legend: { display: true, position :'bottom'},title :{display:true,text:'Mobile Browser Version Details',fontSize:25,fontWeight:'bold'} };/*,cutoutPercentage: 40*/ 
							$scope.optionsD= { legend: { display: true, position :'bottom'},title :{display:true,text:'Desktop Browser Version Details',fontSize:25,fontWeight:'bold'} };


							$scope.series = ['Chrome', 'Firefox','Safari','IE','Opera'];
							$scope.seriesForTableD = ['Chrome', 'Firefox','Safari','IE','Opera'];

							$scope.options={legend:{display:true,position:'bottom'},title:{display:true,text:'Mobile Browser Distribution Details',fontSize:25,fontWeight:'bold'}};
							
							$scope.optionsForTableD={legend:{display:true,position:'bottom'},title:{display:true,text:'Desktop Browser Distribution Details',fontSize:25,fontWeight:'bold'}};

							
							$scope.data = [
								
								
								chromeData,
								firefoxData,
								safariData,
								ieData,
								operaData
								];
							
							$scope.dataForTableD = [
								chromeDataD,
								firefoxDataD,
								safariDataD,
								ieDataD,
								operaDataD
								];


						});
					};

					$scope.search();

				//	console.log("HiASDFF");



						})	
