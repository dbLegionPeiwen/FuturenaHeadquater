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
                        $cookieStore.put("user",message.email);
                        $cookieStore.put("accessToken", message.access_token);
                        $cookieStore.put("userId", message.user_id);
                        $rootScope.currentUser = message.email;
                        $http.defaults.headers.common['Authentication-Header'] = message.access_token; 
                        
					}

				})
			}
		}
	}

	return register;
}])