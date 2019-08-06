package org.flysoohigh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    // @ManyToMany позволяем одному игроку покупать несколько одинаковых предметов, если сделать @OneToMany,
    // то при попытке купить второй такой же предмет летит эксепшен из-за нарушения уникальности ключа
    @ManyToMany(/*targetEntity = Item.class,*/ fetch = FetchType.EAGER/*, mappedBy = "customer"*/)
//    @JoinColumn(name = "item_id")
    @Column(name = "bought_items")
    private List<Item> boughtItems;
    @ToString.Exclude
    @Column(name = "logged_in")
    private boolean loggedIn = false;
}
