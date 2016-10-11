package kr.enterkey.accounts;

/**
 * Created by enterkey88 on 2016-10-11.
 */
public class AccountNotFoundException extends RuntimeException {
    Long id;

    public AccountNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}