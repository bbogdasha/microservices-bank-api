package com.bogdan.cards.service.impl;

import com.bogdan.cards.constants.CardsConstants;
import com.bogdan.cards.dto.CardsDTO;
import com.bogdan.cards.entity.Cards;
import com.bogdan.cards.exception.CardAlreadyExistsException;
import com.bogdan.cards.exception.ResourceNotFoundException;
import com.bogdan.cards.mapper.CardsMapper;
import com.bogdan.cards.repository.CardsRepository;
import com.bogdan.cards.service.ICardsService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    private MessageSource messageSource;

    public CardsServiceImpl(CardsRepository cardsRepository, MessageSource messageSource) {
        this.cardsRepository = cardsRepository;
        this.messageSource = messageSource;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);

        if(optionalCards.isPresent()) {
            throw new CardAlreadyExistsException(messageSource.getMessage(
                    "exception.card.create",
                    new String[]{mobileNumber},
                    Locale.getDefault()));
        }

        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     *
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDTO getCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.card.resource.not_found",
                        new String[]{"Card", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        return CardsMapper.mapToCardsDTO(cards, new CardsDTO());
    }

    /**
     *
     * @param cardsDTO - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDTO cardsDTO) {
        boolean isUpdated = false;

        if (cardsDTO != null) {
            Cards cards = cardsRepository.findByCardNumber(cardsDTO.getCardNumber()).orElseThrow(
                    () -> new ResourceNotFoundException(messageSource.getMessage(
                            "exception.card.resource.not_found",
                            new String[]{"Card", "card number", cardsDTO.getCardNumber()},
                            Locale.getDefault()))
            );

            Optional<Cards> checkCardMobile = cardsRepository.findByMobileNumber(cardsDTO.getMobileNumber());

            if (checkCardMobile.isPresent() && !cards.getMobileNumber().equals(cardsDTO.getMobileNumber())) {
                throw new CardAlreadyExistsException(messageSource.getMessage(
                        "exception.card.update.mobile_number",
                        new String[]{cardsDTO.getMobileNumber()},
                        Locale.getDefault()));
            }

            CardsMapper.mapToCards(cardsDTO, cards);
            cardsRepository.save(cards);

            isUpdated = true;
        }

        return isUpdated;
    }

    /**
     * @param mobileNumber - Input MobileNumber
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.card.resource.not_found",
                        new String[]{"Card", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        cardsRepository.deleteById(cards.getCardId());

        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
