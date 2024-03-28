package com.example.todolist.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class CacheService<K, V> {
    HashMap<K,V> cache = new HashMap<>();

    public void put(K key, V value){
        cache.put(key, value);
    }

    public boolean containsKey(K key){ 
        return cache.containsKey(key);
    }

    public void clear(){ 
        cache.clear();
    }

    public void remove(K key){
        cache.remove(key);
    }

    public V get(K key){
        return cache.get(key);
    }
}
