package com.example.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Item;
import com.example.todolist.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CacheService<Integer, Optional<Item>> cacheService;

    @InjectMocks
    private ItemService itemService;

    private Item item = new Item();

    @BeforeEach
    void setUp() {
        item.setId(1);
        item.setCompletionDate(Instant.now());
        item.setComplete(false);
        item.setCreatedDate(Instant.now());
        item.setDescription("Description");
        item.setModifierDate(Instant.now());
        item.setNameTask("nameTask");
    }

    @Test
    void getToDoItemsTest() throws ObjectNotFoundException {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item, item));

        List<Item> result = itemService.getToDoItems();

        assertEquals(2, result.size());
        assertEquals(result.get(0), item);
    }

    @Test
    void getToDoItemsTest_Throw() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList());

        List<Item> result = itemRepository.findAll();

        assertTrue(result.isEmpty());
        assertThrows(ObjectNotFoundException.class, () -> itemService.getToDoItems());
    }

    @Test
    void getToDoItemByIdRepositoryTest() throws ObjectNotFoundException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(false);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Item result = itemService.getToDoItemById(1);

        assertEquals(result, item);
        verify(cacheService, times(1)).put(hash, Optional.of(item));
    }

    @Test
    void getToDoItemByIdCacheTest() throws ObjectNotFoundException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(true);
        when(cacheService.get(hash)).thenReturn(Optional.of(item));

        Item result = itemService.getToDoItemById(1);

        assertEquals(result, item);
        verify(cacheService, times(1)).get(hash);
    }

    @Test
    void getToDoItemByIdTest_Throw() {
        when(itemRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.getToDoItemById(2));
    }

    @Test
    void getToDoItemByNameTest() throws ObjectNotFoundException {
        when(itemRepository.findByName("nameTask")).thenReturn(Optional.of(item));

        Item result = itemService.getToDoItemByName("nameTask");

        assertEquals(result, item);
    }

    @Test
    void getToDoItemByNameTest_Throw() {
        when(itemRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> itemService.getToDoItemByName("hello"));
    }

    @Test
    void saveTest() throws ObjectExistException {
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.save(item);

        assertEquals(item, result);
    }

    @Test
    void deleteToDoItemByIdRepositoryTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        doNothing().when(itemRepository).deleteById(1);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(cacheService.containsKey(hash)).thenReturn(false);

        Item result = itemService.deleteToDoItemById(1);

        assertEquals(result, item);
    }

    @Test
    void deleteToDoItemByIdCacheTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        doNothing().when(itemRepository).deleteById(1);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(cacheService.containsKey(hash)).thenReturn(true);

        Item result = itemService.deleteToDoItemById(1);

        assertEquals(result, item);
        verify(cacheService, times(1)).remove(hash);
    }

    @Test
    void deleteToDoItemById_Throw() {
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> itemService.deleteToDoItemById(1));
    }

    @Test
    void completeTaskByIdRepositoryTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(cacheService.containsKey(hash)).thenReturn(false);

        Item result = itemService.completeTaskById(1);

        assertEquals(true, result.isComplete());
        verify(cacheService, times(1)).put(hash, Optional.of(item));
    }

    @Test
    void completeTaskByIdCacheTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(true);
        when(cacheService.get(hash)).thenReturn(Optional.of(item));

        Item result = itemService.completeTaskById(1);

        assertEquals(true, result.isComplete());
        verify(cacheService, times(1)).remove(hash);
        verify(cacheService, times(1)).put(hash, Optional.of(item));

    }

    @Test
    void completeTaskByIdTest_Throw() {
        Integer hash = Objects.hash(2);
        when(cacheService.containsKey(hash)).thenReturn(false);
        when(itemRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> itemService.completeTaskById(2));
    }

    @Test
    void getToDoItemByWord() throws ObjectNotFoundException {
        when(itemRepository.findByDescriptionTask("Description")).thenReturn(Arrays.asList(item, item));

        List<Item> result = itemService.getToDoItemByWord("Description");

        assertEquals(2, result.size());
        assertEquals(result.get(0), item);
    }

    @Test
    void getToDoItemByWordTest_Throw() {
        when(itemRepository.findByDescriptionTask(any(String.class))).thenReturn(Arrays.asList());

        assertThrows(ObjectNotFoundException.class, () -> itemService.getToDoItemByWord("asdf"));
    }
}
