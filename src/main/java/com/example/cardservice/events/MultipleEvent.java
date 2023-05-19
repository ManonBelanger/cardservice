package com.example.cardservice.events;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class MultipleEvent {
    protected List<SingleEvent> events = new ArrayList<>();
}
