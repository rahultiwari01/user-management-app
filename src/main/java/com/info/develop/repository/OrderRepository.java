package com.info.develop.repository;

import com.info.develop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    /**
     * Finds all orders associated with a specific customer ID.
     * Spring Data JPA automatically implements this method based on its name.
     * @param customerId The ID of the customer (which is the username in this context).
     * @param pageable Contains pagination and sorting information.
     * @return A paginated list of orders for the given customer.
     */
    Page<Order> findByCustomerId(String customerId, Pageable pageable);
}