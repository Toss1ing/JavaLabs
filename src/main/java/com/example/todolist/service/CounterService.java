package com.example.todolist.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private final AtomicInteger counter = new AtomicInteger(0);

    public int incrementCounter() {
        return counter.incrementAndGet();
    }

    public void reset() {
        counter.set(0);
    }

    public int get() {
        return counter.get();
    }
}
