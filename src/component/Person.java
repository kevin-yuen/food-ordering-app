package component;

import controller.ServerController;

import java.util.ArrayList;
import java.util.HashMap;

public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void viewMenu(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap, ServerController serverController) {
        serverController.renderMenuView(foodHashMap);
    }
}
