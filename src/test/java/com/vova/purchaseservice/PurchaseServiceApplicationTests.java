package com.vova.purchaseservice;

import com.vova.purchaseservice.config.I18NConfig;
import com.vova.purchaseservice.config.JpaConfig;
import com.vova.purchaseservice.config.SecurityConfiguration;
import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringBootTest(classes = {PurchaseServiceApplication.class, TestConfiguration.class,
        SecurityConfiguration.class, I18NConfig.class, JpaConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PurchaseServiceApplicationTests {

    @Autowired
    private Environment env;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
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
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Assert.assertTrue(byLogin.isPresent());
        byLogin.ifPresent(System.out::println);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void saveUserTestLenConstrain() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazc"));
        valueMap.put("password", Collections.singletonList("qac"));
        valueMap.put("name", Collections.singletonList("t"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.length()", Matchers.equalTo(3)));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Assert.assertFalse(byLogin.isPresent());
    }


    @Test
    public void userInfo() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/info").header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes())))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("qazwsxedc")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName", Matchers.is("test name user")));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        byLogin.ifPresent(userRepository::delete);
    }


    @Test
    public void userInfoUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/info"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void lockTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/user/info")
                    .header(HttpHeaders.AUTHORIZATION,
                            "Basic " + Base64Utils
                                    .encodeToString("invlogin:invpass".getBytes())))
                    .andExpect(MockMvcResultMatchers
                            .status()
                            .is(HttpStatus.UNAUTHORIZED.value()));
        }
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/info")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils
                                .encodeToString("invlogin:invpass".getBytes())))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(HttpStatus.FORBIDDEN.value()));
    }


    @Test
    public void changeUserTest() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "qazwsx")
                .param("password", "1qaz2wsx3edc"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("qazwsxedc")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName", Matchers.is("qazwsx")));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Assert.assertTrue(passwordEncoder.matches("1qaz2wsx3edc", byLogin.get().getPassword()));
        byLogin.ifPresent(userRepository::delete);
    }


    @Test
    public void changeUserTestNocomplete() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "qazwsx"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        byLogin.ifPresent(userRepository::delete);
    }

}
