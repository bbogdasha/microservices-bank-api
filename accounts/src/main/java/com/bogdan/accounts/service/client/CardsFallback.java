package com.bogdan.accounts.service.client;

import com.bogdan.accounts.dto.CardsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient {

    @Override
    public ResponseEntity<CardsDTO> getCardDetails(String correlationId, String mobileNumber) {
        return null;
    } //TODO
}
