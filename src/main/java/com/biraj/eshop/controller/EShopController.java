package com.biraj.eshop.controller;


import com.biraj.eshop.beans.*;
import com.biraj.eshop.entity.Cart;
import com.biraj.eshop.entity.CartOrder;
import com.biraj.eshop.entity.Discount;
import com.biraj.eshop.entity.Product;
import com.biraj.eshop.service.CartOrderService;
import com.biraj.eshop.service.CartService;
import com.biraj.eshop.service.DiscountService;
import com.biraj.eshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/eshop")
@Slf4j
public class EShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartOrderService cartOrderService;

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.GET)
    private ResponseEntity getProduct(HttpServletRequest request, @PathVariable(value = "pid") int pid) {
        return new ResponseEntity(productService.getProduct(pid), HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    private ResponseEntity getAllProducts(HttpServletRequest request) {
        List<Product> p = productService.getProducts();
        return new ResponseEntity(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    private ResponseEntity createProduct(HttpServletRequest request, @RequestBody ProductRequest product) {
        return new ResponseEntity(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.PUT)
    private ResponseEntity updateProduct(HttpServletRequest request, @PathVariable(value = "pid") int pid, @RequestBody ProductRequest product) {
        return new ResponseEntity(productService.updateProduct(product, pid), HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.DELETE)
    private ResponseEntity removeProduct(HttpServletRequest request, @PathVariable(value = "pid") int pid) {
        productService.removeProduct(pid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/products/discounts", method = RequestMethod.GET)
    private ResponseEntity getAllDiscounts(HttpServletRequest request) {
        List<Discount> p = discountService.getDiscounts();
        return new ResponseEntity(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/discounts", method = RequestMethod.POST)
    private ResponseEntity createDiscount(HttpServletRequest request, @RequestBody DiscountRequest discountRequest) {
        return new ResponseEntity(discountService.saveDiscount(discountRequest), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    private ResponseEntity getAllCarts(HttpServletRequest request) {
        List<Cart> cart = cartService.getCarts();
        return new ResponseEntity(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/carts/{cid}", method = RequestMethod.GET)
    private ResponseEntity getCartByID(HttpServletRequest request, @PathVariable(value = "cid") int cid) {
        Cart cart = cartService.getByCartId(cid);
        return new ResponseEntity(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/carts", method = RequestMethod.POST)
    private ResponseEntity createCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.saveCart(cartRequest);
        return new ResponseEntity(cart, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/carts/{cid}/orders", method = RequestMethod.POST)
    private ResponseEntity createCartOrder(HttpServletRequest request, @RequestBody CartOrderRequest cartOrderRequest, @PathVariable(value = "cid") int cid) {
        CartOrder cartOrder = cartOrderService.saveCartOrder(cartOrderRequest, cid);
        return new ResponseEntity(cartOrder, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/carts/{cid}/orders", method = RequestMethod.PUT)
    private ResponseEntity updateCartOrder(HttpServletRequest request, @RequestBody CartOrderRequest cartOrderRequest, @PathVariable(value = "cid") int cid) {
        CartOrder cart = cartOrderService.updateCartOrder(cartOrderRequest, cid, cartOrderRequest.getProductId());
        return new ResponseEntity(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/carts/{cid}/orders", method = RequestMethod.DELETE)
    private ResponseEntity removeProduct(HttpServletRequest request, @RequestBody CartOrderRequest cartOrderRequest, @PathVariable(value = "cid") int cid) {
        cartOrderService.removeCartOrder(cid, cartOrderRequest.getProductId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/checkout/{cid}", method = RequestMethod.GET)
    private ResponseEntity checkoutCart(HttpServletRequest request, @PathVariable(value = "cid") int cid) {

        Cart cart = cartService.getByCartId(cid);
        List<CartOrder> orders = cart.getOrders();

        double discountAmount = 0.0;
        double amount = 0.0;
        Map<Integer, Integer> m = new HashMap();
        Bill bill = new Bill();
        bill.setItems(new ArrayList<>());
        for (CartOrder order : orders) {
            Item item = new Item();
            double price = 0.0;
            int productId = order.getProductId();
            int quantity = order.getQuantity();
            Product product = productService.getProduct(productId);
            item.setProductName(product.getName());
            item.setQuantity(quantity);

            List<Discount> discounts = product.getDiscounts();
            double productPrice = product.getPrice();
            if (null != discounts && discounts.size() > 0) {
                for (Discount discount : discounts) {
                    String discountType = discount.getDiscountType();
                    if(null == discountType){
                        continue;
                    }
                    if (discountType.equals("DISCOUNT")) {
                        double discountPercent = discount.getDiscountPercent();
                        if (quantity % 2 == 0) {
                            price = getPrice(quantity, productPrice, discountPercent);
                        } else if (quantity == 1) {
                            price = quantity * productPrice;
                        }else {
                            price = getPrice(quantity, productPrice, discountPercent);
                            price += productPrice;
                        }
                        double actualPrice = quantity * productPrice;
                        discountAmount += (actualPrice - price);

                    } else if (discountType.equals("BUNDLE")) {
                        price = productPrice * quantity;

                        //add free product
                        {
                            Item freeItem = new Item();
                            Product discountProduct = productService.getProduct(discount.getDiscountProduct());
                            freeItem.setProductName(discountProduct.getName());
                            freeItem.setPrice(0.0);
                            freeItem.setQuantity(quantity);
                            bill.getItems().add(freeItem);
                            discountAmount += discountProduct.getPrice() * quantity;
                        }
                    }
                    amount += price;
                    item.setPrice(price);
                }
            } else {
                price = productPrice * quantity;
                item.setPrice(price);
                amount += price;
            }
            bill.getItems().add(item);
        }
        bill.setNetAmount(amount);
        bill.setNetDiscount(discountAmount);
        return new ResponseEntity(bill, HttpStatus.OK);
    }

    private double getPrice(int quantity,double price, double discountPercent) {
          return (quantity*price/2)*((200-discountPercent)/100);
        //return (quantity*price/2)*((100+discountPercent)/100);
    }

}








