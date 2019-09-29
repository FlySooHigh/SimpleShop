package org.flysoohigh.service.myinfo;

import org.flysoohigh.model.Customer;
import org.flysoohigh.service.ImportDataService;
import org.springframework.stereotype.Service;

@Service
public class MyInfoService implements IMyInfoService {
    private ImportDataService dataService;

    public MyInfoService(ImportDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public String showInfo(String currentUser) {
        if (currentUser.isEmpty()) {
            return "You are not logged in";
        }
        Customer customerInfo = dataService.getInfo(currentUser);
        return customerInfo.toString();
    }
}
