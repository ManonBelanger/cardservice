package com.example.cardservice.datastores;

import com.example.cardservice.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class EventDatastore {
    HashMap<String, List<String>> events = new HashMap<>();

    public List<String> findById(String id) throws NotFoundException {
        List<String> list = events.get(id);
        if (list == null) {
            throw new NotFoundException("Entity not found");
        }
        return list;
    }

    public synchronized void save(String id, String message) {
        List<String> list = events.get(id);
        if (list == null) {
            list = new ArrayList<String>();
        }
        list.add(message);
        events.put(id, list);
    }
}
