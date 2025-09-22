package com.bogdan.cards.controller;

import com.bogdan.cards.constants.CardsConstants;
import com.bogdan.cards.dto.CardsContactInfoDTO;
import com.bogdan.cards.dto.CardsDTO;
import com.bogdan.cards.dto.ResponseDTO;
import com.bogdan.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REST APIs for Cards")
@RestController
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CardsController {

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private CardsContactInfoDTO cardsContactInfoDto;

    private ICardsService iCardsService;

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

    public CardsController(ICardsService iCardsService) {
        this.iCardsService = iCardsService;
    }

    @Operation(summary = "Create Card REST API")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createCard(@Valid @RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits.")
                                                  String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @Operation(summary = "Get Card Details REST API")
    @GetMapping("/get")
    public ResponseEntity<CardsDTO> getCardDetails(@RequestHeader("msbank-correlation-id") String correlationId,
                                                   @RequestParam
                                                        @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits.")
                                                        String mobileNumber) {
        logger.debug("msbank-correlation-id found: {} ", correlationId);
        CardsDTO cardsDTO = iCardsService.getCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardsDTO);
    }

    @Operation(summary = "Update Card Details REST API")
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateCardDetails(@Valid @RequestBody CardsDTO cardsDTO) {
        boolean isUpdated = iCardsService.updateCard(cardsDTO);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(summary = "Delete Card Details REST API")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteCardDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits.")
                                                            String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(summary = "Information about version of Cards REST API")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(summary = "Contact Info details that can be reached out in case of any issues")
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDTO> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }
}
