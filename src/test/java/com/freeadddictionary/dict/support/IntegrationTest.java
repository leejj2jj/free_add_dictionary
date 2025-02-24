package com.freeadddictionary.dict.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTest {

  @Autowired private DatabaseCleanup databaseCleanup;

  @BeforeEach
  protected void setUp() {
    databaseCleanup.execute();
  }
}
