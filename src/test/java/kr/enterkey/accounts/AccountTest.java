package kr.enterkey.accounts;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by enterkey88 on 2016-10-11.
 */
public class AccountTest {

    @Test
    public void getterSetter() {
        Account account = new Account();
        account.setUsername("enterkey");
        account.setPassword("entjd123");
        assertThat(account.getUsername(), is("enterkey"));
    }
}
