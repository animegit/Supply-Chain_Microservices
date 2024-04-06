package com.Service.OrderService.controllers;


import com.Service.OrderService.dto.OrderRequest;
import com.Service.OrderService.services.OrderServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class orderController {
private OrderServices orderServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException {
        orderServices.placeorder(orderRequest);
        return "Order";
    }
}
