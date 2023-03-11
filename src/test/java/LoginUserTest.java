import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private final String loginErrorMessage = "email or password are incorrect";

    @Before
    public void setUp() {
        user = Generator.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userSuccesLoginTest() {
        Response loginResponse = userClient.login(UserCredentials.from(user));

        int statusCode = loginResponse.getStatusCode();
        assertEquals(SC_OK, statusCode);

        boolean succesLogin = loginResponse.body().jsonPath().getBoolean("success");
        assertTrue(succesLogin);

        accessToken = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(accessToken);

        String refreshToken = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("логин с неверным логином и паролем")
    public void loginWithInvalidFieldTest() {
        Response loginResponse = userClient.login(Generator.getRandomUserCredentials());

        int statusCode = loginResponse.getStatusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);

        String message = loginResponse.body().jsonPath().getString("message");
        assertEquals(loginErrorMessage, message);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}