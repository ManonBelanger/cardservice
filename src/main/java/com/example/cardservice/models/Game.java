package com.example.cardservice.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.example.cardservice.exceptions.InvalidRequestException;
import com.example.cardservice.exceptions.NotFoundException;

import lombok.Getter;

public class Game{
    @Getter
    private String id;
    @Getter
    private String name;

    HashMap<String, Player> players = new HashMap<>();

    @Getter
    GameDeck gameDeck;

    // For optimistic locking
    private int version;

    public Game(String id, String deckId, String name) {
        this.id = id;
        this.name = name;
        this.gameDeck = new GameDeck(deckId);
        this.version = 1;
    }

    public Player addPlayer(String id, String name) {
        Player newPlayer = new Player(id, name);
        players.put(id, newPlayer);
        return newPlayer;
    }

    public void removePlayer(String playerId) throws NotFoundException {
        Player removedPlayer = players.remove(playerId);
        if (removedPlayer == null) {
            throw new NotFoundException("Player not found");
        }
    }

    public List<PlayerInfo> getPlayers() {
        List<PlayerInfo> list = new ArrayList<>();
        for (Player player : players.values()) {
            list.add(new PlayerInfo(player));
        }
        Collections.sort(list, new PlayerInfoComparator());
        return list;
    }

    public Player getPlayer(String playerId) throws NotFoundException {
        Player player = players.get(playerId);
        if (player == null) {
            throw new NotFoundException("Player not found");
        }
        return player;
    }

    public Card dealCardToPlayer(String playerId) throws NotFoundException, InvalidRequestException {
        Player player = getPlayer(playerId);
        Card card = gameDeck.dealCard();
        player.addCard(card);
        return card;
    }

    public void addDeck() {
        gameDeck.addDeck();
    }

    public boolean isSameVersion(Game otherGame) {
        return this.version == otherGame.version;
    }

    public void incrementVersion() {
        version++;
    }
}