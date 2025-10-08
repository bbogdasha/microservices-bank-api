package com.bogdan.accounts.service.client;

import com.bogdan.accounts.dto.CardsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeignClient {

    @GetMapping(value = "/api/get",consumes = "application/json")
    ResponseEntity<CardsDTO> getCardDetails(@RequestHeader("msbank-correlation-id") String correlationId,
                                            @RequestParam String mobileNumber);

}