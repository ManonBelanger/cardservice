package com.example.cardservice.events;

import com.example.cardservice.models.Card;
import com.example.cardservice.models.Game;

public class DealCardEvent extends MultipleEvent {
    public DealCardEvent(Game game, String playerId, Card card) {
        events.add(new SingleEvent(playerId, String.format("Player received card %s %s", card.getValue(), card.getSuit())));
        events.add(new SingleEvent(game.getId(), String.format("Card %s %s was dealt to player %s", 
            card.getValue(), card.getSuit(), playerId)));
        events.add(new SingleEvent(game.getGameDeck().getId(), String.format("Card %s %s was dealt to player %s", 
            card.getValue(), card.getSuit(), playerId)));
    }
}
