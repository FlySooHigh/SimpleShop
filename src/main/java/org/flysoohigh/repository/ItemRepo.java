package org.flysoohigh.repository;

import org.flysoohigh.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    boolean existsByItemName(String item);

    // Можно было бы добавить Optional, но здесь в этом нет смысла, так как в качестве item передаются только
    // реально существующие товары, а валидация несуществующих товаров происходит в BuyService и SellService
    Item findByItemName(String item);

    @Query(nativeQuery = true,
    value = "SELECT item_price " +
            "FROM item " +
            "WHERE item_name = :itemName")
    int getItemPriceByItemName(@Param(value = "itemName") String itemName);
}
