package com.biraj.eshop.service.impl;


import com.biraj.eshop.beans.CartRequest;
import com.biraj.eshop.entity.Cart;
import com.biraj.eshop.entity.CartOrder;
import com.biraj.eshop.exception.CartNotFoundException;
import com.biraj.eshop.repository.CartOrderRepository;
import com.biraj.eshop.repository.CartRepository;
import com.biraj.eshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartOrderRepository cartOrderRepository;


    @Override
    public Cart saveCart(CartRequest req) {
        return cartRepository.save(Cart.builder().user(req.getUser()).createdAt(Timestamp.from(Instant.now())).createdBy("Admin").build());
    }

    @Override
    public List<Cart> getCarts() {
        List<Cart> carts = (List<Cart>)cartRepository.findAll();
        for(Cart c: carts){
            List<CartOrder> orders = cartOrderRepository.getAllByCartId(c.getId());
            c.setOrders(orders);
        }
        return carts;
    }

    @Override
    public Cart getByCartId(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        List<CartOrder> orders = cartOrderRepository.getAllByCartId(cartId);
        cart.setOrders(orders);
        return cart;
    }

}
