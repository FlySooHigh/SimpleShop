package org.flysoohigh.service.myinfo;

import org.flysoohigh.model.Customer;
import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;

public class MyInfoService implements IMyInfoService {

    private PrintWriter out;
    private ImportDataService dataService;

    public MyInfoService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void showInfo(String currentUser) {
        if (currentUser.isEmpty()) {
            out.println("You are not logged in");
            return;
        }
        Customer customerInfo = dataService.getInfo(currentUser);
        out.println(customerInfo.toString());
    }
}
