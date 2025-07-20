package com.sherry.cards_service.service;

import com.sherry.cards_service.dto.CardsDto;


public interface ICardsService {
    void createCard(String mobileNumber);

    CardsDto getCards(String mobileNumber);

    boolean updateCard(CardsDto cardsDto);

    boolean deleteCard(String mobileNumber);

}
