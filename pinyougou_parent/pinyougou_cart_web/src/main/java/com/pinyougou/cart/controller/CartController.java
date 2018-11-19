package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import entity.Result;
import groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    private String getSessionId() {
        String sessionId = CookieUtil.getCookieValue(request, "cartCookie", "utf-8");
        if (sessionId == null) {
            sessionId = session.getId();
            CookieUtil.setCookie(request, response, "cartCookie", sessionId, 3600 * 24 * 7, "utf-8");
        }
        return sessionId;
    }

    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String userName= SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(userName);
        String sessionId = getSessionId();
        List<Cart> cartList_sessionId = cartService.selectCartListFromRedis(sessionId);
        if (userName.equals("anonymousUser")){
            return cartList_sessionId;
        }else{
            List<Cart> cartList_userName = cartService.selectCartListFromRedis(userName);
            if (cartList_sessionId!=null&&cartList_sessionId.size()>0){
                cartList_userName = cartService.mergeCartList(cartList_sessionId,cartList_userName);
                cartService.deleteCartList(sessionId);
                cartService.saveCartListToRedis(userName,cartList_userName);
            }
            return cartList_userName;
        }
    }

    @RequestMapping("/addItemToCartList")
    public Result addItemToCartList(Long itemId, Integer num) {
        try {
            String userName= SecurityContextHolder.getContext().getAuthentication().getName();
            String sessionId = getSessionId();
            List<Cart> cartList = findCartList();
            cartList = cartService.addItemToCartList(cartList, itemId, num);
            if (userName.equals("anonymousUser")){
                System.out.println("save cartList to redis by sessionId...");
                cartService.saveCartListToRedis(sessionId,cartList);
            }else{
                System.out.println("save cartList to redis by username...");
                cartService.saveCartListToRedis(userName,cartList);
            }
            return new Result(true,"添加购物车成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加购物车失败");
        }
    }
}
