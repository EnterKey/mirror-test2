package kr.enterkey.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.enterkey.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by enterkey88 on 2016-10-11.
 */
// @Transactional
// @TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccountControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // @Rollback(false)
    @Test
    public void createAccount() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("enterkey");
        createDto.setPassword("password");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());
        // {"id":1,"username":"enterkey","fullName":null,"joined":1476117991850,"updated":1476117991850}
        result.andExpect(jsonPath("$.username", is("enterkey")));

        // 중복 발생
        result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    public void createAccountBadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername(" ");
        createDto.setPassword("1234");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("bad.request")));
    }

    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("enterkey");
        createDto.setPassword("password");
        accountService.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts"));

        // {"content":[{"id":1,"username":"enterkey","fullName":null,"joined":1476120658771,"updated":1476120658771}],"last":true,
        // "totalElements":1,"totalPages":1,"number":0,"size":20,"first":true,"sort":null,"numberOfElements":1}
        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void getAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = accountService.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = accountService.createAccount(createDto);

        AccountDto.Update updateDto = new AccountDto.Update();
        updateDto.setFullName("dooseong eom");
        updateDto.setPassword("pass");

        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
        );

        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fullName", is("dooseong eom")));
    }

    @Test
    public void deleteAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = accountService.createAccount(createDto);

        ResultActions result = mockMvc.perform(delete("/accounts/" + account.getId()));
        result.andDo(print());
        result.andExpect(status().isNoContent());

        /**
         * .andExpect(view().name("readingList"))
         * .andExpect(model().attributeExists("books"))
         * .andExpect(model().attribute("books", is(empty()))
         * .andExpect(model().attribute("books", hasSize(1))
         * .andExpect(model().attribute("books", contains(samePropertyValuesAs(testBook))));
         */
    }

    private AccountDto.Create accountCreateFixture() {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("enterkey")  ;
        createDto.setPassword("password");
        return createDto;
    }
}
