package com.example.cardservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Card {
    private Integer value;
    @NonNull
    @Getter
    private Suit suit;

    public String getValue() {
        switch (value) {
            case 1:
                return "A";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
        }
        return value.toString();
    }

    public Integer realValue() {
        return value;
    }
}