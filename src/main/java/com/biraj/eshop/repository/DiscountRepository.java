package com.biraj.eshop.repository;

import com.biraj.eshop.entity.Discount;
import com.biraj.eshop.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer> {

    List<Discount> getAllByProduct(int product);
    List<Discount> getAllByDiscountProduct(int discountProduct);
}
