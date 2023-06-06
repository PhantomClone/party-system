package com.github.dominik48n.party.database;

import com.github.dominik48n.party.config.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class JPAServiceImpl implements JPAService {

    private final @NotNull String url;
    private final @NotNull String user;
    private final @NotNull String password;
    private final boolean enableFlyway;

    private final ConcurrentMap<EntityManager, Boolean> concurrentMap;

    private EntityManagerFactory entityManagerFactory;

    private JPAServiceImpl(final @NotNull String url, final @NotNull String user, final @NotNull String password, final boolean enableFlyway) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.enableFlyway = enableFlyway;
        this.concurrentMap = new ConcurrentHashMap<>();
    }

    public static JPAServiceImpl fromConfig(DatabaseConfig databaseConfig) {
        try {
            //TODO load correct driver
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
        final String url = urlFromConfig(databaseConfig);

        return new JPAServiceImpl(url, databaseConfig.user(), databaseConfig.password(), databaseConfig.enableFlyWay());
    }

    private static String urlFromConfig(DatabaseConfig databaseConfig) {
        return String.format("jdbc:%s://%s:%s/%s", databaseConfig.dataBaseType(), databaseConfig.host(),
                databaseConfig.port(), databaseConfig.database());
    }

    public static HikariDataSource createDataSource(final @NotNull String url, final @NotNull String user, final @NotNull String password) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Override
    public boolean isEnableFlyway() {
        return this.enableFlyway;
    }

    @Override
    public boolean runFlyway() {
        HikariDataSource dataSource = createDataSource(this.url, this.user, this.password);

        final Flyway flyway = Flyway.configure(getClass().getClassLoader())
                .dataSource(dataSource)
                .locations("classpath:/db/migration/")
                .load();
        final MigrateResult migrate = flyway.migrate();
        dataSource.close();
        return migrate.success;
    }

    @Override
    public boolean startJPA() {
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()){
            return false;
        }

        final Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", this.url);
        properties.put("jakarta.persistence.jdbc.user", this.user);
        properties.put("jakarta.persistence.jdbc.password", this.password);
        properties.put("jakarta.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
        properties.put("jakarta.persistence.schema-generation.database.action", "none");

        this.entityManagerFactory = new HibernatePersistenceProvider().createEntityManagerFactory("CoreStorage", properties);
        return true;
    }

    @Override
    public <T> T runInTransaction(Function<EntityManager, T> function) {
        final EntityManager entityManager = this.concurrentMap.keySet().stream()
                .findFirst()
                .orElseGet(this::createEntityManager);
        this.concurrentMap.remove(entityManager);
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        boolean success = false;
        try {
            T returnValue = function.apply(entityManager);
            success = true;
            return returnValue;
 
        } finally {
            if (success) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
            this.concurrentMap.put(entityManager, true);
        }
    }

    private @NotNull EntityManager createEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

    @Override
    public void runInTransactionC(Consumer<EntityManager> consumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        boolean success = false;
        try {
            consumer.accept(entityManager);
            success = true;
        } finally {
            if (success) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close() {
        this.concurrentMap.keySet().stream()
                .filter(EntityManager::isOpen)
                .forEach(EntityManager::close);
        if (this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }
    }
}