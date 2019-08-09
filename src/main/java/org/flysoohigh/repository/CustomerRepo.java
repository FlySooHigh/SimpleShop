package org.flysoohigh.repository;

import org.flysoohigh.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    // FIXME: 04.08.2019 Наверно можно упростить через распознование имени метода самим Spring Data
    @Query(nativeQuery = true,
    value = "SELECT COUNT(*) = 1 " +
            "FROM customer c " +
            "WHERE c.login_name = :loginName")
    boolean existsByLoginName(@Param("loginName") String loginName);

    // FIXME: 04.08.2019 Наверно можно упростить через распознование имени метода самим Spring Data
    @Query(nativeQuery = true,
    value = "SELECT c.logged_in " +
            "FROM customer c " +
            "WHERE c.login_name = :loginName")
    boolean isLoggedIn(@Param("loginName") String loginName);

    @Modifying
    @Query(nativeQuery = true,
    value = "UPDATE customer c " +
            "SET logged_in = TRUE " +
            "WHERE c.login_name = :loginName")
    void logIn(@Param("loginName") String loginName);

    @Modifying
    @Query(nativeQuery = true,
    value = "UPDATE customer c " +
            "SET logged_in = FALSE " +
            "WHERE c.login_name = :loginName")
    void logOut(@Param("loginName") String loginName);

    // FIXME: 04.08.2019 Сделать Optional ?
    Customer findByLoginName(String loggedInCustomer);

    @Query(nativeQuery = true,
    value = "SELECT COUNT(*) >= 1 " +
            "FROM customer_item " +
            "WHERE customer_customer_id = :customerId AND " +
                  "boughtitems_item_id = :itemId")
    boolean isBoughtItem(@Param("customerId") Long customerId, @Param("itemId") Long itemId);
}
