package kr.enterkey.accounts;

import kr.enterkey.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by enterkey88 on 2016-10-11.
 */
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(value="/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(
            @RequestBody @Valid AccountDto.Create create,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            // TODO BindingResult 안에 들어있는 에러 정보 사용하기.
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount = accountService.createAccount(create);
        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @RequestMapping(value="/accounts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PageImpl<AccountDto.Response> getAccounts(Pageable pageable) {
        Page<Account> page = accountRepository.findAll(pageable);
        List<AccountDto.Response> content = page.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDto.Response.class)).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @RequestMapping(value="/accounts/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public AccountDto.Response getAccount(
            @PathVariable Long id
    ) {
        Account account = accountService.getAccount(id);
        return modelMapper.map(account, AccountDto.Response.class);
    }

    @RequestMapping(value="/accounts/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateAccount(
            @PathVariable Long id,
            @RequestBody @Valid AccountDto.Update updateDto,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account updateAccount = accountService.updateAccount(id, updateDto);
        return new ResponseEntity<>(modelMapper.map(updateAccount, AccountDto.Update.class), HttpStatus.OK);
    }

    @RequestMapping(value="/accounts/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAccount(
            @PathVariable Long id
    ) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[ " +e.getUsername() + " ] 중복된 username 입니다." );
        errorResponse.setCode("duplicated.username.exception");
        return errorResponse;
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[ " +e.getId() + " ] 에 해당하는 계정이 없습니다.." );
        errorResponse.setCode("account.not.found.exception");
        return errorResponse;
    }

}
