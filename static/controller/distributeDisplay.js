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
						'$rootScope',
						'$state',
						function($scope, $http, $stateParams, $sce,$rootScope,$state) {

							console.log("distributeDisplayPanel")
							$("#loginPanel").hide();
							$("#blackOverlay").hide();

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

								if($rootScope.currentUser==null)
								{
									$scope.popoutLogin();
									return;
								}

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

								if($rootScope.currentUser==null)
								{
									$scope.popoutLogin();
									return;
								}

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

								if($rootScope.currentUser==null)
								{
									$scope.popoutLogin();
									return;
								}

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

							$scope.popoutLogin = function()
							{
								$(".userSignInUp").css("top",$(window).scrollTop()+150);
								$("#blackOverlay").css("top",$(window).scrollTop());
								$("#mainContainer").addClass("disableScroll");
								$("#loginPanel").show();
								$("#blackOverlay").show();
							}

							$scope.homeCheckUser = function()
							{
								if($rootScope.currentUser==null)
								{
									$scope.popoutLogin();
									return;
								}

								$state.go("home");
							}

							$scope.userPageCheckUser = function()
							{
								if($rootScope.currentUser==null)
								{
									$scope.popoutLogin();
									return;
								}

								$state.go("user({email: currentUser})");

							}

						} ])