package com.freeadddictionary.dict.support;

import com.freeadddictionary.dict.config.TestConfig;
import com.freeadddictionary.dict.config.TestQuerydslConfig;
import com.freeadddictionary.dict.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import({TestSecurityConfig.class, TestQuerydslConfig.class, TestConfig.class})
public abstract class IntegrationTest {

  @Autowired protected MockMvc mockMvc;

  @Autowired private DatabaseCleanup databaseCleanup;

  @BeforeEach
  protected void setUp() {
    databaseCleanup.execute();
  }
}
