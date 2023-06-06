package com.github.dominik48n.party.database;

import com.github.dominik48n.party.api.player.PartyPlayerSettings;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Table(name = "party_player_settings")
@AllArgsConstructor(access = PRIVATE)
@Builder(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@Accessors(fluent = true, chain = true)
public class PartyPlayerSettingsImpl implements PartyPlayerSettings {

    @Id
    @Column(name = "player_uuid")
    @Getter
    private UUID playerUuid;
    @Column(name = "allow_invitation")
    @ColumnDefault("true")
    @Getter
    @Setter
    private boolean allowInvitations;

    public static @NotNull PartyPlayerSettingsImpl ofEntityManager(EntityManager entityManager, @NotNull UUID playerUuid) {
        PartyPlayerSettingsImpl databasePartyPlayer = entityManager.find(PartyPlayerSettingsImpl.class, playerUuid);
        if (databasePartyPlayer == null) {
            return new PartyPlayerSettingsImplBuilder()
                    .playerUuid(playerUuid)
                    .allowInvitations(true)
                    .build();
        }

        return databasePartyPlayer;
    }

    public PartyPlayerSettingsImpl save(EntityManager entityManager) {
        entityManager.merge(this);
        return this;
    }

}
