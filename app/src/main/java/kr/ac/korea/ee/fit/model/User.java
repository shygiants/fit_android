package kr.ac.korea.ee.fit.model;

/**
 * Created by SHYBook_Air on 15. 5. 4..
 */
public class User {

    String email;

    static User user;

    public static User get() {
        if (user != null)
            return user;
        createUser("NULL");
        return user;
    }

    public static void createUser(String email) {
        user = new User();
        user.email = email;
    }

    public String getEmail() {
        return email;
    }

}
