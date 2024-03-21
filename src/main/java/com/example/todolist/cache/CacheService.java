package com.example.todolist.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class CacheService<Type1, Type2> {
    HashMap<Type1, Type2> cache = new HashMap<Type1, Type2>();
    private final Integer capacity = 300;

    public void put(Type1 key, Type2 value){
        cache.put(key, value);
    }

    public void clear(){
        cache.clear();
    }

    public boolean containsKey(Type1 key){
        return cache.containsKey(key);
    }

    public void remove(Type1 key){
        cache.remove(key);
    }

    public Type2 get(Type1 key){
        return cache.get(key);
    }
}
