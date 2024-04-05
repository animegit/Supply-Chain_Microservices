package com.Service.OrderService.repository;

import com.Service.OrderService.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
