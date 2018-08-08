package com.ishoal.app.integrationtest;

import org.flywaydb.core.Flyway;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;

public class DbMigrationTestExecutionListener extends AbstractTestExecutionListener {
    public DbMigrationTestExecutionListener() {
        super();
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        flyway(testContext).clean();
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        flyway(testContext).migrate();
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        DirtiesDb dirtiesDb = findAnnotation(testContext);
        if(dirtiesDb != null && dirtiesDb.value()) {
            flyway(testContext).clean();
        }
    }

    private DirtiesDb findAnnotation(TestContext testContext) {
        DirtiesDb dirtiesDb = AnnotationUtils.findAnnotation(testContext.getTestMethod(), DirtiesDb.class);
        if(dirtiesDb == null) {
            dirtiesDb = AnnotationUtils.findAnnotation(testContext.getTestClass(), DirtiesDb.class);
        }
        return dirtiesDb;
    }

    private Flyway flyway(TestContext testContext) {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("classpath://");
        return flyway;
    }
}
