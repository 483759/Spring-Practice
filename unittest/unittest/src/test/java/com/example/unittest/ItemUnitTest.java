package com.example.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ItemUnitTest {

    @DisplayName("상품을 등록한지 검증한다")
    @Test
    public void 상품등록() throws Exception {
        //given
        Item item = Item.builder()
                .name("피자")
                .price(10000).build();
        ItemRepository itemRepo = Mockito.mock(ItemRepository.class);
        Mockito.when(itemRepo.findOneByName("피자"))
                .thenReturn(Item.builder()
                    .name("피자")
                    .price(10000).build());
        ItemService itemService = new ItemServiceImpl(itemRepo);

        //when
        itemService.saveItem(item);

        //then
        assertEquals(10000, itemService.findOneByName("피자").getPrice());
    }
}
