'use strict';

angular.module('directives')

.directive('product',[function(){

    var product = {
        			restrict: "E",
        			templateUrl: "./directives/product/product.html",
        			transclude: true,

        			controller: function($scope, $http, $element, $rootScope, $state, $cookieStore){

						$scope.productInitial = function(name,id){
							$scope.productName = name;
							$scope.productID = id;
							$scope.imageUrl = "/UploadImages/"+id+"0.jpeg"
						};

						$scope.home = function(){
							return $state.includes('home');
						};

						$scope.admin = function(){
							return $state.includes('admin');
						};
						
						$scope.user = function(){
							return $state.includes('user');
						};
											
						$scope.auth = function(){
							
							if ($rootScope.currentUser == null) {
						         $(".userSignInUp").css("top",$(window).scrollTop()+150);
						         $("#blackOverlay").css("top",$(window).scrollTop());
						         $("#mainContainer").addClass("disableScroll");
						         $("#loginPanel").show();
						         $("#blackOverlay").show();								
							}
							
							else {
								$state.go('product',{productID: $scope.productID});
							}
						}
						
        			}

        		    }
    return product;

}])