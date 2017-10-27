/**
 * 
 */

var routerApp = angular.module('routerApp', ['ui.router','chart.js']);
routerApp.config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/devicesTrends');


	$stateProvider

	
	.state('devicesTrends', {
		url: '/devicesTrends',
		templateUrl: 'DSGlobalAng.jsp',
		params:{myParam:{id:1}},
		controller:"deviceTrends"
	})

	.state('browserTrends',{
		url:"/browserTrends",
		templateUrl: 'BrowserGlobalAng.jsp',
		params:{myParam:{id:1}},
		controller:"BrowserController"
	})
	

	.state('userTrends', {
		url: '/userTrends',
		templateUrl: 'UserTrendsAng.jsp',
		controller:"UserTrendsController"
	})
	/*.state('userTrends1', {
		url: '/userTrends1',
		templateUrl: 'UserTrendsAng.jsp',
		controller:"UserTrendsController"
	})
*/
});



//DEVICES FILE CONTROLLER

routerApp.controller("DeviceFileCtrl",function($scope,$http,$state){
	 
	 $scope.selectedFile = null;
	  
	
	 $scope.loadFile = function (files) {  
		  
	        $scope.$apply(function () {  
	  
	        	$scope.selectedFile=files[0];
	  
	        })  
	  
	    }  
	  $scope.handleFile = function () {  
		  
		
	     var file = $scope.selectedFile;
	    
	    
	    	var fd = new FormData();
	    
	    	fd.append('id',angular.toJson($scope.id,true));
	    	fd.append('file',file);
	     
	        $http({
                url: '../../mobileLab/deviceSelectionMatrix/updateDeviceTrends',
                method: 'POST',
                data: fd,
              transformRequest : angular.identity,
                headers : {'Content-Type' : undefined}
                
              })
              .success(function(data, status) {                       
               var marketId=data.ID;
            	  if(data.Result=="success"){
            		  $state.go('devicesTrends',{myParam:{id:marketId}})
            	  }
            	  
              })
              .error(function(data, status) {
                  alert("Error ... " + status);
              });
   
	  }
	 });

//BROWSER FILE CONTROLLER

routerApp.controller("BrowserFileCtrl",function($scope,$http,$state){
	 
	 $scope.selectedFile = null;
	  
	
	 $scope.loadFile = function (files) {  
		  
	        $scope.$apply(function () {  
	  
	        	$scope.selectedFile=files[0];
	  
	        })  
	  
	    }  
	  $scope.handleFile = function () {  
		  
		
	     var file = $scope.selectedFile;
	    
	    
	    	var fd = new FormData();
	    
	    	fd.append('id',angular.toJson($scope.id,true));
	    	fd.append('file',file);
	     
	        $http({
                url: '../../mobileLab/deviceSelectionMatrix/updateBrowserTrends',
                method: 'POST',
                data: fd,
              transformRequest : angular.identity,
                headers : {'Content-Type' : undefined}
                
              })
              .success(function(data, status) {                       
               var marketId=data.ID;
            	  if(data.Result=="success"){
            		  $state.go('browserTrends',{myParam:{id:marketId}})
            	  }
            	  
              })
              .error(function(data, status) {
                  alert("Error ... " + status);
              });
   
	  }
	 });



//USER TRENDS FILE CONTROLLER

routerApp.controller("userTrendsFileCtrl",function($scope,$http,$state){
	
	 $scope.selectedFile = null;
	  
		
	 $scope.loadFile = function (files) {  
		  
	        $scope.$apply(function () {  
	  
	        	$scope.selectedFile=files[0];
	  
	        })  
	  
	    }  
	 
	  $scope.handleFile = function () {  
		  
			
		     var file = $scope.selectedFile;
		    
		    
		    	var fd = new FormData();
		    
		    	fd.append('file',file);
		     
		        $http({
	                url: '../../mobileLab/deviceSelectionMatrix/updateUserTrends',
	                method: 'POST',
	                data: fd,
	              transformRequest : angular.identity,
	                headers : {'Content-Type' : undefined}
	                
	              })
	              .success(function(data, status) {                       
	               
	            	  if(data.Result=="success"){
	            		  console.log("hi")
	            		
	            		  $state.reload();
	            	  }
	              })
	              .error(function(data, status) {
	                  alert("Error ... " + status);
	              });
	   
		  }

});



///MAIN CONTROLLER

routerApp.controller("mainController",function($scope,$http,$state){
	
	var marketNames=[];
	var marketIds=[];
	$http.get("../../mobileLab/deviceSelectionMatrix/getMarkets").success(function(data){
		//console.log("hi");
		//console.log(data.length);
		
		for(var i=0;i<data.length;i++){
			marketNames[i]=data[i].marketName;
    		marketIds[i]=data[i].marketId;
			//console.log(data[i].marketName);
		}
	/*console.log(marketNames);
		console.log(marketIds);*/
		$scope.marketNames=marketNames;
		$scope.marketIds=marketIds;
		
		$scope.getDevicesData=function(id){

			//console.log(id+"ASH")
			
			$state.go('devicesTrends',{myParam:{id:id}});
			
		
		}
		
		$scope.getBrowserData=function(id){
		//	console.log(id);
			$state.go('browserTrends',{myParam:{id:id}})
		}
		
		$scope.getUserTrendsData=function(){
			$state.go('userTrends');
		}
	
	});

});




//Device Trends Controller
routerApp.controller("deviceTrends",function($scope,$http,$state,$stateParams){
	
	
	// console.log($stateParams.myParam)
	$scope.getData=function(){
		
	var months=[];
	var appleData=[];
	var lgData=[];
	var googleData=[];
	var samsungData=[];
	var motoData=[];

	var monthsOS=[];
	var iosData=[];
	var androidData=[];
	var windowsData=[];

	var versionsAnd=[];
	var valuesAnd=[];
	var versionsIOS=[];
	var valuesIOS=[];
	

		 $http({
	          method : "POST",
	          url : "../../mobileLab/deviceSelectionMatrix/getDevicesData",
	          dataType: 'json',
	          data    :"\"id\":"+$stateParams.myParam.id 
	      }).success(function(data) {
	          

				for (var key in data.tableOEM) {
					if (data.tableOEM.hasOwnProperty(key)) {
						months.push(data.tableOEM[key].month);
						appleData.push(data.tableOEM[key].appleData);
						lgData.push(data.tableOEM[key].lgData);
						googleData.push(data.tableOEM[key].googleData);
						samsungData.push(data.tableOEM[key].samsungData);
						motoData.push(data.tableOEM[key].motoDatata);

					}
				}


				for (var key in data.tableOS) {
					if (data.tableOS.hasOwnProperty(key)) {
						monthsOS.push(data.tableOS[key].month);
						iosData.push(data.tableOS[key].iosData);
						androidData.push(data.tableOS[key].androidData);
						windowsData.push(data.tableOS[key].windowsData);


					}
				}


				for(var key in data.pieAndroid)
				{
					if (data.pieAndroid.hasOwnProperty(key)) {
						versionsAnd.push(data.pieAndroid[key].version);
						valuesAnd.push(data.pieAndroid[key].value);
					}

				}
				var bigAnd=valuesAnd[0];
				var sizeAnd=valuesAnd.length;
				var bigVerAnd=versionsAnd[0];
				for(var i=0;i<sizeAnd;i++){
					if(valuesAnd[i]>bigAnd){


						bigAnd=valuesAnd[i];
						bigVerAnd=versionsAnd[i];
					}
				}
				$scope.bigAnd=bigAnd;
				$scope.bigVerAnd=bigVerAnd;
				
				for(var key in data.pieIOS)
				{
					if (data.pieIOS.hasOwnProperty(key)) {
						versionsIOS.push(data.pieIOS[key].version);
						valuesIOS.push(data.pieIOS[key].value);
					}

				}
				var bigIOS=valuesIOS[0];
				var sizeIOS=valuesIOS.length;
				var bigVerIOS=versionsIOS[0];
				for(var i=0;i<sizeIOS;i++){
					if(valuesIOS[i]>bigIOS){

						bigIOS=valuesIOS[i];
						bigVerIOS=versionsIOS[i];
					}
				}
				$scope.bigIOS=bigIOS;
				$scope.bigVerIOS=bigVerIOS;

				$scope.labels=months;
				$scope.series=['Apple','LG','Google','Samsung','Motorola'];
				$scope.options={legend:{display:true,position:'bottom'},title:{display:true,text:'OEM Distribution Details',fontSize:25,fontWeight:'bold'}};
				$scope.data = [
					appleData,
					lgData,
					googleData,
					samsungData,
					motoData
					];


				$scope.labelsOS=monthsOS;
				$scope.seriesOS=['IOS','Android','Windows']
				$scope.optionsOS={legend:{display:true,position:'bottom'},title:{display:true,text:'OS Distribution Details',fontSize:25,fontWeight:'bold'}};
				$scope.dataOS = [
					iosData,
					androidData,
					windowsData,

					];

				$scope.labelsForPieIOS=versionsIOS;
				$scope.dataForPieIOS=valuesIOS;
				$scope.optionsIOS={legend: { display: true, position :'bottom'},title :{display:true,text:'IOS Version Distribution',fontSize:25,fontWeight:'bold'}};

				$scope.labelsForPieAndroid=versionsAnd;
				$scope.dataForPieAndroid=valuesAnd;
				$scope.optionsAndroid={legend: { display: true, position :'bottom'},title :{display:true,text:'Android Version Distribution',fontSize:25,fontWeight:'bold'}};


	      })
		
		
		 
	  }
	$scope.getData();
	
});
	 

//Browser TRENDS CONTROLLER



routerApp.controller("BrowserController",function($scope, $http,$stateParams){
	
	
	$scope.labels = [];
	
console.log($stateParams.myParam.id);
	$scope.getData=function ()
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


		 $http({
	          method : "POST",
	          url : "../../mobileLab/deviceSelectionMatrix/getBrowserData",
	          dataType: 'json',
	          data    :"\"id\":"+$stateParams.myParam.id 
	      }).success(function (data) {
	    	  
	    	  console.log(data);

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

			var bigDesk=valuesD[0];
			var sizeDesk=valuesD.length;
			var bigDeskName=versionsD[0];
			for(var i=0;i<sizeDesk;i++){
				if(valuesD[i]>bigDesk){


					bigDesk=valuesD[i];
					bigDeskName=versionsD[i];
				}
			}
			$scope.bigDesk=bigDesk;
			$scope.bigDeskName=bigDeskName;



			var bigMob=values[0];
			var sizeMob=values.length;
			var bigVerMob=versions[0];
			for(var i=0;i<sizeMob;i++){
				if(values[i]>bigMob){

					bigMob=values[i];
					bigVerMob=versions[i];
				}
			}
			$scope.bigMob=bigMob;
			$scope.bigVerMob=bigVerMob;

			$scope.labels=months;
			$scope.labelsForTableD=monthsD;

			$scope.labelsForPieM=versions;
			$scope.dataForPieM=values;

			$scope.labelsForPieD=versionsD;
			$scope.dataForPieD=valuesD;

			$scope.series = ['Chrome', 'Firefox','Safari','IE','Opera'];
			$scope.seriesForTableD = ['Chrome', 'Firefox','Safari','IE','Opera'];
			$scope.color=['#7DBADB','#BEC6C8','#EC2B0D','#14A9AB','#DAC00A']


			$scope.options={legend:{display:true,position:'bottom'},  /*scales: {
xAxes: [{
gridLines: {
display: false
}
}],
yAxes: [{
gridLines: {
display: false
}
}]
},*/title:{display:true,text:'Mobile Browser Distribution Details',fontSize:25,fontWeight:'bold'}};

			$scope.optionsForTableD={legend:{display:true,position:'bottom'},title:{display:true,text:'Desktop Browser Distribution Details',fontSize:25,fontWeight:'bold'}};

			$scope.options1= { legend: { display: true, position :'bottom'},title :{display:true,text:'Mobile Browser Versions',fontSize:25,fontWeight:'bold'} };/*,cutoutPercentage: 40*/

			$scope.optionsD= { legend: { display: true, position :'bottom'},title :{display:true,text:'Desktop Browser Versions',fontSize:25,fontWeight:'bold'} };


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


	$scope.getData();
});







	/////USER TRENDS CONTROLLER

						routerApp.controller("UserTrendsController",function($scope,$http,$state,$stateParams)
								{

						

							$scope.getData=function(){

								var months=[];
								var appleData=[];
								var lgData=[];
								var googleData=[];
								var samsungData=[];
								var motoData=[];

								var monthsOS=[];
								var iosData=[];
								var androidData=[];
								var windowsData=[];

								var versionsAnd=[];
								var valuesAnd=[];
								var versionsIOS=[];
								var valuesIOS=[];
								$http.get("../../mobileLab/deviceSelectionMatrix/DSUserTrendsPage").success(function(data){


									for (var key in data.tableOEM) {
										if (data.tableOEM.hasOwnProperty(key)) {
											months.push(data.tableOEM[key].month);
											appleData.push(data.tableOEM[key].appleData);
											lgData.push(data.tableOEM[key].lgData);
											googleData.push(data.tableOEM[key].googleData);
											samsungData.push(data.tableOEM[key].samsungData);
											motoData.push(data.tableOEM[key].motoDatata);

										}
									}


									for (var key in data.tableOS) {
										if (data.tableOS.hasOwnProperty(key)) {
											monthsOS.push(data.tableOS[key].month);
											iosData.push(data.tableOS[key].iosData);
											androidData.push(data.tableOS[key].androidData);
											windowsData.push(data.tableOS[key].windowsData);


										}
									}


									for(var key in data.pieAndroid)
									{
										if (data.pieAndroid.hasOwnProperty(key)) {
											versionsAnd.push(data.pieAndroid[key].version);
											valuesAnd.push(data.pieAndroid[key].value);
										}

									}


									for(var key in data.pieIOS)
									{
										if (data.pieIOS.hasOwnProperty(key)) {
											versionsIOS.push(data.pieIOS[key].version);
											valuesIOS.push(data.pieIOS[key].value);
										}

									}

									var bigAnd=valuesAnd[0];
									var sizeAnd=valuesAnd.length;
									var bigVerAnd=versionsAnd[0];
									for(var i=0;i<sizeAnd;i++){
										if(valuesAnd[i]>bigAnd){


											bigAnd=valuesAnd[i];
											bigVerAnd=versionsAnd[i];
										}
									}
									$scope.bigAnd=bigAnd;
									$scope.bigVerAnd=bigVerAnd;



									var bigIOS=valuesIOS[0];
									var sizeIOS=valuesIOS.length;
									var bigVerIOS=versionsIOS[0];
									for(var i=0;i<sizeIOS;i++){
										if(valuesIOS[i]>bigIOS){

											bigIOS=valuesIOS[i];
											bigVerIOS=versionsIOS[i];
										}
									}
									$scope.bigIOS=bigIOS;
									$scope.bigVerIOS=bigVerIOS;

									$scope.labels=months;
									$scope.series=['Apple','LG','Google','Samsung','Motorola'];
									$scope.options={legend:{display:true,position:'bottom'},title:{display:true,text:'OEM Distribution Details',fontSize:25,fontWeight:'bold'}};
									$scope.data = [
										appleData,
										lgData,
										googleData,
										samsungData,
										motoData
										];


									$scope.labelsOS=monthsOS;
									$scope.seriesOS=['IOS','Android','Windows']
									$scope.optionsOS={legend:{display:true,position:'bottom'},title:{display:true,text:'OS Distribution Details',fontSize:25,fontWeight:'bold'}};
									$scope.dataOS = [
										iosData,
										androidData,
										windowsData,

										];

									$scope.labelsForPieIOS=versionsIOS;
									$scope.dataForPieIOS=valuesIOS;
									$scope.optionsIOS={legend: { display: true, position :'bottom'},title :{display:true,text:'IOS Version Distribution',fontSize:25,fontWeight:'bold'}};

									$scope.labelsForPieAndroid=versionsAnd;
									$scope.dataForPieAndroid=valuesAnd;
									$scope.optionsAndroid={legend: { display: true, position :'bottom'},title :{display:true,text:'Android Version Distribution',fontSize:25,fontWeight:'bold'}};


								})




							}

							$scope.getData();
								})



							