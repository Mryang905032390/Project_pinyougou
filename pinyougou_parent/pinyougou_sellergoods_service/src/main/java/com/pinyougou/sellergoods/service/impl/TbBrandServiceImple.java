package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.TbBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class TbBrandServiceImple implements TbBrandService {
    @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> fandAll() {
        return brandMapper.fandAll();
    }

    @Override
    public PageResult findPage(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>)brandMapper.fandAll();
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByParmaryKey(id);
    }

    @Override
    public void update(TbBrand brand) {
        brandMapper.update(brand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.delete(id);
        }
    }
}
