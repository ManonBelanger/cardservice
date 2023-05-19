package com.example.cardservice.events;

import lombok.Getter;

@Getter
public class SingleEvent{
    private final String entityId;
    private final String message;

    public SingleEvent(String entityId, String message) {
        this.entityId = entityId;
        this.message = message;
    }
}
