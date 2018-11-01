package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;
    //条件分页查询
    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize,@RequestBody TbSpecification tbSpecification){
        return specificationService.search(pageNum,pageSize,tbSpecification);
    }
}
