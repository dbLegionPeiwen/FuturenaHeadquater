/**
* @author: Hao Qiao
*/

'use strict';

angular.module('controller',[])

.controller('home',function($scope, $http, $rootScope, $state){
    var scope = $scope;
    console.log("home page");
    $scope.loginSuccess = false;
    if ($rootScope.currentUser != null){   	
    	$scope.loginSuccess = true;
    	console.log($rootScope.currentUser);
    }
    $scope.showState = "released";
    $scope.filterShow = false;
    $scope.view = "highTech";
    $scope.timeScale = 1;
    $scope.nameShow = false;
    $scope.a1 = "selected";
    $scope.navShow = false;
    $scope.filterName = "CATEGORY";

    var url = "/product/getByState/released"
    $http.get(url).success(function(_d){
        $scope.productsFactory = _d;
    });

    $(window).ready(function(){
       $("#loginPanel").hide();
       $("#blackOverlay").hide();
       $(".productBoard").width(1170);
    });

    $(window).resize(function(){
        if ($(window).width() < 1170)
            $(".productBoard").css("width",780);
            else
            $(".productBoard").css("width",1170);
        if ($(window).width() < 780)
            {
             $(".productBoard").css("width",400);
             $(".block").addClass(".mobileBlock");
             }
    })

    $("#slideHide").click(function(){
    	
        $(".overlay").fadeOut(function(){
            $("#home .productBoard").css("margin-top",80);
            $("#titleSection").slideUp();
            $("#home .productBoard").css("position","static");
            setTimeout(function(){$(".webName").fadeIn();},850);
        });
    });

    $(".webName").click(function(){
        $(".headSection").css("overflow","hidden");
        $("#titleSection").slideDown();
        $("#home .productBoard").css("margin-top",1000);
        $("#home .productBoard").css("position","fixed");
        setTimeout(function(){
            $(".webName").fadeOut();
            $(".overlay").fadeIn();
        },850);
    });

    $("#display").click(function(){
        $(".headSection").css("overflow","visible");
    });

    $("#home .productNavigator .link").click(function(){
        if($(this).hasClass('selected') === false){
            $("#home .productNavigator .selected").removeClass('selected');
            $(this).addClass('selected');
        }
    })


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
})


.controller('productPreview',['$scope','$http', '$stateParams','$sce', '$state', '$rootScope',function($scope, $http, $stateParams, $sce, $state, $rootScope){

       console.log("productPreviewPanel");
       
       $http.get("/product/"+$stateParams.id).success(function(_p){
           if (_p){
             $scope.previewProduct = _p;
             var tempUrl = "https://www.youtube.com/embed/"+_p.videoURL;
             $scope.videoUrl = $sce.trustAsResourceUrl(tempUrl);
             
             $scope.currentImageUrl = '/UploadImages/'+$scope.previewProduct.productID+'0.jpeg';
	         $scope.imgurl1 = '/UploadImages/'+$scope.previewProduct.productID+'1.jpeg';
	         $scope.imgurl2 = '/UploadImages/'+$scope.previewProduct.productID+'2.jpeg';
	         $scope.imgurl3 = '/UploadImages/'+$scope.previewProduct.productID+'3.jpeg';
	         $scope.imgurl4 = '/UploadImages/'+$scope.previewProduct.productID+'4.jpeg';
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




.controller('adminConsole',['$scope','$http', '$rootScope','$state', function($scope, $http, $rootScope, $state){

       console.log("adminConsole");

       $scope.adminState = $rootScope.adminAccess;
       $scope.viewState = "admin";
       $scope.productFactory = null;
       $scope.a1 = "selected";

       if ($rootScope.adminAccess){
       		var url = "/product/getByState/pending";
            $http.get(url).success(function(_d){
                 $scope.productFactory = _d;
            })
       }

       $scope.navProduct = function(_state) {
            console.log(_state);

            var url= "/product/getByState/"+_state;
            $http.get(url).success(function(_d){
                 $scope.productFactory = _d;
            })
       }

       $scope.showProducts = function(){
            return !$state.includes('admin.search');
       }

}])

.controller('productAdmin',['$scope','$http', '$rootScope','$stateParams','$state',function($scope, $http, $rootScope, $stateParams, $state){

       var url = "/product/"+$stateParams.id;

       $http.get(url).success(function(_p){
           if (_p){
              $scope.previewProduct = _p;
              var url = "/product/"+_p.productID+"/getRate";
              $http.get(url).success(function(_r){
                console.log(_r);
                $scope.productRate = _r;
              });
           }
       })

       $scope.submitProduct = function(){

           var url = "product/fullyUpdate/"+$stateParams.id

           $http.post(url,$scope.previewProduct).success(function(_d){
               console.log($scope.productRate);
               var url = "/product/"+$scope.productRate.productID+"/updateRate"
               $http.post(url, $scope.productRate).success(function(_r){
                    console.log(_r);
               });
               $state.go('adminProductPreview',{id: _d.productID});
           })


       }

       $scope.deleteProduct = function() {
            var temp = confirm('Are you absolutely sure you want to delete?');

            if (temp) {
                var Id = {
                    id: $stateParams.id
                }

                $http.post('/product/deleteById',Id).success(function(_m){
                    if (_m.status == "200") {
                        alert("Successful delete the product!");
                        $state.go('admin');
                    }

                    else alert("internal error, please try again");

                });

                console.log("deleted");
            }
            else console.log("canceled");
       }
}])

.controller('adminProductPreview',['$scope','$http', '$stateParams','$sce', '$state', '$rootScope',function($scope, $http, $stateParams, $sce, $state, $rootScope){

       console.log("adminProductPreviewPanel");
       $scope.currentImageUrl = "/images/car1.jpeg";
       var url = "/product/"+$stateParams.id;

       $http.get(url).success(function(_p){
           if (_p){
             $scope.previewProduct = _p;
             var tempUrl = "https://www.youtube.com/embed/"+_p.videoURL;
             $scope.videoUrl = $sce.trustAsResourceUrl(tempUrl);
           }
       });

       $scope.submit= function(){
            $state.go('admin')
       }

       $scope.back = function(){
            $rootScope.previewProductID = $scope.previewProduct.productID;
            $state.go('productAdmin',{id: $stateParams.id});
       }

}])


.controller('adminSearch',['$scope','$http', function($scope, $http){

       $scope.searchView = "product";
       $scope.searchResult = null;

       $scope.searchProduct = function(){

            var url="product/get/"+$scope.productName;
            $http.get(url).success(function(_p){
                console.log(_p);
                if (_p.length>0)
                    $scope.searchResult = _p;
                else {
                    $scope.searchResult = null;
                    alert("Product not exists");
                };
            });
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

.controller('submissionWanted',function($scope,$http){
    var wantedBuffer = null;

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
