package com.github.dominik48n.party.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyPlayerSettingsImplTest extends ITest {

    @AfterAll
    static void afterAll() {
        clear(PartyPlayerSettingsImpl.class);
    }

    @Test
    void test() {
        UUID playerUuid = UUID.randomUUID();

        PartyPlayerSettingsImpl partyPlayerSetting = jpaService.runInTransaction(entityManager ->
                PartyPlayerSettingsImpl.ofEntityManager(entityManager, playerUuid));

        UUID uniqueId = partyPlayerSetting.playerUuid();

        Boolean entityShouldNotExists = jpaService.runInTransaction(entityManager -> entityManager.find(PartyPlayerSettingsImpl.class, uniqueId) == null);

        assertTrue(entityShouldNotExists);

        jpaService.runInTransaction(partyPlayerSetting::save);

        assertTrue(partyPlayerSetting.allowInvitations());

        partyPlayerSetting.allowInvitations(false);
        jpaService.runInTransaction(entityManager -> entityManager.merge(partyPlayerSetting));

        Boolean entityShouldExists = jpaService.runInTransaction(entityManager -> entityManager.contains(entityManager.getReference(PartyPlayerSettingsImpl.class, uniqueId)));

        assertTrue(entityShouldExists);

        Boolean allowInvitation = jpaService.runInTransaction(entityManager -> entityManager.find(PartyPlayerSettingsImpl.class, uniqueId).allowInvitations());

        assertFalse(allowInvitation);
    }

}
