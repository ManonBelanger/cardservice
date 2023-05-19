package com.example.cardservice.models;

import java.util.Comparator;

public class PlayerInfoComparator implements Comparator<PlayerInfo> {
    @Override
    public int compare(PlayerInfo a, PlayerInfo b) {
        return b.getTotalCardValues() - a.getTotalCardValues();
    }
}
