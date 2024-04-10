package com.Service.OrderService.controllers;


import com.Service.OrderService.dto.OrderRequest;
import com.Service.OrderService.models.Order;
import com.Service.OrderService.services.OrderServices;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@Slf4j
public class orderController {
private OrderServices orderServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory",fallbackMethod = "fallback")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")

    public String placeOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException {
        orderServices.placeorder(orderRequest);
        return "Order";
    }
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
