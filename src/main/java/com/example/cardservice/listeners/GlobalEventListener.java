package com.example.cardservice.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.cardservice.datastores.EventDatastore;
import com.example.cardservice.events.MultipleEvent;
import com.example.cardservice.events.SingleEvent;

@Component
public class GlobalEventListener {
    
    @Autowired
	private EventDatastore datastore;
    
    @EventListener
    void handleSingleEvent(SingleEvent event) {
        datastore.save(event.getEntityId(), event.getMessage());
    }

    @EventListener
    void handleMultipleEvent(MultipleEvent events) {
        for(SingleEvent event : events.getEvents()) {
            datastore.save(event.getEntityId(), event.getMessage());
        }
    }
}
