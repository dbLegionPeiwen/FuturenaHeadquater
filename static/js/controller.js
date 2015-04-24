/**
* @author: Hao Qiao
*/

'use strict';

angular.module('controller',[])

.run(function($rootScope){
    $rootScope.tempProduct;
    $rootScope.adminAccess = false;
})

.controller('home',['$scope','$http',function($scope,$http){
    var scope = $scope;
    console.log("home page");
    $scope.loginSuccess = false;
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

    /*$scope.changeTextHighTech = function(){
        document.getElementById("display").innerHTML = "High Tech";
    };

    $scope.changeTextFashion = function(){
        document.getElementById("display").innerHTML = "Living and Office";
    };

    $scope.changeTextArtisan = function(){
        document.getElementById("display").innerHTML = "Artisan";
    };*/

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

        /*setInterval(function(){
        $currentImage.fadeOut(1000);
        if($currentImage.next().is('img') === false){
            $currentImage = $('.imageMain :first-child');
            $currentImage.fadeIn(3000);
        }
        else{
            $currentImage.next().fadeIn(3000);
            $currentImage = $currentImage.next();
        }
        }, 
        3000);*/
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
}])


.controller('productPreview',['$scope','$http', '$stateParams','$sce', '$state', '$rootScope',function($scope, $http, $stateParams, $sce, $state, $rootScope){

       console.log("productPreviewPanel");
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
            $rootScope.previewProductID = null;
            $state.go('user');
       }

       $scope.back = function(){
            $rootScope.previewProductID = $scope.previewProduct.productID;
            $state.go('submission');
       }

}])


.controller('submission',function($scope, $http, $state, $rootScope){
    $scope.unit = "Unit";
    $scope.productName = "";
    $scope.details = "";
    $scope.typeP = "regular";
    $scope.videoUrl;
    $scope.price ="";
    $scope.material;
    $scope.image;
    $scope.heightP;
    $scope.widthP;
    $scope.lengthP;
    $scope.imageBuffer;
    $scope.demonBuffer;
//    $scope.productNameFilled = false;
//    $scope.detailFilled =false;
//    $scope.priceFilled = false;
//    $scope.imagesFilled = true;

    if ($rootScope.previewProductID != null){
        var url = "product/"+$rootScope.previewProductID
       $http.get(url).success(function(_p){
           $scope.productName = _p.productName;
           $scope.details = _p.description;
           $scope.videoUrl = _p.videoURL;
           $scope.price = _p.price;
           $scope.material = _p.material;
           $scope.heightP = _p.height;
           $scope.widthP = _p.width;
           $scope.lengthP = _p.length;
       })
    }



    $scope.demon = function() {
        var demonUp = document.getElementById("demonUpload");
        if(demonUp.files.length > 3){
            $('#demonUpload').val('');
            alert("Max 3 photos!");
        }
    };

    $scope.upLoading = function() {
        var filesUp = document.getElementById("fileToUpload");
        var text ="";
        var count=1;
        //alert(filesUp.files.length);
            /*else if(filesUp.files.length > 0 && filesUp.files.length<=5){
                for(var i = 0; i<filesUp.files.length; i++) {
                    $scope.imageBuffer.push(filesUp.files[i]);
                }
            }*/
        if(filesUp.files.length <= 5 && filesUp.files.length > 0) {
            $scope.imagesFilled = true;
        }
        else if(filesUp.files.length > 5){
            $('#fileToUpload').val('');
            alert("Max 5 photos!");
            $scope.imagesFilled = false;
        }
        else if(filesUp.files.length === 0) {
            $scope.imagesFilled = false;
        }
        for(var i = 0; i < filesUp.files.length; i++){
            var imageReader = new FileReader();
            imageReader.onload = function(e) {
                $("#display"+count).attr("src",e.target.result);
                count++;
            }
            imageReader.readAsDataURL(filesUp.files[i]);
        }
    }

    $scope.submitProduct = function(){

        $scope.date = new Date()
        var product = {
                productName: $scope.productName,
                description: $scope.details,
                productType: "Fashion",
                price: $scope.price,
                videoURL: $scope.videoUrl,
                length: $scope.lengthP,
                height: $scope.heightP,
                width:  $scope.widthP,
                productMaterial: $scope.material,
                createDate: $scope.date,
        }


        if (!$rootScope.previewProductID){

            $http.post("/product/add",product).success(function(_d){
                console.log(_d);
                $state.go('productPreview',{id: _d.productID});
             })
        } else {
            var url = "product/update/"+$rootScope.previewProductID
            $http.post(url,product).success(function(_d){
                console.log(_d);
                $state.go('productPreview',{id: _d.productID});
            })
        }


    }

})

.controller('user',function($scope,$http){



})

.controller('productDisplay',['$scope','$http','$stateParams','$sce',function($scope, $http, $stateParams,$sce){

       console.log("productDisplayPanel");
       $scope.currentImageUrl = "/images/car1.jpeg";
       $scope.showProduct = null;

       var url = "/product/"+$stateParams.productID;

       $http.get(url).success(function(_p){
           if (_p){
            $scope.showProduct = _p;
            var tempUrl = "https://www.youtube.com/embed/"+_p.videoURL;
            $scope.videoUrl = $sce.trustAsResourceUrl(tempUrl);
           }
       });

       $scope.addToCart= function(){
            var url = "/shoppingCart/123@123.com/"+$stateParams.productID+"/addToCart";
            var data = new Date();
            var cartProduct = {
                productCartID: null,
                productID: null,
                productName: $scope.showProduct.productName,
                buyerEmail: "123@123.com",
                createDate: data,
                quantity: 10,
                processState: false,
            }

            console.log(cartProduct);

            $http.post(url, cartProduct).success(function(_d){
                alert($scope.showProduct.productName+"is successfully added to cart!");
            })
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
