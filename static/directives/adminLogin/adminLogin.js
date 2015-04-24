'use strict';

angular.module('directives')

.directive('admin',[function(){

	var admin = {
		restrict: "E",
		templateUrl: "./directives/adminLogin/adminLogin.html",
		transclude: true,

		controller: function($scope,$http,$element,$rootScope){
			$scope.adminLogin = function(){

				var admin={
					adminName: $scope.username,
					password: $scope.password,
					accessCode: $scope.access
				}

				$http.post("/consoleAccess",admin).success(function(state){

					if (state==true) {

						$rootScope.adminAccess = true;
						$scope.adminState = $rootScope.adminAccess;
						var url = "/product/getByState/pending"
                   		$http.get(url).success(function(_d){
                       		$scope.productFactory = _d;
                        })
					}
					else $scope.adminState = false;
				})
			}

			$scope.stateChange = function(state){
				$(".elem").removeClass('textDecoration');
			}
		}}

	return admin;
}])