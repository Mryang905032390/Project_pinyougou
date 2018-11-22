package com.pinyougou.pay.service;

import java.util.Map;

public interface PayService {
    public Map<String,Object> createNative(String out_trade_no,String total_fee) throws Exception;
    public Map<String,String> queryPayStatus(String out_trade_no) throws Exception;
}
