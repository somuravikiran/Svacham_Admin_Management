package com.svacham.Order_Service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StockClientService {

    private final WebClient.Builder webClientBuilder;

    public Boolean checkStock(String itemName) {
        return webClientBuilder.build()
                .get()
                .uri("https://stock-service-7kyr.onrender.com/api/stock/check/" + itemName)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public String reduceStock(String itemName, Integer qty) {
        return webClientBuilder.build()
                .put()
                .uri("https://stock-service-7kyr.onrender.com/api/stock/reduce/" + itemName + "/" + qty)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}