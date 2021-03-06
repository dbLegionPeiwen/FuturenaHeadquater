'use strict';

angular.module('directives')

.directive('login',[function(){

	var login = {
		restrict: "E",
		templateUrl: "./directives/login/login.html",
		transclude: true,

		controller: function($scope, $http, $element, $cookieStore, $rootScope){
			$scope.State = "login";
			$scope.errorMessage = null;
			$scope.loginTime = 10;
			
			$scope.close = function(){		
			       $("#loginPanel").hide();
			       $("#blackOverlay").hide();
			       $scope.State= "login";
			}

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
                        $cookieStore.put("user",message.email);
                        $cookieStore.put("accessToken", message.access_token);
                        $cookieStore.put("userId", message.user_id);
                        $rootScope.currentUser = message.email;
                        $http.defaults.headers.common['Authentication-Header'] = message.access_token;                        
					}

				})}

                }
		}
	}

	return login
}])