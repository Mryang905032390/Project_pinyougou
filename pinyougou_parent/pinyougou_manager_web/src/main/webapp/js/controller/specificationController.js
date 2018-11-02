app.controller("specificationController", function ($scope, specificationService, $controller) {

    $controller("baseController", {$scope: $scope});
    //条件分页查询
    $scope.searchEntity = {};
    $scope.search = function (pageNum, pageSize) {
        specificationService.search(pageNum, pageSize, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        })
    };

    $scope.save = function () {
        var method = null;
        if ($scope.entity.tbSpecification.id != null) {
            method = specificationService.update($scope.entity);
        } else {
            method = specificationService.add($scope.entity);
        }

        method.success(function (response) {
            if (response.success) {
                $scope.reloadList();
                alert(response.message)
            } else {
                alert(response.message)
            }
        })
    };

    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };

    $scope.dele = function () {
        if (confirm("确定要进行删除吗？")) {
            specificationService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                    }
                })
        }
    };
    //添加规格选项行
    $scope.entity={tbSpecification:{},specificationOptions:[]};

    $scope.addRow = function () {
        $scope.entity.specificationOptions.push({});
    };
    //删除规格选项行
    $scope.deleRow = function (index) {
        $scope.entity.specificationOptions.splice(index, 1);
    }
});