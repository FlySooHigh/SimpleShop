package org.flysoohigh.repository;

import org.flysoohigh.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    boolean existsByItemName(String item);

    // FIXME: 04.08.2019 Добавить Optional
    Item findByItemName(String item);

//    boolean existsByItemNameAndCustomersCustomerId(String itemName, Long customerId);
}
