//控制层
app.controller('cartController', function ($scope, $controller, cartService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                $scope.cartList = response;
                sum();
            }
        );
    }
    $scope.addItemToCartList = function (itemId, num) {
        cartService.addItemToCartList(itemId, num).success(function (response) {
            if (response.success) {
                $scope.findCartList();
            } else {
                alert(response.message);
            }
        })
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
});
