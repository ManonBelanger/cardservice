package com.example.cardservice.datastores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.cardservice.exceptions.ConcurrentException;
import com.example.cardservice.exceptions.NotFoundException;
import com.example.cardservice.models.Game;

@Repository
public class GameDatastore {
    private HashMap<String, Game> games = new HashMap<>();

    public void create(Game game) throws ConcurrentException {
        save(game.getId(), game, true);
    }

    public void update(Game game) throws ConcurrentException {
        save(game.getId(), game, false);
    }

    public Game findById(String id) throws NotFoundException {
        Game game = games.get(id);
        if (game == null) {
            throw new NotFoundException("Game not found");
        }
        return game;
    }

    public List<Game> findAll() {
        return new ArrayList<>(games.values());
    }

    public void delete(Game game) throws ConcurrentException {
        save(game.getId(), null, false);
    }

    private synchronized void save(String id, Game game, boolean isNew) throws ConcurrentException {
        // Check the currently stored version
        Game currentGame = games.get(id);
        if (isNew) {
            if (currentGame != null) {
                // It is a new game, so the id should not already be present in the datastore
                throw new ConcurrentException("Game already in datastore");
            }
            games.put(id, game);
        } else {
            if (currentGame == null) {
                // We expect the game to be there but it's not. It might have been deleted by another thread
                throw new ConcurrentException("Missing game in datastore");
            }
            if (game == null) {
                games.remove(id);
            } else {
                if (game.isSameVersion(currentGame)) {
                    currentGame.incrementVersion();
                    games.put(id, game);
                } else {
                    // The version changed, so the game was modified by another thread
                    throw new ConcurrentException("Unexpected version");
                }
            }
        }
    }
}
