//控制层
app.controller('searchController', function ($scope, $controller, searchService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.searchMap = {
        keywords:"",
        category:"",
        brand:"",
        spec:{},
        price:"",
        sortField:"",
        sort:"ASC",
        pageNo:1,
        pageSize:60
    };
    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        })
    };

    //封装条件查询
    $scope.addFilterCondition = function (key, value) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();
    }

    $scope.removeSearchItem = function (key) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] ="";
        } else {
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }
});	
