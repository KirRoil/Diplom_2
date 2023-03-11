import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetUserOrdersTest {

    private User user;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String accessToken;

    private final String orderErrorMessage = "You should be authorised";

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        User user = Generator.getRandomUser();
        userClient.createUser(user);
        Response loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.body().jsonPath().getString("accessToken");
        orderClient = new OrderClient();
        orderClient.orderCreate(Order.getDefaultOrder(), accessToken);
    }


    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersAuthUserTest() {
        Response response = orderClient.getOrderList(accessToken);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_OK, statusCode);

        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertTrue(success);

        List<Object> body = response.jsonPath().getList("orders");
        Assert.assertFalse("Body is empty", body.isEmpty());
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    public void getOrdersWithoutAuthTest() {
        Response response = orderClient.getOrderList("");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(SC_UNAUTHORIZED, statusCode);

        boolean success = response.jsonPath().getBoolean("success");
        Assert.assertFalse(success);

        String message = response.jsonPath().getString("message");
        Assert.assertEquals(orderErrorMessage, message);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}