package com.biraj.eshop.service.impl;

import com.biraj.eshop.beans.DiscountRequest;
import com.biraj.eshop.beans.ProductRequest;
import com.biraj.eshop.entity.Discount;
import com.biraj.eshop.entity.Product;
import com.biraj.eshop.exception.ProductNotFoundException;
import com.biraj.eshop.repository.DiscountRepository;
import com.biraj.eshop.repository.ProductRepository;
import com.biraj.eshop.service.DiscountService;
import com.biraj.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Discount saveDiscount(DiscountRequest discountRequest) {

        productRepository.findById(discountRequest.getProduct()).orElseThrow(()-> new ProductNotFoundException("4001","Create product with id "+ discountRequest.getProduct()+ " before creating discount for it."));
        productRepository.findById(discountRequest.getDiscountProduct()).orElseThrow(()-> new ProductNotFoundException("4001","Create product with id "+ discountRequest.getDiscountProduct()+ " before creating discount for it."));
        Discount discount = Discount.builder().product(discountRequest.getProduct()).discountPercent(discountRequest.getDiscountPercent())
                .discountProduct(discountRequest.getDiscountProduct()).createdAt(Timestamp.from(Instant.now())).createdBy("Admin").build();
        return discountRepository.save(discount);
    }

    @Override
    public List<Discount> getDiscounts() {
        return (List<Discount>) discountRepository.findAll();
    }

    @Override
    public List<Discount> getAllByProduct(int product) {
        return discountRepository.getAllByProduct(product);
    }
}
