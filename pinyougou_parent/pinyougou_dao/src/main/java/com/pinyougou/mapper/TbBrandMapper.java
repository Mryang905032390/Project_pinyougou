package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface TbBrandMapper {
    public List<TbBrand> fandAll();

    public void insert(TbBrand brand);

    TbBrand selectByParmaryKey(Long id);

    void update(TbBrand brand);

    void delete(Long ids);
}
