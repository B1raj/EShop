package com.biraj.eshop.service;

import com.biraj.eshop.beans.DiscountRequest;
import com.biraj.eshop.entity.Discount;

import java.util.List;

public interface DiscountService {

    Discount saveDiscount(DiscountRequest discount);
    List<Discount> getDiscounts();
    List<Discount> getAllByProduct(int product);

}
