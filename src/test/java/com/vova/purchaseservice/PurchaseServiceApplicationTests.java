package com.vova.purchaseservice;

import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PurchaseServiceApplicationTests {

    @Autowired
    private Environment env;
    @Resource
    UserRepository userRepository;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .dispatchOptions(true).build();

    }
    @Test
    public void contextLoads() {
    }

    @Test
    public void saveUserTestBadReq() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void saveUserTest() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        Optional<User> byLogin = userRepository.findByLogin(valueMap.get("login").get(0));
        byLogin.ifPresent(user -> userRepository.delete(user));
    }

    @Test
    public void changeUSerTest() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        Optional<User> byLogin = userRepository.findByLogin(valueMap.get("login").get(0));

        String auth="Basic "+ Base64.getEncoder().encodeToString("qazwsxedc:qaz121wsxedc".getBytes());
        LinkedMultiValueMap<String, String> changemap = new LinkedMultiValueMap<>();
        changemap.put("name", Collections.singletonList("test11r"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/user/"+byLogin.get().getIdUser()).params(valueMap).header("Authorization",auth))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Assert.assertEquals("test11r",userRepository.findByLogin("qazwsxedc").get().getName());
        byLogin.ifPresent(user -> userRepository.delete(user));
    }



    @Test
    public void saveAndUpdateUserTest(){
        User user=new User();
        user.setLogin("test11wwqwq1");
        user.setPassword("passss");
        user.setName("name11");
        userRepository.save(user);
        Optional<User> login = userRepository.findByLogin("test11wwqwq1");
        User user1 = login.get();
        user1.setName("aaaa");
        user1=userRepository.save(user1);
        Assert.assertEquals("aaaa", user1.getName());
        userRepository.delete(user);
    }

}
