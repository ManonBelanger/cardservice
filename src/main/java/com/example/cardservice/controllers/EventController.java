package com.example.cardservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.cardservice.datastores.EventDatastore;
import com.example.cardservice.exceptions.NotFoundException;

@RestController
public class EventController {

    @Autowired
	private EventDatastore datastore;

    @GetMapping("/events/{id}")
	public List<String> getEvents(@PathVariable String id) throws NotFoundException {
		return datastore.findById(id);
	}
}
