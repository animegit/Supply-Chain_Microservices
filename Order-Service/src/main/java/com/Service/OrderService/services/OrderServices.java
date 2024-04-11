package com.Service.OrderService.services;


import com.Service.OrderService.dto.InventoryResponse;
import com.Service.OrderService.dto.OrderLineRequest;
import com.Service.OrderService.dto.OrderRequest;
import com.Service.OrderService.models.Order;
import com.Service.OrderService.models.OrderLineItems;
import com.Service.OrderService.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderServices {
 private OrderRepository orderRepository;
 private WebClient.Builder webClientBuilder;
private  final ObservationRegistry observationRegistry;


    public String placeorder(OrderRequest orderRequest) throws IllegalAccessException {
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList=orderRequest.getOrderLineRequestList().stream().map(this::maptoDto).toList();


        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes=order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        Observation inventoryobservation=Observation.createNotStarted("inventory-service-lookup",this.observationRegistry);
        inventoryobservation.lowCardinalityKeyValue("call","inventory-service");
        return inventoryobservation.observe(()->{


            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).
                    build()).retrieve().bodyToMono(InventoryResponse[].class).block();

            boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                return "Order Placed";
            } else{

                return "No Item in stock";
            }

        });






    }
    private OrderLineItems maptoDto(OrderLineRequest orderLineRequest){
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineRequest.getPrice());
        orderLineItems.setSkuCode(orderLineRequest.getSkuCode());
        orderLineItems.setQuantity(orderLineRequest.getQuantity());
        return orderLineItems;

    }
}
