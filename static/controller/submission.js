'use strict';

angular.module('controller')

.controller('submission',function($scope, $http, $state, $rootScope){
	
	$scope.unit = "Unit";
    $scope.productName = "";
    $scope.details = "";
    $scope.typeP = "designer";
    $scope.videoUrl;
    $scope.price ="";
    $scope.material;
    $scope.image;
    $scope.heightP;
    $scope.widthP;
    $scope.lengthP;
    $scope.imageBuffer;
    $scope.demonBuffer;
    var buffer = [];
    $scope.imageBinding;
    var count=1;
    $scope.imageNumber = 0;

    if ($rootScope.currentUser == null) $state.go("home");

    if ($rootScope.previewProductID != null){
       $http.get("product/"+$rootScope.previewProductID).success(function(_p){
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

    $("#uploadButton").change(function(){
        upLoading();
    })


    function upLoading() {
        var filesUp = document.getElementById("uploadButton");
        var text ="";
        var size = filesUp.files[0].size;

        if (size > 2000000) {
            console.log(size);
            alert("Image size can not be larger than 2MB");
            return;
        }
        
        if(filesUp.files.length <= 5 && filesUp.files.length > 0) {
            $scope.imagesFilled = true;
        }
        else if(filesUp.files.length > 5){
            $('#uploadButton').val('');
            console.log("Max 5 photos!");
            $scope.imagesFilled = false;
        }
        else if(filesUp.files.length === 0) {
            $scope.imagesFilled = false;
        }
        for(var i = 0; i < filesUp.files.length; i++){
            var imageReader = new FileReader();
            $scope.imagesFilled = true;
            imageReader.onload = function(e) {
                $('#uploadButton').val('');
                if(buffer.indexOf(e.target.result) === -1 || buffer.length === 0){
                    buffer.push(e.target.result);
                    
                    $scope.imageBuffer = buffer;
                    $scope.imageNumber = $scope.imageNumber + 1;
                }											
                else return;
                if(buffer.length < 5) {
                    for(var j = 0;j<buffer.length;j++){
                        $("#display"+j).attr("src",buffer[j]);
                        $("#display"+j).css("display","inline");
                    }
                }
                else {
                    for(var k = 0;k<5;k++){
                        $("#addImage").css("display","none");
                        $("#display"+k).attr("src",buffer[k]);
                        $("#display"+k).css("display","inline");
                    }
                    for(var l = buffer.length;l > 5;l--){
                        buffer.splice(l,1);
                    }
                }
            }
            imageReader.readAsDataURL(filesUp.files[i]);    
        }
    }

    $(".displayImage").click(function(){

        if($(this).attr("src")!=="images/1.jpg"){
           var bufferIndex =  buffer.indexOf($(this).attr("src"));
           buffer.splice(bufferIndex,1);
           $(this).attr("src","");
           $(this).css("display","none");
        }
        else{
            $("#uploadButton").click();
            $('#uploadButton').val('');
        }
    })
        
    $scope.submitProduct = function(){

        $scope.date = new Date();
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
                designer: $rootScope.currentUser,
                imageNumber: $scope.imageNumber
        }

        if (!$rootScope.previewProductID){

            $http.post("/product/add",product).success(function(_d){

            	$http.post('upload', {
            		buffer: $scope.imageBuffer,
            		productID: _d.productID
            		}).success(function(_i){        		
            	});
                $state.go('productPreview',{id: _d.productID});
             })
        } else {
            var url = "product/update/"+$rootScope.previewProductID
            $http.post(url,product).success(function(_d){
                $state.go('productPreview',{id: _d.productID});
            })
        }
    }

})