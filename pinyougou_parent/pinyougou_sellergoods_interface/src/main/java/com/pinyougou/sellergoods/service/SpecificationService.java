package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;

public interface SpecificationService {

    //条件分页查询
    public PageResult search(Integer pageNum, Integer pageSize, TbSpecification tbSpecification);
}
