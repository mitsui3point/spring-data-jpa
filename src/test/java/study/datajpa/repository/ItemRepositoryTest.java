package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void saveItemTest() {
        //given
        Item item = Item.builder().id("A").build();
        //when
        Item saveItem = itemRepository.save(item);
        //then
    }
}
