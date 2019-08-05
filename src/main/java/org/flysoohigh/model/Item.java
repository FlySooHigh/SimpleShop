package org.flysoohigh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@Entity
@Table(name = "item")
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ToString.Exclude
    @Column(name = "item_id")
    private Long itemId;
    @XmlElement(name = "name")
    @Column(name = "item_name")
    private String itemName;
    @XmlElement(name = "price")
    @Column(name = "item_price")
    private int itemPrice;
    @ManyToOne/*(targetEntity = Customer.class)*/
    private Customer customer;
}
