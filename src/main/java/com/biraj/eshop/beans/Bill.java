package com.biraj.eshop.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    private List<Item> items;
    private double netAmount;
    private double netDiscount;
}
