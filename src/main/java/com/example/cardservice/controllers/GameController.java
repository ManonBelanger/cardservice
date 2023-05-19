package com.example.cardservice.controllers;

import java.util.List;
import java.util.Map;

import com.example.cardservice.datastores.GameDatastore;
import com.example.cardservice.events.CreateGameEvent;
import com.example.cardservice.events.DeleteGameEvent;
import com.example.cardservice.exceptions.ConcurrentException;
import com.example.cardservice.exceptions.NotFoundException;
import com.example.cardservice.models.Game;
import com.example.cardservice.utils.UuidGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GameController {

	@Autowired
	private GameDatastore datastore;

	@Autowired
	private UuidGenerator uuidGenerator;

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping("/games")
	public List<Game> listGames() {
		return datastore.findAll();
	}

	@GetMapping("/games/{id}")
	public Game getGame(@PathVariable String id) throws NotFoundException {
		return datastore.findById(id);
	}

	@PostMapping("/games")
	@Retryable(retryFor = ConcurrentException.class, maxAttempts = 2, backoff = @Backoff(delay = 10))
	public Game newGame(@RequestBody Map<String,String> input) throws ConcurrentException {
		Game newGame = new Game(uuidGenerator.getUniqueUuid(), uuidGenerator.getUniqueUuid(),
			input.getOrDefault("name", "newGame"));
		datastore.create(newGame);
		CreateGameEvent event = new CreateGameEvent(newGame);
		applicationEventPublisher.publishEvent(event);
		return newGame;
	}

	@DeleteMapping("/games/{id}")
	public void deleteGame(@PathVariable String id) throws NotFoundException, ConcurrentException {
		Game gameToDelete = datastore.findById(id);
		datastore.delete(gameToDelete);
		DeleteGameEvent event = new DeleteGameEvent(gameToDelete);
		applicationEventPublisher.publishEvent(event);
	}

}