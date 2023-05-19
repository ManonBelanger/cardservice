package com.example.cardservice.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Player {
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }
}
