package com.example.cardservice.events;

import com.example.cardservice.models.Game;

public class RemovePlayerEvent extends MultipleEvent{
    public RemovePlayerEvent(Game game, String playerId) {
        events.add(new SingleEvent(playerId, String.format("Player was removed from game %s", game.getId())));
        events.add(new SingleEvent(game.getId(), String.format("Player %s was removed from the game", playerId)));
    }
}
