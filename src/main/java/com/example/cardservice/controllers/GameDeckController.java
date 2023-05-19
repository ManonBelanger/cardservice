package com.example.cardservice.controllers;

import com.example.cardservice.datastores.GameDatastore;
import com.example.cardservice.events.AddDeckEvent;
import com.example.cardservice.exceptions.ConcurrentException;
import com.example.cardservice.exceptions.NotFoundException;
import com.example.cardservice.models.Game;
import com.example.cardservice.models.GameDeck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameDeckController {

    @Autowired
	private GameDatastore gameDatastore;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/games/{gameId}/decks")
    @Retryable(retryFor = ConcurrentException.class, maxAttempts = 2, backoff = @Backoff(delay = 10))
	public void addDeck(@PathVariable String gameId) throws NotFoundException, ConcurrentException {
		Game game = gameDatastore.findById(gameId);
        game.addDeck();
        gameDatastore.update(game);
        AddDeckEvent event = new AddDeckEvent(game);
		applicationEventPublisher.publishEvent(event);
	}

    @GetMapping("/games/{gameId}/decks")
	public GameDeck getUndealtCards(@PathVariable String gameId) throws NotFoundException {
		Game game = gameDatastore.findById(gameId);
        return game.getGameDeck();
	}
}
