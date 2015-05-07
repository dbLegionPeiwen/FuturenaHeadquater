'use strict';

angular
		.module('controller')

		.controller(
				'distributeDisplay',
				[
						'$scope',
						'$http',
						'$stateParams',
						'$sce',
						function($scope, $http, $stateParams, $sce) {

							console.log("distributeDisplayPanel")

							var info = $stateParams.info.split("&&");
							var productID= info[0].trim();
							//var user = info[1].trim();
							//user info which not using right now


							$scope.imgurl = [];

							$scope.inFavourates = false;

							$http.get(
									"/a@a.com/" + productID
											+ "/check").success(function(_f) {
								if (_f)
									$scope.inFavourates = true;
								else
									$scope.inFavourates = false;
							});
						

							$scope.showProduct = null;

							$http
									.get("/product/" + productID)
									.success(
											function(_p) {

												if (_p) {
													$scope.showProduct = _p;
									
													var tempUrl = "https://www.youtube.com/embed/"
															+ _p.videoURL;
													$scope.videoUrl = $sce
															.trustAsResourceUrl(tempUrl);
										            $scope.currentImageUrl = "/UploadImages/"+$scope.showProduct.productID+"0.jpeg";

									
										            for (var i=1; i<_p.imageNumber;i++){
										            	$scope.imgurl.push('/UploadImages/'+$scope.showProduct.productID+i+'.jpeg')
										            }

												}
											});


							$scope.addToCart = function() {
								var url = "/shoppingCart/123@123.com/"
										+ productID + "/addToCart";
								var data = new Date();
								var cartProduct = {
									productCartID : null,
									productID : null,
									productName : $scope.showProduct.productName,
									buyerEmail : "123@123.com",
									createDate : data,
									quantity : 10,
									processState : false
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
									productID : productID,
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
										"/a@a.com/" + productID
												+ "/deleteFavourate").success(
										function(_f) {
											if (_f.status == 200)
												$scope.inFavourates = false;
										})
							}

							$scope.stateJudgement= function(){
								if ($scope.showProduct.productState == "pending") 
									return false
								else 
									return true;

							}

						} ])