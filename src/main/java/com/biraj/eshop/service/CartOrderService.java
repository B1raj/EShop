package com.biraj.eshop.service;

import com.biraj.eshop.beans.CartOrderRequest;
import com.biraj.eshop.entity.CartOrder;

public interface CartOrderService {
    CartOrder saveCartOrder(CartOrderRequest cart, int cartId);
    CartOrder updateCartOrder(CartOrderRequest req, int cartId, int productId);
    void removeCartOrder(int cartId, int productId);
}
