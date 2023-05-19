package com.example.cardservice.controllers;

import java.util.List;
import java.util.Map;

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

import com.example.cardservice.datastores.GameDatastore;
import com.example.cardservice.events.AddPlayerEvent;
import com.example.cardservice.events.DealCardEvent;
import com.example.cardservice.events.RemovePlayerEvent;
import com.example.cardservice.exceptions.ConcurrentException;
import com.example.cardservice.exceptions.InvalidRequestException;
import com.example.cardservice.exceptions.NotFoundException;
import com.example.cardservice.models.Card;
import com.example.cardservice.models.Game;
import com.example.cardservice.models.Player;
import com.example.cardservice.models.PlayerInfo;
import com.example.cardservice.utils.UuidGenerator;

@RestController
public class PlayerController {

	@Autowired
	private GameDatastore gameDatastore;

	@Autowired
	private UuidGenerator uuidGenerator;

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/games/{gameId}/players")
	public List<PlayerInfo> getPlayers(@PathVariable String gameId) throws NotFoundException {
		Game game = gameDatastore.findById(gameId);
		return game.getPlayers();
	}

	@PostMapping("/games/{gameId}/players")
	@Retryable(retryFor = ConcurrentException.class, maxAttempts = 2, backoff = @Backoff(delay = 10))
	public Player newPlayer(@PathVariable String gameId, @RequestBody Map<String,String> input) throws NotFoundException, ConcurrentException {
		Game game = gameDatastore.findById(gameId);
		Player player = game.addPlayer(uuidGenerator.getUniqueUuid(), input.getOrDefault("name", "newPlayer"));
		gameDatastore.update(game);
		AddPlayerEvent event = new AddPlayerEvent(game, player.getId());
		applicationEventPublisher.publishEvent(event);
		return player;
	}

	@GetMapping("/games/{gameId}/players/{playerId}")
	public Player getPlayer(@PathVariable String gameId, @PathVariable String playerId) throws NotFoundException {
		Game game = gameDatastore.findById(gameId);
		return game.getPlayer(playerId);
	}

	@PostMapping("/games/{gameId}/players/{playerId}/dealcard")
	@Retryable(retryFor = ConcurrentException.class, maxAttempts = 2, backoff = @Backoff(delay = 10))
	public void dealCardToPlayer(@PathVariable String gameId, @PathVariable String playerId)
		throws NotFoundException, ConcurrentException, InvalidRequestException {

		Game game = gameDatastore.findById(gameId);
		Card card = game.dealCardToPlayer(playerId);
		gameDatastore.update(game);
		DealCardEvent event = new DealCardEvent(game, playerId, card);
		applicationEventPublisher.publishEvent(event);
	}

	@DeleteMapping("/games/{gameId}/players/{playerId}")
	@Retryable(retryFor = ConcurrentException.class, maxAttempts = 2, backoff = @Backoff(delay = 10))
	public void deletePlayer(@PathVariable String gameId, @PathVariable String playerId) throws NotFoundException, ConcurrentException {
		Game game = gameDatastore.findById(gameId);
		game.removePlayer(playerId);
		gameDatastore.update(game);
		RemovePlayerEvent event = new RemovePlayerEvent(game, playerId);
		applicationEventPublisher.publishEvent(event);
	}
}
