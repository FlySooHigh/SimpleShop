package org.flysoohigh.service;

import lombok.Getter;
import org.flysoohigh.model.Customer;
import org.flysoohigh.model.Item;
import org.flysoohigh.model.Items;
import org.flysoohigh.repository.CustomerRepo;
import org.flysoohigh.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

@Getter
@Component
public class ImportDataService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public void parseAndSaveItemsXml() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Items.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Items items = (Items) unmarshaller.unmarshal(new File("src\\main\\resources\\items.xml"));
        for (Item item: items.getItems() ) {
            itemRepo.save(item);
        }
    }

    public void saveSomeCustomers(){
        Customer zidan = new Customer(null, "zidan", 100, null, false);
        Customer ovechkin = new Customer(null, "ovechkin", 50, null, false);
        Customer vinnipuh = new Customer(null, "vinni", 5, null, false);

        customerRepo.save(zidan);
        customerRepo.save(ovechkin);
        customerRepo.save(vinnipuh);
    }

    public boolean isCustomer(String loginName) {
        return customerRepo.existsByLoginName(loginName);
    }

    public boolean isLoggedIn(String loginName) {
        return customerRepo.isLoggedIn(loginName);
    }

    public void test() {
        List<Customer> all = customerRepo.findAll();
        for (Customer customer:all) {
            System.out.println(customer.toString());
        }
    }

    @Transactional
    public void logIn(String loginName) {
        customerRepo.logIn(loginName);
    }

    @Transactional
    public void logOut(String loggedInCustomer) {
        customerRepo.logOut(loggedInCustomer);
    }

    public Customer getInfo(String loggedInCustomer) {
        return customerRepo.findByLoginName(loggedInCustomer);
    }

    public boolean isInTheList(String item) {
        return itemRepo.existsByItemName(item);
    }

    public int getCustomerFunds(String loggedInCustomer) {
        Customer byLoginName = customerRepo.findByLoginName(loggedInCustomer);
        return byLoginName.getAvailableFunds();
    }

    public int getItemPrice(String item) {
        Item byItemName = itemRepo.findByItemName(item);
        return byItemName.getItemPrice();
    }

    @Transactional
    public void buyItem(String loggedInCustomer, String itemName) {
        Customer customer = customerRepo.findByLoginName(loggedInCustomer);
        Item item = itemRepo.findByItemName(itemName);
//        item.getCustomers().add(customer);
//        item.setCustomer(customer);
//        itemRepo.save(item);
        customer.setAvailableFunds(customer.getAvailableFunds() - item.getItemPrice());
        customer.getBoughtItems().add(item);
        customerRepo.save(customer);
    }

    public boolean isInTheCustomerList(String loggedInCustomer, String itemName) {
        Customer byLoginName = customerRepo.findByLoginName(loggedInCustomer);
        Long customerId = byLoginName.getCustomerId();
        Item item = itemRepo.findByItemName(itemName);
        Long itemId = item.getItemId();
//        return itemRepo.existsByItemNameAndCustomersCustomerId(itemName, customerId);
        return customerRepo.isBoughtItem(customerId, itemId);
//        return itemRepo.existsByItemNameAndCustomersCustomerId(itemName, customerId);
    }

    @Transactional
    public void sellItem(String loggedInCustomer, String itemName) {
        Item item = itemRepo.findByItemName(itemName);
        int itemPrice = item.getItemPrice();
//        item.setCustomer(null);

        Customer customer = customerRepo.findByLoginName(loggedInCustomer);
        customer.setAvailableFunds(customer.getAvailableFunds() + itemPrice);
        customer.getBoughtItems().remove(item);
        customerRepo.save(customer);

//        item.getCustomers().remove(customer);
//        itemRepo.save(item);
    }
}
