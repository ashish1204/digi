routerApp.controller("GlobalController",function($scope,$http, $state)
		{

	$scope.labels = [];
	
	(function(){
		search();
	})();
	
	$scope.getGlobalData = function(){
		/*search();
		second();
		third();
		fourth();*/
		$state.go('global')
		
	}
	
	function search() 
  	{
		//alert('hi11');
		var months=[];
		var chromeData=[];
		var firefoxData=[];
		var safariData=[];
		var ieData=[];
		var operaData=[];
	//	console.log("in ");
  		$http.get('http://localhost:8080/digiDEMO/integratedQALabs/mobileLab/deviceSelectionMatrix/BrowserGlobalPage').success(function (data) {
  		//console.log("Received ff");
  			
  			$scope.status1 = data;
  			
  			/*data = {
  					"bdd": [{
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}, {
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}],
  					"v&v": [{
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}, {
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}],
  					"third": [{
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}, {
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}],
  					"fourth": [{
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}, {
  						"month": "2016-Jan",
  						"chromeData": 12.1,
  						"firefoxData": 12.1,
  						"safariData": 0.0,
  						"ieData": 12.1,
  						"operaData": 0.0
  					}]
  				};
  				
  				take data.bdd for first graph, similarly for others;
  				
  				*
  				*/
  			
  			$scope.status=data;
  			console.log("yes"+data);
  			console.log("endf");
  			 
 
  			/*$scope.data1=data;
  			console.log($scope.data1);*/
  			
  			//var months=[];
  			
  			//console.log(data.chromeData);
  			
  			
  			
  			
  			for (var key in data) {
  			  if (data.hasOwnProperty(key)) {
  				  months.push(data[key].month);
  				  chromeData.push(data[key].chromeData);
  				  firefoxData.push(data[key].firefoxData);
  				 safariData.push(data[key].safariData);
  				 ieData.push(data[key].ieData);
  				 operaData.push(data[key].operaData);
  				 //var val = data[key].month;
  			    //console.log(val);
  			  }
  			}

  		  	$scope.labels=months;
  			
  			  $scope.series = ['Chrome', 'Firefox','Safari','IE','Opera'];

  			  $scope.data = [
  			    chromeData,
  			    firefoxData,
  			    safariData,
  			    ieData,
  			    operaData
  			  ];
  			
  		
  			
  			//console.log(months[1]);
  			
  			
  			
  			
  			
  			
  			
  			//console.log("Hii12312"+$scope.data1)
  			
  			
  		});
  		
  	};
  	
  	//$scope.search();
	
	
	
console.log("HiASDFF");
	//$scope.data1="Gandhi";
	/* $scope.labels = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
	  $scope.series = ['Series A', 'Series B'];

	  $scope.data12 = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	*/
	
	
	
	
  	
  	
  	
  	
  	
  //	
  	
  	
  	
	
	
		});