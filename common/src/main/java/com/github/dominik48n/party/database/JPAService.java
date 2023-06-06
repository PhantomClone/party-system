package com.github.dominik48n.party.database;

import jakarta.persistence.EntityManager;

import java.io.Closeable;
import java.util.function.Consumer;
import java.util.function.Function;

public interface JPAService extends Closeable {

    boolean runFlyway();

    boolean startJPA();

    <T> T runInTransaction(Function<EntityManager, T> function);
    void runInTransactionC(Consumer<EntityManager> consumer);

    boolean isEnableFlyway();

}
