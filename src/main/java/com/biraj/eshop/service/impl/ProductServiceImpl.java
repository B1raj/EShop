package com.biraj.eshop.service.impl;

import com.biraj.eshop.beans.ProductRequest;

import com.biraj.eshop.entity.Discount;
import com.biraj.eshop.entity.Product;
import com.biraj.eshop.exception.ProductNotFoundException;
import com.biraj.eshop.repository.DiscountRepository;
import com.biraj.eshop.repository.ProductRepository;
import com.biraj.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Product saveProduct(ProductRequest productRequest) {
        Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice()).isAvailable(true).createdAt(Timestamp.from(Instant.now())).createdBy("Admin").build();
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(int pid) {
        Optional<Product> product = productRepository.findById(pid);
        Product p = product.orElseThrow(ProductNotFoundException::new);
        p.setDiscounts(discountRepository.getAllByProduct(p.getId()));
        return p;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> prods = (List<Product>) productRepository.findAll();

        for (Product p : prods) {
            p.setDiscounts(discountRepository.getAllByProduct(p.getId()));
        }
        return prods;
    }

    @Override
    public Product updateProduct(ProductRequest product, int pid) {
        Product old = this.getProduct(pid);
        old.setDescription(product.getDescription());
        old.setName(product.getName());
        old.setPrice(product.getPrice());
        old.setUpdatedAt(Timestamp.from(Instant.now()));
        old.setUpdatedBy("Admin");
        return productRepository.save(old);
    }

    @Override
    public void removeProduct(int pid) {
        Product toDelete = this.getProduct(pid);
        //TO DO : delete discount first
        List<Discount> d1 = discountRepository.getAllByProduct(pid);
        for (Discount d: d1) {
            discountRepository.deleteById(d.getId());
        }
        List<Discount> d2 = discountRepository.getAllByDiscountProduct(pid);
        for (Discount d: d2) {
            discountRepository.deleteById(d.getId());
        }
        productRepository.delete(toDelete);
    }
}
