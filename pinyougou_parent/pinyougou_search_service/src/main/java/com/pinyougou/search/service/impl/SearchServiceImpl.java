package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        HighlightQuery query = new SimpleHighlightQuery();
        //输入条件查询
        String keywords = (String) searchMap.get("keywords");
        Criteria criteria = null;
        if (keywords != null && !"".equals(keywords)) {
            //输入了关键字
            criteria = new Criteria("item_keywords").is(keywords);
        } else {
            criteria = new Criteria().expression("*:*");
        }
        //条件品牌查询
        String brand = (String) searchMap.get("brand");
        if (brand != null && !"".equals(brand)) {
            Criteria brandCriteria = new Criteria("item_brand").is(brand);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(filterQuery);
        }
        //条件分类查询
        String category = (String) searchMap.get("category");
        if (category != null && !"".equals(category)) {
            Criteria categoryCriteria = new Criteria("item_category").is(category);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(filterQuery);
        }

        //条件规格查询
        Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
        if (specMap != null) {
            for (String key : specMap.keySet()) {
                Criteria specCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //条件价格查询
        String price = (String) searchMap.get("price");
        if (price != null && !"".equals(price)) {
            String[] split = price.split(("-"));
            if (!"0".equals(split[0])){
                Criteria priceCriteria = new Criteria("item_price").greaterThanEqual(split[0]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);

            }
            if (!"*".equals(split[1])){
                Criteria priceCriteria = new Criteria("item_price").lessThanEqual(split[1]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //商品排序操作
        String sortField = (String) searchMap.get("sortField");
        String sort = (String) searchMap.get("sort");
        if (sortField != null && !"".equals(sortField)) {
          //设置排序条件
            if ("ASC".equals(sort)){
                query.addSort(new Sort(Sort.Direction.ASC,"item_"+sortField));
            }else{
                query.addSort(new Sort(Sort.Direction.DESC,"item_"+sortField));
            }
        }

        //分页条件查询
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        query.setOffset((pageNo-1)*pageSize);
        query.setRows(pageSize);
        query.addCriteria(criteria);

        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        List<TbItem> content = page.getContent();

        for (TbItem item : content) {
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            if (highlights.size() > 0) {
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if (snipplets.size() > 0) {
                    item.setTitle(snipplets.get(0));
                }
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", content);
        resultMap.put("pageNo", pageNo);
        resultMap.put("totalPages",page.getTotalPages());
        return resultMap;
    }
}
