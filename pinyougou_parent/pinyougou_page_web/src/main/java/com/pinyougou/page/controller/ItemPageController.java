package com.pinyougou.page.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemPage")
public class ItemPageController {

    @Reference
    private ItemPageService itemPageService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 基于商品id查询商品数据生成静态页
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/genHtml")
    public String genHtml(Long goodsId) {

        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Goods goods = itemPageService.findOne(goodsId);
            List<TbItem> itemList = goods.getItemList();

            for (TbItem item : itemList) {
                Map<String, Object> map = new HashMap<>();
                map.put("item", item);
                map.put("goods", goods);
                Writer out = new FileWriter("G:/freeMarkerTest/pinyougou/" + item.getId() + ".html");
                template.process(map, out);
                out.close();
            }
            return "success ....";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail ....";
        }

    }
}
