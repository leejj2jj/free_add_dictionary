package com.freeadddictionary.dict.support;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanup implements InitializingBean {

  @PersistenceContext private EntityManager entityManager;

  private List<String> tableNames;

  @Override
  public void afterPropertiesSet() {
    tableNames =
        entityManager.getMetamodel().getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(
                e -> {
                  Table table = e.getJavaType().getAnnotation(Table.class);
                  return table != null ? table.name() : e.getName();
                })
            .collect(Collectors.toList());
  }

  @Transactional
  public void execute() {
    entityManager.flush();
    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

    for (String tableName : tableNames) {
      entityManager
          .createNativeQuery("TRUNCATE TABLE \"" + tableName.toUpperCase() + "\"")
          .executeUpdate();
    }

    entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
  }
}
