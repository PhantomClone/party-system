package com.github.dominik48n.party.database;

import com.github.dominik48n.party.config.DatabaseConfig;
import com.github.dominik48n.party.config.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ITest {

    protected static JPAService jpaService;

    @BeforeAll
    static void beforeAll() throws InvocationTargetException, IllegalAccessException {
        Method method = ReflectionUtils.findMethod(DatabaseConfig.class, "fromDocument", Document.class).get();

        method.setAccessible(true);
        DatabaseConfig databaseConfig = (DatabaseConfig) method.invoke(null, new Document());
        method.setAccessible(false);

        jpaService = JPAServiceImpl.fromConfig(databaseConfig);
        jpaService.runFlyway();
        jpaService.startJPA();
    }

    static void clear(Class<?> clazz) {
        jpaService.runInTransactionC(entityManager ->
            entityManager.createQuery(
                    entityManager.getCriteriaBuilder().createCriteriaDelete(clazz)
            ));
    }

}
