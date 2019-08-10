package org.flysoohigh.service.viewshop;

import org.flysoohigh.model.Item;
import org.flysoohigh.service.ImportDataService;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.IntStream;

public class ViewShopService implements IViewShopService {

    private PrintWriter out;
    private ImportDataService dataService;

    public ViewShopService(PrintWriter out, ImportDataService dataService) {
        this.out = out;
        this.dataService = dataService;
    }

    @Override
    public void showItems() {
            List<Item> allShopItems = dataService.getAllShopItems();
            IntStream.range(0, allShopItems.size())
                     .forEach(i -> out.println(allShopItems.get(i).toString()));
    }
}
