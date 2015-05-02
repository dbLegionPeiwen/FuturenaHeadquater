'use strict';

angular
		.module('controller')

		.controller(
				'productDisplay',
				[
						'$scope',
						'$http',
						'$stateParams',
						'$sce',
						function($scope, $http, $stateParams, $sce) {

							console.log("productDisplayPanel");
							
							$(function () {
								  $('[data-toggle="popover"]').popover({
									  placement: "top",
									  html: true,
									  content: 'URL: <button class="btn btn-warning">Copy</button>'
								  })
								})

							$scope.inFavourates = false;

							$http.get(
									"/a@a.com/" + $stateParams.productID
											+ "/check").success(function(_f) {
								if (_f)
									$scope.inFavourates = true;
								else
									$scope.inFavourates = false;
							});
						

							$scope.showProduct = null;

							$http
									.get("/product/" + $stateParams.productID)
									.success(
											function(_p) {

												if (_p) {
													$scope.showProduct = _p;
													console.log($scope.showProduct);
													var tempUrl = "https://www.youtube.com/embed/"
															+ _p.videoURL;
													$scope.videoUrl = $sce
															.trustAsResourceUrl(tempUrl);
										            $scope.currentImageUrl = "/UploadImages/"+$scope.showProduct.productID+"0.jpeg";
										            $scope.imgurl1 = '/UploadImages/'+$scope.showProduct.productID+'1.jpeg';
										            $scope.imgurl2 = '/UploadImages/'+$scope.showProduct.productID+'2.jpeg';
										            $scope.imgurl3 = '/UploadImages/'+$scope.showProduct.productID+'3.jpeg';
										            $scope.imgurl4 = '/UploadImages/'+$scope.showProduct.productID+'4.jpeg';
												}
											});


							$scope.addToCart = function() {
								var url = "/shoppingCart/123@123.com/"
										+ $stateParams.productID + "/addToCart";
								var data = new Date();
								var cartProduct = {
									productCartID : null,
									productID : null,
									productName : $scope.showProduct.productName,
									buyerEmail : "123@123.com",
									createDate : data,
									quantity : 10,
									processState : false,
								}

								console.log(cartProduct);

								$http
										.post(url, cartProduct)
										.success(
												function(_d) {
													alert($scope.showProduct.productName
															+ "is successfully added to cart!");
												})
							}

							$scope.addFavourate = function() {

								var favourate = {

									email : "a@a.com",
									productID : $stateParams.productID,
									productName : $scope.showProduct.productName,
									viewedAt : new Date()
								}

								$http.post("/a@a.com/addFavourate", favourate)
										.success(function(_f) {
											if (_f.status == 200)
												$scope.inFavourates = true;
										})
							}

							$scope.removeFavourate = function() {

								$http.get(
										"/a@a.com/" + $stateParams.productID
												+ "/deleteFavourate").success(
										function(_f) {
											if (_f.status == 200)
												$scope.inFavourates = false;
										})
							}

						} ])