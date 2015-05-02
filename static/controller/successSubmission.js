'use strict';

angular.module('controller')

.controller('successSubmission', function($http, $scope, $stateParams) {
	
	$scope.code= $stateParams.code;
	
	$http.get('/successSubmission/'+$scope.code).success(function(message){		
		if (message.body.message == null) console.log(JSON.parse(message.body));
	});
	
});