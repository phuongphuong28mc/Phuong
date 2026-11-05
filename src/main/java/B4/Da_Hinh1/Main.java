
package B4.Da_Hinh1;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Car("C01", "Toyota", 120, 4));
        vehicles.add(new Motorbike("M01", "Honda", 80, 125));
        vehicles.add(new Bicycle("B01", "Giant", 20, true));

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            vehicle.displayInfo();
            vehicle.move();
            System.out.println();
        }
    }
}
