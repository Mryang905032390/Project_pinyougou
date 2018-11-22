//控制层
app.controller('paySuccessController', function ($scope, $controller, $location) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.getMoney = function () {
        $scope.money = $location.search()["money"];
    }
});
