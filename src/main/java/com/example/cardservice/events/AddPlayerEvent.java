package com.example.cardservice.events;

import com.example.cardservice.models.Game;

public class AddPlayerEvent extends MultipleEvent{
    public AddPlayerEvent(Game game, String playerId) {
        events.add(new SingleEvent(playerId, String.format("Player was added to game %s", game.getId())));
        events.add(new SingleEvent(game.getId(), String.format("Player %s was added to the game", playerId)));
    }
}
