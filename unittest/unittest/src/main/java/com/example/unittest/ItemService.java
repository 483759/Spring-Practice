package com.example.unittest;

import org.springframework.stereotype.Service;

import java.util.List;

public interface ItemService {
    void saveItem(Item item);
    Item findOne(Long id);
    Item findOneByName(String name);
    List<Item> findAll();
}
