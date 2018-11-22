//控制层
app.controller('payController', function ($scope, $controller, payService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.createNative = function () {
        payService.createNative().success(function (response) {
            $scope.out_trade_no = response.out_trade_no;
            $scope.total_fee = (response.total_fee / 100).toFixed(2);

            var qr = window.qr = new QRious({
                element: document.getElementById('qrious'),
                size: 300,
                value: response.code_url,
                level: 'H'
            });
            $scope.queryPayStatus();
        })
    };

    $scope.queryPayStatus = function () {
        payService.queryPayStatus($scope.out_trade_no).success(function (response) {
            if (response.success) {
                location.href = "paysuccess.html#?money="+$scope.total_fee;
            } else {
                if (response.message == 'timeout') {
                    $scope.createNative();
                } else {
                    location.href = "payfail.html";
                }
            }
        })
    }
});
