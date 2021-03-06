package com.example.unittest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ItemMockingUnitTest {

    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepo;

    @DisplayName("상품을 등록한지 검증한다")
    @Test
    public void 상품등록() throws Exception {
        //given
        Item item = Item.builder()
                .name("피자")
                .price(10000).build();

        given(itemRepo.findOneByName("피자"))
                .willReturn(item);
//        Mockito.when(itemRepo.findOneByName("피자"))
//                .thenReturn(item);

        //when
        itemService.saveItem(item);

        //then
        verify(itemRepo).save(item);            // 해당 메서드가 실제로 호출되었나?
        //verify(itemRepo).findAll();
        assertEquals(10000, itemService.findOneByName("피자").getPrice());
    }
}
