package com.bogdan.accounts.controller;

import com.bogdan.accounts.dto.CustomerDetailsDTO;
import com.bogdan.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REST APIs for Customers")
@RestController
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerController {

    private final ICustomersService iCustomersService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(ICustomersService iCustomersService) {
        this.iCustomersService = iCustomersService;
    }

    @Operation(summary = "Get Customer Details REST API")
    @GetMapping("/getCustomerDetails")
    public ResponseEntity<CustomerDetailsDTO> getCustomerDetails(@RequestHeader("msbank-correlation-id") String correlationId,
                                                                 @RequestParam
                                                                   @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                   String mobileNumber){
        logger.debug("msbank-correlation-id found: {} ", correlationId);
        CustomerDetailsDTO customerDetailsDTO = iCustomersService.getCustomerDetails(mobileNumber, correlationId);
        return ResponseEntity
                .status(HttpStatus.SC_OK)
                .body(customerDetailsDTO);
    }
}
