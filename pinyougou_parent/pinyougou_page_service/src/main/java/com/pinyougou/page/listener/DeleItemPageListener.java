package com.pinyougou.page.listener;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.util.List;

public class DeleItemPageListener implements MessageListener {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = tbItemMapper.selectByExample(example);
            for (TbItem item : itemList) {
                new File("G:/freeMarkerTest/pinyougou/" + item.getId() + ".html").delete();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
