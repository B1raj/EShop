package com.biraj.eshop.service;


import com.biraj.eshop.beans.CartRequest;
import com.biraj.eshop.entity.Cart;

import java.util.List;

public interface CartService {
    Cart saveCart(CartRequest cart);
    List<Cart> getCarts();
    Cart getByCartId(int cartId);

}
