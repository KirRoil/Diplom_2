import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class CreateWithEmptyFieldTest {
    private UserClient userClient;
    private Response createNullField;

    @DataProvider
    public static Object[][] getData() {
        return new Object[][]{
                {"", "123456", "qwerty"},
                {"test@test.ru", "", "qwerty"},
                {"test@test.ru", "123456", ""},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @UseDataProvider("getData")
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей.")
    public void createWithNullFieldTest(String email, String password, String name) {
        User user = Generator.getDefault(email, password, name);
        createNullField = userClient.createUser(user);
        int statusCode = createNullField.getStatusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
        String message = createNullField.jsonPath().getString("message");
        assertEquals("Email, password and name are required fields", message);
    }

    @After
    public void tearDown() {
        String accessToken = createNullField.body().jsonPath().getString("accessToken");
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}