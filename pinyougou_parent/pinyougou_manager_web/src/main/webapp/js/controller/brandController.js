app.controller("brandController", function ($scope, brandService,$controller) {
    $controller("baseController",{$scope:$scope});
    $scope.findPage = function (currentPage,itemsPerPage) {
        brandService.findPage(currentPage,itemsPerPage).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        })
    }

    $scope.entity = {};
    $scope.save = function () {
        var method = null;
        if ($scope.entity.id != null) {
            method = brandService.update($scope.entity);
        } else {
            method = brandService.add($scope.entity);
        }

        method.success(function (response) {
            if (response.success) {
                $scope.reloadList();
                alert(response.message)
            } else {
                alert(response.message)
            }
        })
    }

    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };

    $scope.dele = function () {
        if (confirm("确定要进行删除吗？")) {
            brandService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                    }
                })
        }
    }
});