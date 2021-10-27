package com.example.unittest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IntegrationTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    public void 상품등록_통합테스트() throws Exception {
        //given
        Item item = Item.builder()
                .name("피자")
                .price(10000).build();

        //when
        //itemRepository.saveItem(item);
        itemRepository.save(item);

        //then
        assertEquals(10000, itemRepository.findOneByName("피자").getPrice());
    }
}
