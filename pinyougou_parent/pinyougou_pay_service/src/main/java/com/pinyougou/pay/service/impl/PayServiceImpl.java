package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import util.HttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
@Transactional
public class PayServiceImpl implements PayService {

    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;
    @Override
    public Map<String, Object> createNative(String out_trade_no, String total_fee) throws Exception {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("mch_id",partner);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body","品优购");
        paramMap.put("out_trade_no",out_trade_no);
        paramMap.put("total_fee",total_fee);
        paramMap.put("spbill_create_ip","127.0.0.1");
        paramMap.put("notify_url",notifyurl);
        paramMap.put("trade_type","NATIVE");
        paramMap.put("product_id","1");
        String paramXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXml);
        httpClient.post();
        String resultXml = httpClient.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        String code_url = resultMap.get("code_url");
        Map<String,Object> map = new HashMap<>();
        map.put("code_url",code_url);
        map.put("out_trade_no",out_trade_no);
        map.put("total_fee",total_fee);
        System.out.println(map.toString());
        return map;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) throws Exception {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("mch_id",partner);
        paramMap.put("out_trade_no",out_trade_no);
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());

        String paramXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXml);
        httpClient.post();
        String resultXml = httpClient.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        return resultMap;
    }
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TbPayLog findPayLogByUserId(String userId) {
        TbPayLog payLog = (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
        return payLog;
    }

    @Autowired
    private TbPayLogMapper payLogMapper;
    @Autowired
    private TbOrderMapper orderMapper;
    @Override
    public void updateStatus(String out_trade_no, String transaction_id) {
        TbPayLog tbPayLog = payLogMapper.selectByPrimaryKey(out_trade_no);
        tbPayLog.setTradeState("2");
        tbPayLog.setPayTime(new Date());
        tbPayLog.setTransactionId(transaction_id);
        payLogMapper.updateByPrimaryKey(tbPayLog);

        String orderList = tbPayLog.getTransactionId();
        String[] ids = orderList.split(",");
        for (String id : ids) {
            TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.parseLong(id));
            tbOrder.setPaymentTime(new Date());
            tbOrder.setStatus("2");
            orderMapper.updateByPrimaryKey(tbOrder);
        }
        redisTemplate.boundHashOps("payLog").delete(tbPayLog.getUserId());
    }
}
