package moving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import moving.domain.Item;

public class Main {

    public static void main(String[] args) {
        // test your program here
        
        List<Item> items = new ArrayList<Item>();
    items.add(new Item("passport", 2));
    items.add(new Item("toothbrash", 1));
    items.add(new Item("circular saw", 100));

    //Collections.sort(items);
    System.out.println(items);
    }
}
