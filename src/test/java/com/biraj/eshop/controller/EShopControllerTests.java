package com.biraj.eshop.controller;

import com.biraj.eshop.beans.Bill;
import com.biraj.eshop.entity.Cart;
import com.biraj.eshop.entity.CartOrder;
import com.biraj.eshop.entity.Discount;
import com.biraj.eshop.entity.Product;
import com.biraj.eshop.exception.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class EShopControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "http://localhost:";

    @Test
    void givenProductId_GetProductEndpoint_ShouldReturnProduct(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/1"));
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).isAvailable());
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void givenNotExistingProductId_GetProductEndpoint_ShouldReturn400Error(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/100"));
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenStringProductId_GetProductEndpoint_ShouldReturn500Error(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/dummyProduct"));
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getAllProductsEndpoint_ShouldReturnAllProducts(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products"));
        ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).length).isGreaterThanOrEqualTo(4);
    }

    @Test
    void givenProduct_CreateProductEndpoint_ShouldCreateProduct(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products"));
        String productName= "Motherboard";
        Product product = Product.builder().name(productName).description("Intell Motherboard").price(199.99).build();
        ResponseEntity<Product> response = restTemplate.postForEntity(url, product, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo(productName);
    }

    @Test
    void givenProductAndId_UpdateProductEndpoint_ShouldUpdateProduct(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/1"));
        String productName= "Motherboard";
        double price= 299.99;
        Product product = Product.builder().name(productName).description("Intell Motherboard").price(price).build();
        HttpEntity<Product> requestUpdate = new HttpEntity<>(product);
        ResponseEntity<Product>  response =restTemplate.exchange(url, HttpMethod.PUT,requestUpdate,Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(productName);
        assertThat(response.getBody().getPrice()).isEqualTo(price);

    }

    @Test
    void givenProduct_RemoveProductEndpoint_ShouldRemoveProduct(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/5"));
        int initial = getProductCount();
        restTemplate.delete(url);
        int last = getProductCount();
        assertThat(initial-1).isEqualTo(last);
    }

   public int getProductCount(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products"));
        ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class);
        return response.getBody().length;
    }


    @Test
    void getDiscountsEndpoint_ShouldReturnAllDiscounts(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/discounts"));
        ResponseEntity<Discount[]> response = restTemplate.getForEntity(url, Discount[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).length).isGreaterThan(0);
    }

    @Test
    void givenDiscount_CreateDiscountEndpoint_ShouldCreateDiscount(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/discounts"));
        Discount discount = Discount.builder().product(1).discountProduct(2).discountPercent(20).discountType("BUNDLE").build();
        ResponseEntity<Discount> response = restTemplate.postForEntity(url, discount, Discount.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void givenInvalidProductInDiscount_CreateDiscountEndpoint_ShouldThrowProductNotFoundException(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/discounts"));
        Discount discount = Discount.builder().product(20).discountProduct(2).discountPercent(20).discountType("BUNDLE").build();
        ResponseEntity<Discount> response = restTemplate.postForEntity(url, discount, Discount.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenInvalidDiscountProductInDiscount_CreateDiscountEndpoint_ShouldThrowProductNotFoundException(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/products/discounts"));
        Discount discount = Discount.builder().product(1).discountProduct(20).discountPercent(20).discountType("BUNDLE").build();
        ResponseEntity<Discount> response = restTemplate.postForEntity(url, discount, Discount.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenCart_GetAllCartsEndpoint_ShouldReturnCart(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts"));
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(url, Cart[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isNotNull();
    }

    @Test
    void givenCart_GetCartByIDEndpoint_ShouldReturnCart(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts/1"));
        ResponseEntity<Cart> response = restTemplate.getForEntity(url, Cart.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
    }

    @Test
    void givenProduct_CreateCartEndpoint_ShouldCreateAndReturnCart(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts"));
        Cart cart = Cart.builder().user("1").build();
        ResponseEntity<Cart> response = restTemplate.postForEntity(url, cart, Cart.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUser()).isEqualTo("1");
    }

    @Test
    void givenCartOrder_CreateCartOrderEndpoint_ShouldCreateAndReturnOrder(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts/1/orders"));
        CartOrder cartOrder = CartOrder.builder().productId(1).quantity(1).build();
        ResponseEntity<CartOrder> response = restTemplate.postForEntity(url, cartOrder, CartOrder.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getQuantity()).isPositive();
    }

    @Test
    void givenProductAndId_UpdateCartOrderEndpoint_ShouldUpdateCartOrder(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts/1/orders"));
        CartOrder cartOrder = CartOrder.builder().productId(1).quantity(10).build();
        HttpEntity<CartOrder> requestUpdate = new HttpEntity<>(cartOrder);
        ResponseEntity<CartOrder>  response =restTemplate.exchange(url, HttpMethod.PUT,requestUpdate,CartOrder.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getQuantity()).isEqualTo(10);
    }

    @Test
    void givenOrder_RemoveOrderEndpoint_ShouldRemoveOrder(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts/2/orders"));
        CartOrder cartOrder = CartOrder.builder().productId(2).build();
        //restTemplate.delete(url,cartOrder);
        HttpEntity<CartOrder> requestUpdate = new HttpEntity<>(cartOrder);
        ResponseEntity<CartOrder>  response =restTemplate.exchange(url, HttpMethod.DELETE,requestUpdate,CartOrder.class);
        assertThat(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void givenOrder_RemoveNotExistingOrderEndpoint_ShouldGetError(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/carts/10/orders"));
        CartOrder cartOrder = CartOrder.builder().productId(1).build();
        HttpEntity<CartOrder> requestUpdate = new HttpEntity<>(cartOrder);
        ResponseEntity<CartOrder>  response =restTemplate.exchange(url, HttpMethod.DELETE,requestUpdate,CartOrder.class);
        assertThat(response.getStatusCode().is5xxServerError());
    }

    @Test
    void givenCartId_checkoutCartEndpoint_ShouldReturnBill(){
        String url = BASE_URL.concat((String.valueOf(port)).concat("/eshop/checkout/1"));
        ResponseEntity<Bill> response = restTemplate.getForEntity(url, Bill.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getItems().size()).isGreaterThan(0);
    }

}
