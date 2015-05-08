/**
 * @author: Hao Qiao
 */

'use strict';

angular
		.module('mainApp',
				[ 'ui.router', 'controller', 'directives', 'ngCookies' ])

		.run(
				function($http, $rootScope, $cookieStore) {
					$rootScope.tempProduct;
					$rootScope.adminAccess = false;
					$rootScope.currentUser = null;

					if ($cookieStore.get('accessToken') != null) {
						$http.defaults.headers.common['Authentication-Header'] = $cookieStore
								.get('accessToken');
						$http.get('/user/' + $cookieStore.get('userId'))
								.success(function(message) {
									if (message.user_id != null){
										$rootScope.currentUser = message.email;
									}
								})
					}
					

		})

		.config(function($stateProvider, $urlRouterProvider) {
			
			$urlRouterProvider.otherwise('/home');

			$stateProvider.state('home', {
				url : '/home',
				templateUrl : 'partials/home.html',
				controller : 'home'
			});

			$stateProvider.state('productPreview', {
				url : '/productPreview/:id',
				templateUrl : 'partials/productPreview.html',
				controller : 'productPreview'
			});

			$stateProvider.state('adminProductPreview', {
				url : '/adminProductPreview/:id',
				templateUrl : 'partials/adminProductPreview.html',
				controller : 'adminProductPreview'
			});

			$stateProvider.state('product', {
				url : '/product/:productID',
				templateUrl : 'partials/productDisplayPanel.html',
				controller : 'productDisplay'
			});
			
			$stateProvider.state('admin', {
				url : '/admin',
				templateUrl : 'partials/adminConsole.html',
				controller : 'adminConsole'
			});

			$stateProvider.state('admin.search', {
				url : '/search',
				templateUrl : 'partials/adminSearch.html',
				controller : 'adminSearch'
			});

			$stateProvider.state('submission', {
				url : '/submissionPage',
				templateUrl : 'partials/submissionPage.html',
				controller : 'submission'
			});

			$stateProvider.state('user', {
				url : '/userPage/{email}',
				templateUrl : 'partials/userPage.html',
				controller : 'user'
			});

			$stateProvider.state('productAdmin', {
				url : '/productAdmin/{id}',
				templateUrl : 'partials/productAdminPage.html',
				controller : 'productAdmin'
			});

			$stateProvider.state('shoppingCart', {
				url : '/shoppingCart',
				templateUrl : '/partials/shoppingCart.html',
				controller : 'shoppingCart'
			});

			$stateProvider.state('checkout', {
				url : '/checkout',
				templateUrl : '/partials/checkout.html',
				controller : 'checkout'
			});
			
			$stateProvider.state('successSubmission', {
				url : '/successSubmission?scope&code',
				templateUrl : '/partials/successSubmission.html',
				controller : 'successSubmission'
			});
			
		    $stateProvider.state('otherUserPage',{
		            url: '/otherUserPage',
		            templateUrl: '/partials/otherUserPage.html',
		            controller: 'otherUserPage'
		    });
		    
		    $stateProvider.state('submissionWanted',{
		            url: '/submissionWanted',
		            templateUrl: '/partials/submissionWanted.html',
		            controller: 'submissionWanted'
		    });
		    
		    $stateProvider.state('payment',{
	            url: '/payment',
	            templateUrl: '/partials/paymentest.html'
	    });

			$stateProvider.state('distribute', {
				//http://localhost:8000/?user=peiwen@dbroslegion.com&productId=79f1b94e-ed5f-4d63-a4dc-9f5a0677aeb9
			url : '/share/:info',
			templateUrl : 'partials/distributeDisplayPanel.html',
			controller : 'distributeDisplay'
		});

		})