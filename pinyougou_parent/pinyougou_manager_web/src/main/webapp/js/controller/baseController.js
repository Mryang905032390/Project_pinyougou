app.controller("baseController",function ($scope) {

    //分页配置对象
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();
        }
    };

    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }

    //记录选中的id的数组
    $scope.selectIds = [];

    //复选框勾选和取消勾选
    $scope.updateSelection = function ($event, id) {
        //判断复选框勾选状态
        if ($event.target.checked) {
            //往数组中添加值调用js的push实现
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);
            //从数组中移除元素
            $scope.selectIds.splice(index, 1);
        }
    }
});