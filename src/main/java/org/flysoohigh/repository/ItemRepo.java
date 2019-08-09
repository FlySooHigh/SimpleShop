package org.flysoohigh.repository;

import org.flysoohigh.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    boolean existsByItemName(String item);

    // FIXME: 04.08.2019 Добавить Optional ?
    Item findByItemName(String item);

    @Query(nativeQuery = true,
    value = "SELECT item_price " +
            "FROM item " +
            "WHERE item_name = :itemName")
    int getItemPriceByItemName(@Param(value = "itemName") String itemName);
}
