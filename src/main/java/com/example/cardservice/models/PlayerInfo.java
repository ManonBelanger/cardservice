package com.example.cardservice.models;

import lombok.Getter;

@Getter
public class PlayerInfo {
    private String id;
    private String name;
    private Integer totalCardValues;

    public PlayerInfo(Player player) {
        id = player.getId();
        name = player.getName();

        totalCardValues = 0;
        for (Card card : player.getCards()) {
            totalCardValues += card.realValue();
        }
    }
}
