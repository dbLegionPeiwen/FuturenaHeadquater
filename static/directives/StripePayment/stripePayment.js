'use strict';

angular.module('directives')

.directive('stripe',function(){
	
	var stripe = {
			restrict: "E",
			templateUrl: "./directives/StripePayment/stripePayment.html",
			transclude: true,

			controller: function($scope, $http, $element){
				
				Stripe.setPublishableKey('pk_test_iSxpfIIs5pAHbIhIg5PFqMAe');
				
				$scope.pay= function(){				
					var $form = $('#payment-form');
					$form.find('button').prop('disabled', true);
				    Stripe.card.createToken($form, stripeResponseHandler);
				}

  
				function stripeResponseHandler(status, response) {
					var $form = $('#payment-form');
					if (response.error) {
						$form.find('.payment-errors').text(response.error.message);
						$form.find('button').prop('disabled', false);
					} else {						
						$http.post('/charge',{id: response.id}).success(function(message){						
							console.log(message);
						});
					}
				};							
			}
	}
	
	return stripe;
	
})