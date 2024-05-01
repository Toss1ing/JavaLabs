package com.example.todolist.service;

import static com.example.todolist.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.todolist.utilities.Constants.NOT_FOUND_MSG;
import static com.example.todolist.utilities.Constants.OBJECT_EXIST_MSG;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Item;
import com.example.todolist.repository.ItemRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final CacheService<Integer, Optional<Item>> cacheService;

    private static final Integer ALL_CONTAINS = 1111;

    private void updateCacheService() {
        if (!cacheService.containsKey(ALL_CONTAINS)) {
            List<Item> items = itemRepository.findAll();
            for (Item item : items) {
                if (cacheService.containsKey(item.getId())) {
                    int hash = Objects.hash(item.getId());
                    cacheService.put(hash, Optional.of(item));
                }
            }
            cacheService.put(ALL_CONTAINS, null);
        }
    }

    public List<Item> getToDoItems() throws ObjectNotFoundException {
        updateCacheService();
        List<Item> items = itemRepository.findAll();
        if (items.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return itemRepository.findAll();
    }

    public Item getToDoItemById(final Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<Item> item;
        if (cacheService.containsKey(hash)) {
            item = cacheService.get(hash);
        } else {
            item = itemRepository.findById(id);
            cacheService.put(hash, item);
        }
        if (item.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }

        return item.get();
    }

    public Item getToDoItemByName(final String taskName) throws ObjectNotFoundException {
        Optional<Item> item = itemRepository.findByName(taskName);
        if (item.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return item.get();
    }

    public Item save(final Item toDoItem) throws ObjectExistException {
        Optional<Item> item = itemRepository.findByName(toDoItem.getNameTask());
        if (item.isPresent()) {
            throw new ObjectExistException(OBJECT_EXIST_MSG);
        }
        if (toDoItem.getUsers() == null) {
            toDoItem.setUsers(new ArrayList<>());
        }
        Integer hash = Objects.hash(toDoItem.getId());
        cacheService.put(hash, item);
        return itemRepository.save(toDoItem);
    }

    public Item deleteToDoItemById(final Integer id) throws BadRequestException {

        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        Integer hash = Objects.hashCode(id);
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        itemRepository.deleteById(id);
        return item.get();
    }

    public void deleteAllItem() {
        cacheService.clear();
        itemRepository.deleteAll();
    }

    public Item completeTaskById(final Integer taskId) throws BadRequestException {
        Optional<Item> item;
        Integer hash = Objects.hash(taskId);
        if (cacheService.containsKey(hash)) {
            item = cacheService.get(hash);
        } else {
            item = itemRepository.findById(taskId);
        }
        if (item.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        item.get().setComplete(true);
        item.get().setCompletionDate(Instant.now());
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        cacheService.put(hash, item);
        itemRepository.save(item.get());
        return item.get();
    }

    public List<Item> getToDoItemByWord(final String keyWord) throws ObjectNotFoundException {
        List<Item> listItemByWord = itemRepository.findByDescriptionTask(keyWord);
        if (listItemByWord.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return listItemByWord;
    }
}
