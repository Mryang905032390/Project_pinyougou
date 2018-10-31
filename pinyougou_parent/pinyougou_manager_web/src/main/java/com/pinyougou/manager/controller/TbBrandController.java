package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.TbBrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class TbBrandController {

    @Reference
    private TbBrandService tbBrandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return tbBrandService.fandAll();
    }

    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        return tbBrandService.findPage(pageNum, pageSize);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            tbBrandService.add(brand);
            return new Result(true, "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增失败");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
     return tbBrandService.findOne(id);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            tbBrandService.update(brand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            tbBrandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
}
