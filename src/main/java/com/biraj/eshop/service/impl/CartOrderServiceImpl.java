package com.biraj.eshop.service.impl;


import com.biraj.eshop.beans.CartOrderRequest;
import com.biraj.eshop.entity.CartOrder;
import com.biraj.eshop.exception.CartNotFoundException;
import com.biraj.eshop.exception.OrderNotFoundException;
import com.biraj.eshop.repository.CartOrderRepository;
import com.biraj.eshop.repository.CartRepository;
import com.biraj.eshop.service.CartOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class CartOrderServiceImpl implements CartOrderService {

    @Autowired
    private CartOrderRepository cartOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartOrder saveCartOrder(CartOrderRequest req,int cartId) {

        checkCartPresentOrNot(cartId);
        // Check if cart already has the product, then increase the quantity instead
        Optional<CartOrder> opt = cartOrderRepository.getByCartIdAndProductId(cartId, req.getProductId());

       if(opt.isPresent()){
           CartOrder co = opt.get();
           co.setQuantity(co.getQuantity()+req.getQuantity());
           return cartOrderRepository.save(co);
       }else{
           CartOrder co = CartOrder.builder().cartId(cartId).productId(req.getProductId()).quantity(req.getQuantity()).createdAt(Timestamp.from(Instant.now())).createdBy("Admin").build();
           return cartOrderRepository.save(co);
       }
    }

    private void checkCartPresentOrNot(int cartId) {
        cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException());
    }

    @Override
    public CartOrder updateCartOrder(CartOrderRequest req, int cartId, int productId) {
        checkCartPresentOrNot(cartId);
        CartOrder co = cartOrderRepository.getByCartIdAndProductId(cartId, productId).orElseThrow(()-> new OrderNotFoundException());
        co.setQuantity(req.getQuantity());
        co.setUpdatedAt(Timestamp.from(Instant.now()));
        co.setUpdatedBy("Admin");
        return cartOrderRepository.save(co);
    }

    @Override
    public void removeCartOrder(int cartId, int productId) {
        checkCartPresentOrNot(cartId);
        CartOrder co = cartOrderRepository.getByCartIdAndProductId(cartId, productId).orElseThrow(()-> new OrderNotFoundException());
        cartOrderRepository.delete(co);
    }

}
