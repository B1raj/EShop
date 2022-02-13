package com.biraj.eshop.service;

import com.biraj.eshop.beans.ProductRequest;
import com.biraj.eshop.entity.Product;
import java.util.List;

public interface ProductService {
    Product saveProduct(ProductRequest product);
    Product getProduct(int pid);
    List<Product> getProducts();
    Product updateProduct(ProductRequest product, int pid);
    void removeProduct(int pid);
}
