app.service("brandService", function ($http) {

            this.findPage = function (currentPage,itemsPerPage,searchEntity) {
                return $http.post("../brand/findPage.do?pageNum=" + currentPage
                    + "&pageSize=" + itemsPerPage,searchEntity);
            }

            this.add = function (entity) {
                return $http.post("../brand/add.do", entity);
            }

            this.update = function (entity) {
                return $http.post("../brand/update.do", entity);
            }

            this.findOne = function (id) {
                return $http.get("../brand/findOne.do?id=" + id);
            }

            this.dele = function (ids) {
                return $http.get('../brand/delete.do?ids=' + ids);
            }

            this.selectBrandList=function () {
                return $http.get("../brand/selectBrandList.do");
            }
        });