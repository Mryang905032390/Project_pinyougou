package com.pinyougou.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.security.Security;
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
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            TbPayLog tbPayLog = payService.findPayLogByUserId(userId);
            return payService.createNative( tbPayLog.getOutTradeNo(), tbPayLog.getTotalFee()+"");
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
                    String transaction_id = resultMap.get("transaction_id");
                    payService.updateStatus(out_trade_no,transaction_id);
                    return new Result(true, "支付成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
