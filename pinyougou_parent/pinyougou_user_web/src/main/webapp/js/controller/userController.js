//控制层
app.controller('userController', function ($scope, $controller, userService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        userService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        userService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        userService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = userService.update($scope.entity); //修改
        } else {
            serviceObject = userService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        userService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        userService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //用户注册
    $scope.reg = function () {
        if ($scope.repassword != $scope.entity.password) {
            alert("两次输入的密码不一致，请重新输入");
            return;
        }
        userService.add($scope.entity, $scope.smsCode).success(
            function (response) {
                if (response.success) {
                    location.href = "login.html"
                } else {
                    alert(response.message);
                }
            }
        );
    }


    $scope.sendSmsCode = function () {
        userService.sendSmsCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        })
    }
    $scope.entity={phone:""};

    $scope.isDisabled=true;
    $scope.phoneVal=function(){
        return $scope.entity.phone;
    };
    $scope.$watch($scope.phoneVal,function(newValue,oldValue){
        var regex = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
        if(regex.test(newValue)){
            $scope.isDisabled=false;
        }else{
            $scope.isDisabled=true;
        }
    });

    $scope.isSubmitted=true;
    $scope.smsCode="";
    $scope.codeVal=function(){
        return $scope.smsCode;
    };
    $scope.$watch($scope.codeVal,function(newValue,oldValue){
        if(newValue.length==6){
            $scope.isSubmitted=false;
        }else{
            $scope.isSubmitted=true;
        }
    });
});	
