package kr.enterkey.accounts;

/**
 * Created by enterkey88 on 2016-10-11.
 */
public class UserDuplicatedException extends RuntimeException {

    String username;

    public UserDuplicatedException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
