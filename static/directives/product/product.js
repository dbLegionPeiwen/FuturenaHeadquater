'use strict';

angular.module('directives')

.directive('product',[function(){

    var product = {
        			restrict: "E",
        			templateUrl: "./directives/product/product.html",
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