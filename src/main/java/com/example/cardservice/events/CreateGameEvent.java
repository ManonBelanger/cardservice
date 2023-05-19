package com.example.cardservice.events;

import com.example.cardservice.models.Game;

public class CreateGameEvent extends MultipleEvent{
    public CreateGameEvent(Game game) {
        events.add(new SingleEvent(game.getId(), "Game created"));
        events.add(new SingleEvent(game.getGameDeck().getId(), "Game deck created"));
    }
}
