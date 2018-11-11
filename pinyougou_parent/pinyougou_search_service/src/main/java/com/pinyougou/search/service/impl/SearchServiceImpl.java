package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        HighlightQuery query = new SimpleHighlightQuery();
        String keywords = (String) searchMap.get("keywords");
        Criteria criteria = null;
        if (keywords != null && !"".equals(keywords)){
            //输入了关键字
            criteria = new Criteria("item_keywords").is(keywords);
        }else{
            criteria = new Criteria().expression("*:*");
        }

        query.addCriteria(criteria);

        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        List<TbItem> content = page.getContent();

        for (TbItem item : content) {
            List<HighlightEntry.Highlight> highlights=page.getHighlights(item);
            if (highlights.size()>0){
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if (snipplets.size()>0){
                    item.setTitle(snipplets.get(0));
                }
            }
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",content);
        return resultMap;
    }
}
