package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface TbBrandService {

    public List<TbBrand> fandAll();

    public PageResult selectByExample(Integer pageNum, Integer pageSize);

    void add(TbBrand brand);

    TbBrand findOne(Long id);

    void update(TbBrand brand);

    public void delete(Long[] ids);

    List<Map> selectBrandList();
}
