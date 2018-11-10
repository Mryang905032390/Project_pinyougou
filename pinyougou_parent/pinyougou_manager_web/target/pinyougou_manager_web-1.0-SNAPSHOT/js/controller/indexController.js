app.controller("indexController", function ($scope, $controller, loginService) {
    $controller("baseController", {$scope: $scope});
    //获取登录名
    $scope.getName=function () {
        loginService.getName().success(function (response) {
            $scope.loginName=response.loginName;
        })
    }
});