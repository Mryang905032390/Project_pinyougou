app.service("specificationService", function ($http) {

    this.add = function (entity) {
        return $http.post("../specification/add.do", entity);
    }

    this.update = function (entity) {
        return $http.post("../specification/update.do", entity);
    }

    this.findOne = function (id) {
        return $http.get("../specification/findOne.do?id=" + id);
    }

    this.dele = function (ids) {
        return $http.get('../specification/delete.do?ids=' + ids);
    }
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post("../specification/search.do?pageNum=" + pageNum + "&pageSize=" + pageSize, searchEntity);
    }

    this.selectSpecList=function(){
        return $http.get("../specification/selectSpecList.do");
    }
});