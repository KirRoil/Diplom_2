import java.util.ArrayList;

public class Order {

    private ArrayList<Object> ingredients;
    private static String bunR2D3 = "61c0c5a71d1f82001bdaaa6d";
    private static String meatImmortal = "61c0c5a71d1f82001bdaaa6f";

    public Order(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getDefaultOrder() {
        ArrayList<Object> order = new ArrayList<>();
        order.add(bunR2D3);
        order.add(meatImmortal);

        return new Order(order);
    }

    public static Order getOrderIncorrectHash() {
        ArrayList<Object> order = new ArrayList<>();
        order.add("aswxa123");
        order.add("4356");

        return new Order(order);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients=" + ingredients +
                '}';
    }

}



