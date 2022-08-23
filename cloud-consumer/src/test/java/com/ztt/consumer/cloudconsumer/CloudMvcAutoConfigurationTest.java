package com.ztt.consumer.cloudconsumer;

import com.ztt.consumer.cloudconsumer.controller.MvcController;
import com.ztt.consumer.cloudconsumer.service.ConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MvcController.class)
public class CloudMvcAutoConfigurationTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ConsumerService consumerService;


    @Test
    void testExample() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/mvc/test").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect((ResultMatcher) content().string("callProvider2"));
    }
}
