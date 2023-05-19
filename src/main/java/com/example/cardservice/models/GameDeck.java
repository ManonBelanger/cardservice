package com.example.cardservice.models;

import java.util.List;

import com.example.cardservice.exceptions.InvalidRequestException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

@RequiredArgsConstructor
public class GameDeck {
    @Getter
    private final String id;
    private List<Card> undealtCards = new ArrayList<>();

    public void addDeck() {
        for (Suit suit : Suit.values()) { 
            for (int value = 1; value <= 13; value++) {
                undealtCards.add(new Card(value, suit));
            }
        }
        Collections.shuffle(undealtCards);
    }

    public Card dealCard() throws InvalidRequestException {
        if (undealtCards.size() == 0) {
            throw new InvalidRequestException("No cards left");
        }
        return undealtCards.remove(0);
    }

    public HashMap<String, Integer> getUndealtCards() {
        HashMap<String, Integer> undealtCount = new HashMap<>();
        Integer numHearts = 0;
        Integer numSpades = 0;
        Integer numClubs = 0;
        Integer numDiamonds = 0;
        for (Card card : undealtCards) {
            switch(card.getSuit()) {
                case HEART:
                    numHearts++;
                    break;
                case SPADE:
                    numSpades++;
                    break;
                case CLUB:
                    numClubs++;
                    break;
                case DIAMOND:
                    numDiamonds++;
                    break;
            }
        }
        undealtCount.put(Suit.HEART.toString(), numHearts);
        undealtCount.put(Suit.SPADE.toString(), numSpades);
        undealtCount.put(Suit.CLUB.toString(), numClubs);
        undealtCount.put(Suit.DIAMOND.toString(), numDiamonds);
        return undealtCount;
    }

}
