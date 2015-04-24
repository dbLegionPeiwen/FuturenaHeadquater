'use strict';

angular.module('directives')

.directive('register',[function(){

	var register = {
		restrict: "E",
		templateUrl: "./directives/register/register.html",
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