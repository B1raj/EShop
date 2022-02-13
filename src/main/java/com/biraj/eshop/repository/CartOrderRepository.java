package com.biraj.eshop.repository;

import com.biraj.eshop.entity.CartOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartOrderRepository extends CrudRepository<CartOrder, Integer> {

    Optional<CartOrder> getByCartIdAndProductId(int cartId, int productId);
    List<CartOrder> getAllByCartId(int cartId);
}
