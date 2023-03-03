import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private User user;
    private UserClient userClient;
    private String accessToken;

    private static final String errorNullIngredients = "Ingredient ids must be provided";

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        User user = Generator.getRandomUser();
        userClient.createUser(user);
        Response responseLogin = userClient.login(UserCredentials.from(user));
        accessToken = responseLogin.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthTest() {
        order = Order.getDefaultOrder();
        Response orderResponse = orderClient.orderCreate(order, accessToken);
        int statusCode = orderResponse.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);
        boolean orderCreate = orderResponse.jsonPath().getBoolean("success");
        Assert.assertTrue(orderCreate);
        int orderNumber = orderResponse.jsonPath().getInt("order.number");
        Assert.assertNotEquals(0, orderNumber);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void creatOrderWithoutAuthTest() {
        order = Order.getDefaultOrder();
        Response response = orderClient.orderCreate(order, "");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);
        boolean orderCreate = response.jsonPath().getBoolean("success");
        Assert.assertTrue(orderCreate);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(null);
        Response response = orderClient.orderCreate(order, "accessToken");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_BAD_REQUEST, statusCode);
        boolean orderCreate = response.jsonPath().getBoolean("success");
        Assert.assertFalse(orderCreate);
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(errorNullIngredients, message);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientsHashTest() {
        order = Order.getOrderIncorrectHash();
        Response response = orderClient.orderCreate(order, accessToken);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}