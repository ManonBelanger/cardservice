package com.example.cardservice.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class UuidGenerator {
    private Set<String> usedUuids = new HashSet<>();

    public synchronized String getUniqueUuid() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (usedUuids.contains(uuid));
        usedUuids.add(uuid);
        return uuid;
    } 
}
