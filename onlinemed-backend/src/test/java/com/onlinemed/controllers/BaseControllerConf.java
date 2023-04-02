package com.onlinemed.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.onlinemed.config.BuildProfiles.TEST_PROFILE;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(TEST_PROFILE)
@PropertySource(name = TEST_PROFILE, value = "classpath:application-test.properties")
@AutoConfiguration
abstract class BaseControllerConf {

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

}
