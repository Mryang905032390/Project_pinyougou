//控制层
app.controller('orderController', function ($scope, $controller, addressService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.findAddressByUserId=function () {
        addressService.findAddressByUserId().success(function (response) {
            $scope.addressList=response;
            for(var i = 0; i< $scope.addressList.length;i++){
                if ($scope.addressList[i].isDefault=='1'){
                    $scope.address=$scope.addressList[i];
                    break;
                }
            }
            if($scope.address==null){
                $scope.address=$scope.addressList[0];
            }
        })
    }

    //读取列表数据绑定到表单中
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                $scope.cartList = response;
                sum();
            }
        );
    }

    sum = function () {
        $scope.totalNum = 0;
        $scope.totalMoney = 0.00;
        for (var i = 0; i < $scope.cartList.length; i++) {
            var cart = $scope.cartList[i];
            var orderItemList = cart.orderItemList;
            for (var j = 0; j < orderItemList.length; j++) {
                $scope.totalNum+=orderItemList[j].num;
                $scope.totalMoney+=orderItemList[j].totalFee;
            }
        }
    }

    $scope.address=null;
    $scope.isSelect=function(addr){
        if($scope.address==addr){
            return true;
        }
        else{
            return false;
        }
    }
});
