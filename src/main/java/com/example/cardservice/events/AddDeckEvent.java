package com.example.cardservice.events;

import com.example.cardservice.models.Game;

public class AddDeckEvent extends MultipleEvent {
    public AddDeckEvent(Game game) {
        events.add(new SingleEvent(game.getId(), "Adding a deck to the game deck"));
        events.add(new SingleEvent(game.getGameDeck().getId(), "Adding a deck to the game deck"));
    }
}
