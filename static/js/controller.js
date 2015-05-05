/**
* @author: Hao Qiao
*/

'use strict';

angular.module('controller',[])

.controller('home',function($scope, $http, $rootScope, $state, $cookieStore){
    
    console.log("home page");
    console.log($rootScope.currentUser);

    var scope = $scope;
    $scope.showState = "released";
    $scope.a1 = "selected";

    $http.get("/product/getByState/released").success(function(_d){
        $scope.productsFactory = _d;
    });

    $(window).ready(function(){
       $("#loginPanel").hide();
       $("#blackOverlay").hide();
       $(".productBoard").width(1100);
       $(".sideBar").hide();
       $("#productBoard").hide();
    });

    if ($rootScope.currentUser != null) {
        $(".overlay").hide();
        $("#titleSection").hide();
        $(".sideBar").show();
        $("#productBoard").show();

    }

    $("#slideHide").click(function(){
    	
        $(".overlay").fadeOut(function(){
            $("#titleSection").slideUp();
        });

        $(".sideBar").fadeIn(1500).show();
        $("#productBoard").fadeIn(1500).show();

    });

    $(function(){
        $('.imageMain img:gt(0)').hide();
        var $currentImage = $('.imageMain :first-child');
        var $overlayContent = $('#firstContent');
        $("#previous").click(function(){
            $currentImage.fadeOut();
            $overlayContent.css("display","none");
            if($currentImage.prev().is('img') === false){
                $currentImage = $('.imageMain :last-child');
                $overlayContent = $('#thirdContent');

            }
            else {
                $currentImage = $currentImage.prev();
                $overlayContent = $overlayContent.prev();
            }
            $currentImage.fadeIn(2000);
            $overlayContent.fadeIn(2000);
        });

        $("#next").click(function(){
            $currentImage.fadeOut();
            $overlayContent.css("display","none");
            if($currentImage.next().is('img') === false){
                $currentImage = $('.imageMain :first-child');
                $overlayContent = $('#firstContent');
            }
            else {
                $currentImage = $currentImage.next();
                $overlayContent = $overlayContent.next();
            }
            $currentImage.fadeIn(2000);
            $overlayContent.fadeIn(2000);
        });
    });

    // setInterval(function(){
    //     $currentImage.fadeOut(1000);
    //     if($currentImage.next().is('img') === false){
    //         $currentImage = $('.imageMain :first-child');
    //         $currentImage.fadeIn(3000);
    //     }
    //     else{
    //         $currentImage.next().fadeIn(3000);
    //         $currentImage = $currentImage.next();
    //     }
    //     }, 3000)

    $scope.showLoginPanel = function(){
         $(".userSignInUp").css("top",$(window).scrollTop()+150);
         $("#blackOverlay").css("top",$(window).scrollTop());
         $("#mainContainer").addClass("disableScroll");
         $("#loginPanel").show();
         $("#blackOverlay").show();
    }

    $scope.changeProductState= function(state){
         var url = "/product/getByState/"+state;
         $http.get(url).success(function(_d){
             $scope.productsFactory = _d;
         });
    }

    $scope.logout = function(){
        if ($rootScope.currentUser != null){
          $http.get("/user/"+$rootScope.currentUser+"/logout").success(function(_d){
              console.log(_d);
              $cookieStore.remove("user");
              $cookieStore.remove("accessToken");
              $cookieStore.remove("userId");
              $rootScope.currentUser = null;
              $state.go('home');
          });
        }
    }
    
    $scope.goSubmission = function(){
    	
    	if ($rootScope.currentUser != null)
    		$state.go('submission');
    	else {
	         $(".userSignInUp").css("top",$(window).scrollTop()+150);
	         $("#blackOverlay").css("top",$(window).scrollTop());
	         $("#mainContainer").addClass("disableScroll");
	         $("#loginPanel").show();
	         $("#blackOverlay").show();	
    	}
    }

    $scope.goWanted = function(){
      
      if ($rootScope.currentUser != null)
        $state.go('submissionWanted');
      else {
           $(".userSignInUp").css("top",$(window).scrollTop()+150);
           $("#blackOverlay").css("top",$(window).scrollTop());
           $("#mainContainer").addClass("disableScroll");
           $("#loginPanel").show();
           $("#blackOverlay").show(); 
      }
    }
})


.controller('productPreview',['$scope','$http', '$stateParams','$sce', '$state', '$rootScope',function($scope, $http, $stateParams, $sce, $state, $rootScope){

       console.log("productPreviewPanel");
       $scope.imgurl = [];
       
       $http.get("/product/"+$stateParams.id).success(function(_p){
           if (_p){
             $scope.previewProduct = _p;
             var tempUrl = "https://www.youtube.com/embed/"+_p.videoURL;
             $scope.videoUrl = $sce.trustAsResourceUrl(tempUrl);
             
             $scope.currentImageUrl = '/UploadImages/'+$scope.previewProduct.productID+'0.jpeg';
             
             for (var i=1; i<_p.imageNumber;i++){
                $scope.imgurl.push('/UploadImages/'+$scope.previewProduct.productID+i+'.jpeg')
             }
          }
       });

       $scope.submit= function(){
            $rootScope.previewProductID = null;
            $state.go('user');
       }

       $scope.back = function(){
            $rootScope.previewProductID = $scope.previewProduct.productID;
            $state.go('submission');
       }

}])

.controller("shoppingCart",function($scope,$http){
  $scope.productName = "Ferrari";
  $scope.productNumber = 1;
  $scope.price = 300000;
  $scope.decreaseNumber = function() {
    $scope.productNumber -= 1;
    if($scope.productNumber===0){
      document.getElementById("hiddenDelete").click();
    }
  }
  $scope.increaseNumber = function() {
      $scope.productNumber += 1;
  }

  $("#deleteX").click(function(){
    $("#hiddenDelete").click();
  })
})

.controller("checkout",function($scope,$http){
  $scope.productName = "Ferrari";
  $scope.productNumber = 1;
  $scope.price = 300000;
  $scope.shippingFee = 10;
})

.controller("otherUserPage",function($scope,$http){

    $scope.targetName = "Eminem";


    $(".userPageTabs").click(function(){
        if($(this).attr("id") === "wishListTab") {
            $("#wishListContent").show();
            $("#distributionContent").hide();
            $("#soldContent").hide();
        }
        else if($(this).attr("id") === "distributionTab") {
            $("#wishListContent").hide();
            $("#distributionContent").show();
            $("#soldContent").hide();        
        }
        else if($(this).attr("id") === "soldTab") {
            $("#wishListContent").hide();
            $("#distributionContent").hide();
            $("#soldContent").show();          
        }
        console.log($(this).attr("id"));     
    })
})

.controller('submissionWanted',function($scope,$http, $rootScope, $state){
    var wantedBuffer = null;

    if ($rootScope.currentUser == null) $state.go("home");

    $("#addWantedImage").click(function(){
        $("#uploadWanted").click();
    })

    $("#uploadWanted").change(function(){
        uploadingWanted();
    })

    $("#wantedimage").click(function(){
        wantedBuffer = null;
        $("#addWantedImage").css("display","inline");
        $("#wantedimage").css("display","none");
        $("#uploadWanted").val('');   
    })

    var uploadingWanted = function(){
        var filesUp = document.getElementById("uploadWanted");
        var imageReader = new FileReader();
        imageReader.onload = function(e){
            wantedBuffer = e.target.result;
            $("#wantedimage").attr("src",e.target.result);
            $("#wantedimage").css("display","inline");
            $("#addWantedImage").css("display","none");
        }
            imageReader.readAsDataURL(filesUp.files[0]);
    }

})
