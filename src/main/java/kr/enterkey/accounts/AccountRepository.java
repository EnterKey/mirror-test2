package kr.enterkey.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by enterkey88 on 2016-10-11.
 */
public interface AccountRepository extends JpaRepository<Account, Long>{
    Account findByUsername(String username);
}
