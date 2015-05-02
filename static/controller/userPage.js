

'use strict';

angular.module('controller')
.controller('user', function($http, $scope, $stateParams, $rootScope, $cookieStore) {
	
		$scope.userName;
		$scope.productFactory = null;
	    $scope.userGender = "male";
	    $scope.ageRange;
	    $scope. shippingAddress;
	    $scope.emailAdd;
	    $scope.phoneNumber;
	    $scope.profileShow = false;
	    $scope.passwordTest;
	    $scope.newPassword;
	    $scope.confirmPassword;
	    $scope.userNameShow = "User1";
	    $scope.shippingCity;
	    $scope.shippingState;
	    $scope.shippingCountry;
	    $scope.postal;
	    $scope.trackingNumber = null;
	    $scope.haveTrackingNumber = false;
	    $scope.submissionShow =true;
	    $scope.distributionShow = false;
	    $scope.buyingHistoryShow = false;
	    $scope.sellingHistoryShow = false;
	    $scope.profileShow = false;
	    $scope.securityShow = false;
	    $scope.favouriteShow = false;
	    
	    console.log($cookieStore.user);
    	$http.get("/product/getByDesigner/"+$cookieStore.get("user")).success(function(_p){
    		$scope.productFactory = _p;
    		console.log(_p);
    	});

	    $scope.pageChangeSubmission = function() {
	    	
	    	$http.get("/product/getByDesigner/"+$cookieStore.get("user")).success(function(_p){
	    		$scope.productFactory = _p;
	    	});
	    	
	        $scope.submissionShow =true;
	        $scope.distributionShow = false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = false;
	        $scope.securityShow = false;
	        $scope.favouriteShow = false;
	    }

	    $scope.pageChangeDistribution = function() {
	        $scope.distributionShow = true;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = false;
	        $scope.securityShow = false;
	        $scope.favouriteShow = false;
	    }

	    $scope.pageChangeBuyingHistory = function() {
	        $scope.distributionShow = false;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = true;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = false;
	        $scope.securityShow = false;
	        $scope.favouriteShow = false;
	    }


	    $scope.pageChangeSellingHistory = function() {
	        $scope.distributionShow = false;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = true;
	        $scope.profileShow = false;
	        $scope.securityShow = false;
	        $scope.favouriteShow = false;
	    }

	    $scope.pageChangeProfile = function() {
	        $scope.distributionShow = false;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = true;
	        $scope.securityShow = false;
	        $scope.favouriteShow = false;
	    }

	    $scope.pageChangeSecurity = function() {
	        $scope.distributionShow = false;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = false;
	        $scope.securityShow = true;
	        $scope.favouriteShow = false;
	    }

	    $scope.pageChangeFavourite = function() {
	        $scope.distributionShow = false;
	        $scope.submissionShow =false;
	        $scope.buyingHistoryShow = false;
	        $scope.sellingHistoryShow = false;
	        $scope.profileShow = false;
	        $scope.securityShow = false;
	        $scope.favouriteShow = true;
	    }
	    
	    var photoBuffer = null;

	    $("#uploadPlus").click(function(){
	        $("#uploadPhoto").click();
	    })

	    $("#uploadPhoto").change(function(){
	        uploadingPhoto();
	    })

	    $("#photoPresent").click(function(){
	        $("#photoPresent").css("display","none");
	        photoBuffer = null;
	        $("#uploadPhoto").val('');
	        $("#uploadPlus").css("display","inline");        
	    })

	    var uploadingPhoto = function(){
	        var filesUp = document.getElementById("uploadPhoto");
	        var imageReader = new FileReader();
	        imageReader.onload = function(e){
	            photoBuffer = e.target.result;
	            $("#photoPresent").attr("src",e.target.result);
	            $("#photoPresent").css("display","inline");
	            $("#uploadPlus").css("display","none");
	        }
	            imageReader.readAsDataURL(filesUp.files[0]);
	    }
})