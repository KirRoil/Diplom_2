import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private final String ORDERS = "orders";

    @Step("Создание заказа")
    public Response orderCreate(Order order, String token) {
        return (Response) given()
                .spec(getBaseSpecSettings())
                .headers("Authorization", token)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract();
    }

    @Step("Получение списка заказов")
    public Response getOrderList(String token) {
        return (Response) given()
                .spec(getBaseSpecSettings())
                .header("authorization", token)
                .when()
                .get(ORDERS)
                .then()
                .extract();
    }
}
