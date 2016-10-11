package kr.enterkey.accounts;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by enterkey88 on 2016-10-11.
 */
@Service
@Transactional
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);

        String username = dto.getUsername();
        if(accountRepository.findByUsername(username) != null) {
            log.error("user duplicated exception. {}", username);
            throw new UserDuplicatedException(username);
        }

        // TODO PASSWORD 파싱
        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, AccountDto.Update updateDto) {
        Account account = getAccount(id);
        account.setPassword(updateDto.getPassword());
        account.setFullName(updateDto.getFullName());
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        Account account = accountRepository.findOne(id);
        if(account == null) {
            throw new AccountNotFoundException(id);
        }

        return account;
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(id);
    }
}
