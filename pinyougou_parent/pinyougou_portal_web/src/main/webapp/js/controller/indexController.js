//控制层
app.controller('indexController', function ($scope, $controller, contentService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList=response;
        })
    }
});	
