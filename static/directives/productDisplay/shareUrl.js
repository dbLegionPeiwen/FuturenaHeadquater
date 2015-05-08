'use strict';

angular.module('directives')

	.config(['ngClipProvider', function(ngClipProvider) {
		ngClipProvider.setPath("bower_components/zeroclipboard/dist/ZeroClipboard.swf");
	}])

.directive('shareUrl',[function($compile){

	var shareUrl = {
		restrict: "E",
		templateUrl: "./directives/productDisplay/shareUrl.html",
		transclude: true,

		controller: function($scope,$compile,$stateParams,$rootScope){
			$("#pop-over-link").popover({
				'placement': 'top',
				'trigger': 'click',
				'html': true,
				'container': 'body',
				'content': function() {
					//var shareContent = 'http://localhost:8000/#/?user=' + $rootScope.currentUser
					//	+ '&productId='+ $stateParams.productID;

					var shareContent = 'http://localhost:8000/#/share/'
						+$stateParams.productID  +"&&"+ $rootScope.currentUser;

					$scope.copyContent = shareContent;

					return $compile($("#pop-over-content").html())($scope);
				}
			});

			$scope.checkUser = function() {
				if($rootScope.currentUser == null){
					$scope.popoutLogin();
					$("#pop-over-link").popover('hide');
				return;
				}
			}


		}
	}

	return shareUrl;
}

	])