package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecificationServiceImple implements SpecificationService {
    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, TbSpecification tbSpecification) {
        PageHelper.startPage(pageNum, pageSize);
        TbSpecificationExample example = new TbSpecificationExample();
        if (tbSpecification != null) {
            String specName = tbSpecification.getSpecName();
            if (specName != null && !"".equals(specName)) {
                TbSpecificationExample.Criteria criteria = example.createCriteria();
                criteria.andSpecNameLike("%" + specName + "%");
            }
        }
        Page<TbSpecification> page = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
