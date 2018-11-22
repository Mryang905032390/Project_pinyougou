package com.pinyougou.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.PayService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("createNative")
    public Map<String, Object> createNative() {
        IdWorker idWorker = new IdWorker();
        try {
            return payService.createNative(idWorker.nextId() + "", "1");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        int i = 1;
        try {
            while (true) {
                Thread.sleep(3000);
                i++;
                if (i>=100){
                    return new Result(false, "timeout");
                }
                Map<String, String> resultMap = payService.queryPayStatus(out_trade_no);
                if ("SUCCESS".equals(resultMap.get("trade_state"))) {
                    return new Result(true, "支付成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
