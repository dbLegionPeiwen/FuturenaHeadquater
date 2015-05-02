'use strict';

angular.module('directives')

.directive('userproduct',[function(){

    var product = {
        			restrict: "E",
        			templateUrl: "./directives/UserProduct/UserProduct.html",
        			transclude: true,

        			controller: function($scope, $http, $element, $rootScope, $state, $cookieStore){
  			      				
						$scope.productInitial = function(name,id){
							$scope.productName = name;
							$scope.productID = id;
							$scope.imageUrl = "/UploadImages/"+id+"0.jpeg";
						};
						
        			}

        		    }
    return product;

}])