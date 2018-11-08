//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, itemCatService, typeTemplateService, uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
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

    //保存
    $scope.add = function () {
        //获取商品编辑器中的html内容，复制给goodsDesc.introduction
        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.entity = {};//重新加载
                    editor.html("");//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        })
    }

    $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
            $scope.itemCat3List = [];
        })
    })

    $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        })
    })

    $scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.goods.typeTemplateId = response.typeId;
        })
    })

    $scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.brandList = JSON.parse(response.brandIds);
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
        });
        //模板关联的规格列表数据;
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.specList = response;
        })

    })
    $scope.image_entity = {};
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {
                $scope.image_entity.url = JSON.parse(response.message).url;
                alert(JSON.parse(response.message).message);
            } else {
                alert(response.message);
            }
        })

    }

    $scope.entity = {goods: {isEnableSpec:"1"}, goodsDesc: {itemImages: [], specificationItems: []}, itemList: []};
    $scope.addImageEntity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //删除图片列表中的对象
    $scope.deleImageEntity = function (index) {
        alert(index);
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }
    //规格选项勾选和取消勾选组装规格结果集功能
    $scope.updateSpecAttribute = function (optionName, $event, text) {
        var specObject = $scope.getObjectByName($scope.entity.goodsDesc.specificationItems, "attributeName", text)
        if (specObject != null) {
            if ($event.target.checked) {
                specObject.attributeValue.push(optionName);
            } else {
                var index = specObject.attributeValue.indexOf(optionName);
                specObject.attributeValue.splice(index, 1);
                if (specObject.attributeValue.length == 0) {
                    var index1 = $scope.entity.goodsDesc.specificationItems.indexOf(specObject);
                    $scope.entity.goodsDesc.specificationItems.splice(index1, 1);
                }
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": text, "attributeValue": [optionName]});
        }
    }

    //组装sku列表数据
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: "1", isDefault: "0"}];

        var specList = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < specList.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, specList[i].attributeName, specList[i].attributeValue);
        }
        if(specList.length==0){
            $scope.entity.itemList=[];
        }
    }
    //构建sku列表行列数据
    addColumn = function (list, specName, specValue) {
        var newList=[];
        for (var i = 0; i < list.length; i++) {
            var olditem = list[i];
            for (var j = 0; j < specValue.length; j++) {
                var newitem= JSON.parse(JSON.stringify(olditem));
                newitem.spec[specName] = specValue[j];
                newList.push(newitem);
            }
        }
        return newList;
    }

    $scope.status=['未审核','已审核','审核通过','关闭'];
    $scope.isMarketables=['下架','上架'];
    $scope.updateIsMarketable=function (isMarketable) {
        goodsService.updateIsMarketable($scope.selectIds,isMarketable).success(function (response) {
            if(response.success){
                $scope.reloadList();
                $scope.selectIds = [];
            }else{
                alert(response.message)
            }
        })
    }

});
