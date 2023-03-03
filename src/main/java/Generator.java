import net.datafaker.Faker;

public class Generator {

    static Faker faker = new Faker();

    public static User getRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.password().toString();
        String name = faker.name().firstName();

        return new User(email, password, name);
    }

    public static UserCredentials getRandomUserCredentials() {
        String email = faker.internet().emailAddress();
        String password = faker.password().toString();

        return new UserCredentials(email, password);
    }

    public static User getDefault(String email, String password, String name) {
        return new User(email, password, name);
    }
}
