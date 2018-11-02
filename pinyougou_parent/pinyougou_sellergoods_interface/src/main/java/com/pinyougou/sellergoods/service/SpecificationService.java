package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import entity.PageResult;
import groupEntity.Specification;

public interface SpecificationService {

    //条件分页查询
    public PageResult search(Integer pageNum, Integer pageSize, TbSpecification tbSpecification);

    void add(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);
}
