package org.flysoohigh.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ToString.Exclude
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "login_name")
    private String loginName;
    @Column(name = "available_funds")
    private int availableFunds;
    // FIXME: 03.08.2019 Разобраться с фетчем!
    @OneToMany(/*targetEntity = Item.class,*/ fetch = FetchType.EAGER, mappedBy = "customer")
//    @JoinColumn(name = "item_id")
    @Column(name = "bought_items")
    private List<Item> boughtItems;
    @ToString.Exclude
    @Column(name = "logged_in")
    private boolean loggedIn = false;
}
