package com.pinyougou.demo;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.solr.util.SolrUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class SolrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void save() {
        TbItem item = new TbItem();
        item.setId(2L);
        item.setBrand("2华为");
        item.setTitle("2华为p20 移动3G 64G");
        item.setSeller("2华为旗舰店");
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void queryById() {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item.getId() + "  " + item.getBrand() + "  " + item.getTitle() + "  " + item.getSeller());
    }

    @Test
    public void deleteById() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }


    @Test
    public void deleteAll() {
        SolrDataQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Test
    public void saveBatch() {
        List<TbItem> items = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            TbItem item = new TbItem();
            item.setId(i);
            item.setBrand(i+"华为");
            item.setTitle(i+"华为p20 移动3G 64G");
            item.setSeller(i+"华为旗舰店");
            items.add(item);
        }
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    @Test
    public void queryPage() {
        Query query = new SimpleQuery("*:*");
        query.setOffset(0);
        query.setRows(20);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        for (TbItem item : tbItems) {
            System.out.println(item.getId() + "  " + item.getBrand() + "  " + item.getTitle() + "  " + item.getSeller());
        }
    }


    @Test
    public void multiQuery() {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_title").contains("9").and("item_seller").contains("5");
        query.addCriteria(criteria);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        for (TbItem item : tbItems) {
            System.out.println(item.getId() + "  " + item.getBrand() + "  " + item.getTitle() + "  " + item.getSeller());
        }
    }

    @Autowired
    private SolrUtil solrUtil;
    @Test
    public void testDateImport(){
        solrUtil.dataImport();
    }
}
