package com.mzuha.configservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzuha.configservice.model.ConfigRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
class ConfigControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15-alpine");

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer("apache/kafka:3.7.0");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testFullCycle() throws Exception {
        // 1. CREATE
        ConfigRequest request = new ConfigRequest("testApp", "testProfile", "testKey", "testValue");

        MvcResult result = mockMvc.perform(post("/config")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("testApp"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Integer id = com.jayway.jsonpath.JsonPath.read(content, "$.id");

        // 2. GET ALL
        mockMvc.perform(get("/config")
                                .param("application", "testApp")
                                .param("profile", "testProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].application").value("testApp"));

        // 3. GET BY ID
        mockMvc.perform(get("/config/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.key").value("testKey"))
                .andExpect(jsonPath("$.value").value("testValue"));

        // 4. UPDATE
        ConfigRequest updateRequest = new ConfigRequest("testApp", "testProfile", "updatedKey", "updatedValue");
        mockMvc.perform(put("/config/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("updatedValue"))
                .andExpect(jsonPath("$.key").value("updatedKey"));

        // 5. DELETE
        mockMvc.perform(delete("/config/" + id))
                .andExpect(status().isOk());

        // 6. VERIFY DELETED (404)
        mockMvc.perform(get("/config/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/config/999999"))
                .andExpect(status().isNotFound());
    }
}