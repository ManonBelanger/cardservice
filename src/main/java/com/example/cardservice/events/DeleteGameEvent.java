package com.example.cardservice.events;

import com.example.cardservice.models.Game;

public class DeleteGameEvent extends MultipleEvent{
    public DeleteGameEvent(Game game) {
        events.add(new SingleEvent(game.getId(), "Game deleted"));
        events.add(new SingleEvent(game.getGameDeck().getId(), "Game deck deleted"));
    }
}
