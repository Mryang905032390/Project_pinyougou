package com.pinyougou.order.service;

import groupEntity.Cart;

import java.util.List;

public interface CartService {
    List<Cart> selectCartListFromRedis(String sessionId);
}
