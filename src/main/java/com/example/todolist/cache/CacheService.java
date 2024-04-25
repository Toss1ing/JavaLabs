package com.example.todolist.cache;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class CacheService<K, V> {
    private final HashMap<K, V> cache = new HashMap<>();

    private static final Integer MAX_SIZE = 2000;

    public void put(final K key, final V value) {
        if (cache.size() > MAX_SIZE) {
            cache.clear();
        }
        cache.put(key, value);
    }

    public boolean containsKey(final K key) {
        return cache.containsKey(key);
    }

    public void clear() {
        cache.clear();
    }

    public void remove(final K key) {
        cache.remove(key);
    }

    public V get(final K key) {
        return cache.get(key);
    }
}
