package com.bogdan.accounts.service.client;

import com.bogdan.accounts.dto.LoansDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "/api/get",consumes = "application/json")
    ResponseEntity<LoansDTO> getLoanDetails(@RequestHeader("msbank-correlation-id") String correlationId,
                                            @RequestParam String mobileNumber);

}
