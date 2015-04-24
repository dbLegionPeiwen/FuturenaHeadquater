/**
* @author: Hao Qiao
*/

'use strict';

angular.module('directives',[])

.directive('product',[function(){

    var product = {
        			restrict: "E",
        			templateUrl: "./directives/product.html",
        			transclude: true,

        			controller: function($scope, $http, $element, $rootScope, $state){
						$scope.productInitial = function(name,id){
							$scope.productName = name;
							$scope.productID = id;
						};

						$scope.home = function(){
							return $state.includes('home');
						};

						$scope.admin = function(){
							return $state.includes('admin');
						};
        			}

        		    }
    return product;

}])

.directive('login',[function(){

	var login = {
		restrict: "E",
		templateUrl: "./directives/login.html",
		transclude: true,

		controller: function($scope, $http, $element){
			$scope.State = "login";
			$scope.errorMessage = null;
			$scope.loginTime = 10;

			$scope.auth = function() {

				var user = {
					email: $scope.email,
					password: $scope.password,
					grant_type: 'password',
					client_id: 'DBLI',
					client_secret: '2649c5732a1b'
				}

				if ($scope.loginTime>0) {
				$http.post('/authenticate',user).success(function(message){

					if (message.status) {
						$scope.errorMessage = message.message;
						console.log($scope.errorMessage);
						$scope.loginTime--;
						if ($scope.loginTime < 1) {
							$scope.errorMessage ="Please try Later"
						}
					}
					else {

						$("#mainContainer").removeClass("disableScroll");
                        $("#loginPanel").hide();
                        $("#blackOverlay").hide();
                        $scope.loginSuccess = true;
					}

				})}


                }
		}
	}

	return login
}])

.directive('register',[function(){

	var register = {
		restrict: "E",
		templateUrl: "./directives/register.html",
		transclude: true,

		controller: function($scope, $http, $element){

			$scope.register = function(){

				var user = {
					email: $scope.email,
					username: $scope.username,
					password: $scope.password
				}

				$http.post("/user/register",user).success(function(message){
					if (message.status) {
						$scope.errorMessage = message.message;
					}

					else{
						$("#mainContainer").removeClass("disableScroll");
                    	$("#loginPanel").hide();
                        $("#blackOverlay").hide();
                        $scope.loginSuccess = true;
					}

				})
			}
		}
	}

	return register;
}])

.directive('admin',[function(){

	var admin = {
		restrict: "E",
		templateUrl: "./directives/_login.html",
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