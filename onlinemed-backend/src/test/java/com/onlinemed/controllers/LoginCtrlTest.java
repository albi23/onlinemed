package com.onlinemed.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class LoginCtrlTest extends BaseControllerConf {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @Sql(scripts = "classpath:test-data/admin-test/person_admin.sql")
    void shouldPassLogin() throws Exception {
        Resource jsonResource = resourceLoader.getResource("classpath:test-data/admin-test/expected-login-response.json");
        final String expectedContent = new String(Files.readAllBytes(jsonResource.getFile().toPath()));

        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode expectedJsonNode = objectMapper.readTree(expectedContent);
        final String expectedToken = expectedJsonNode.get("person").toString();

        final String adminTest = Base64.getEncoder().encodeToString("admin:admin123".getBytes());
        var headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + adminTest);
        mvc.perform(post("/api/login")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((res) ->{
                    final JsonNode responseJson = objectMapper.readTree(res.getResponse().getContentAsString());
                    final String responsePerson = responseJson.get("person").toString();
                    Assertions.assertEquals(responsePerson, expectedToken);
                    Assertions.assertNotNull(responseJson.get("token"));
                });

    }

}