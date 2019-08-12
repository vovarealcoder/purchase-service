package com.vova.purchaseservice;

import com.jayway.jsonpath.JsonPath;
import com.vova.purchaseservice.config.I18NConfig;
import com.vova.purchaseservice.config.SecurityConfiguration;
import com.vova.purchaseservice.data.crud.PurchaseRepository;
import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
        SecurityConfiguration.class, I18NConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PurchaseServiceApplicationTests {

    @Autowired
    private Environment env;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;
    private MockMvc insecuredMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .dispatchOptions(true).build();
        this.insecuredMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
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
    public void userInfoWithoutSecurity() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        insecuredMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        insecuredMvc.perform(MockMvcRequestBuilders.get("/user/info")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes())))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(401)));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        byLogin.ifPresent(userRepository::delete);
    }


    @Test
    public void userInfoUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/info"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Ignore("Тест блокировки при переборе паролей. отключен т.к. ломает другие")
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

    @Test
    public void createPurchase() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders.put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void createPurchaseAllFields() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void getPurchaseInfoTest() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void getPurchaseInfoTestInsecured() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        insecuredMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        insecuredMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        byLogin.ifPresent(userRepository::delete);
    }


    @Test
    public void editPurchaseOneField() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/service/purchases/" + id)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase11"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void editPurchaseAllField() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/service/purchases/" + id)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase11")
                .param("purchased", "12-05-2009")
                .param("factPrice", "1600")
                .param("planPrice", "200")
                .param("count", "3")
                .param("status", "DONE")
                .param("comment", "1azwsx")
                .param("planDate", "14-05-2006"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase11")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("DONE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.purchased", Matchers.is("2009-05-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.factPrice", Matchers.is(1600)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("1azwsx")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2006-05-14T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(200)))
                .andReturn().getResponse().getContentAsString();
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }


    @Test
    public void getPurchase() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(("NEW"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idPurchase"), Integer.class);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/service/purchases/" + id)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes())))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }

    @Test
    public void deletePurchase() throws Exception {
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.put("login", Collections.singletonList("qazwsxedc"));
        valueMap.put("password", Collections.singletonList("qaz121wsxedc"));
        valueMap.put("name", Collections.singletonList("test name user"));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/user/register").params(valueMap))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        String content = mockMvc.perform(MockMvcRequestBuilders
                .put("/service/purchases/create")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes()))
                .param("name", "test purchase")
                .param("planDate", "12-12-2019")
                .param("planPrice", "1233")
                .param("comment", "test comment")
                .param("count", "2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test purchase")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("NEW")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", Matchers.is("test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planDate", Matchers.is("2019-12-12T00:00:00.000+0000")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.planPrice", Matchers.is(1233)))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(content).read(JsonPath.compile("$.idSchedule"), Integer.class);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/service/schedule/" + id)
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("qazwsxedc:qaz121wsxedc".getBytes())))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
        Optional<User> byLogin = userRepository.findByLogin("qazwsxedc");
        Optional<Purchase> purchase = purchaseRepository.getByUser_LoginAndIdPurchase("qazwsxedc", id);
        Assert.assertFalse(purchase.isPresent());
        purchase.ifPresent(purchaseRepository::delete);
        byLogin.ifPresent(userRepository::delete);
    }
    

}
