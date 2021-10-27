package com.example.unittest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    @Override
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    @Override
    public Item findOneByName(String name) {
        return itemRepository.findOneByName(name);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
