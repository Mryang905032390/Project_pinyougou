package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface TbBrandService {

    public List<TbBrand> fandAll();

    public PageResult findPage(Integer pageNum,Integer pageSize);

    void add(TbBrand brand);

    TbBrand findOne(Long id);

    void update(TbBrand brand);

    public void delete(Long[] ids);
}
