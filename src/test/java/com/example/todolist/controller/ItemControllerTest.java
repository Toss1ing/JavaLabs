package com.example.todolist.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Item;
import com.example.todolist.service.ItemService;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    Item item = new Item();

    @BeforeEach
    public void setUp() {
        item.setId(1);
        item.setDescription("Description");
        item.setNameTask("ItemName");
        item.setModifierDate(Instant.now());
        item.setCompletionDate(Instant.now());
        item.setCreatedDate(Instant.now());
        item.setComplete(false);
    }

    @Test
    @SuppressWarnings("null")
    public void getToDoItemsTest() throws ObjectNotFoundException {
        when(itemService.getToDoItems()).thenReturn(Arrays.asList(item, item));

        ResponseEntity<List<Item>> result = itemController.getToDoItems();

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody().size(), 2);
        assertEquals(result.getBody().get(0), item);
    }

    @Test
    public void getToDoItemByIdTest() throws ObjectNotFoundException {
        when(itemService.getToDoItemById(1)).thenReturn(item);

        ResponseEntity<Item> result = itemController.getToDoItemById(1);

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), item);
    }

    @Test
    @SuppressWarnings("null")
    public void getToDoItemByWordTest() throws ObjectNotFoundException {
        when(itemService.getToDoItemByWord("Description")).thenReturn(Arrays.asList(item, item));

        ResponseEntity<List<Item>> result = itemController.getToDoItemByWord("Description");

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody().get(0), item);
        assertEquals(result.getBody().size(), 2);
    }

    @Test
    public void getToDoItemByNameTest() throws ObjectNotFoundException {
        when(itemService.getToDoItemByName("ItemName")).thenReturn(item);

        ResponseEntity<Item> result = itemController.getToDoItemByName("ItemName");

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), item);
    }

    @Test
    public void addToDoItemTest() throws ObjectExistException {
        when(itemService.save(item)).thenReturn(item);

        ResponseEntity<Item> result = itemController.addToDoItem(item);

        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        assertEquals(result.getBody(), item);
    }

    @Test
    public void deleteToDoItemByIdTest() throws BadRequestException {
        when(itemService.deleteToDoItemById(1)).thenReturn(item);

        ResponseEntity<Item> result = itemController.deleteToDoItemById(1);

        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
        assertEquals(result.getBody(), item);
    }

    @Test
    public void completeTaskByIdTest() throws BadRequestException {
        when(itemService.completeTaskById(1)).thenReturn(item);

        ResponseEntity<Item> result = itemController.completeTaskById(1);

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), item);
    }

}
