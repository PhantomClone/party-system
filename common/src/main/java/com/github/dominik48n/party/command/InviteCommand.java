/*
 * Copyright 2023 Dominik48N
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dominik48n.party.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dominik48n.party.api.Party;
import com.github.dominik48n.party.api.PartyAPI;
import com.github.dominik48n.party.api.player.PartyPlayer;
import com.github.dominik48n.party.config.PartyConfig;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class InviteCommand extends PartyCommand {

    private final @NotNull PartyConfig config;

    InviteCommand(final @NotNull PartyConfig config) {
        this.config = config;
    }

    @Override
    public void execute(final @NotNull PartyPlayer player, final @NotNull String[] args) {
        if (args.length != 1) {
            player.sendMessage("command.usage.invite");
            return;
        }

        String name = args[0];
        if (name.equalsIgnoreCase(player.name())) {
            player.sendMessage("command.invite.self");
            return;
        }

        Optional<PartyPlayer> target;
        try {
            target = PartyAPI.get().onlinePlayerProvider().get(name);
        } catch (final JsonProcessingException e) {
            target = Optional.empty();
        }
        if (target.isEmpty()) {
            player.sendMessage("general.player_not_online", name);
            return;
        }

        if (target.get().partyId().isPresent()) {
            player.sendMessage("command.invite.already_in_party");
            return;
        }

        name = target.get().name();

        if (PartyAPI.get().existsPartyRequest(player.name(), name)) {
            player.sendMessage("command.invite.already_invited");
            return;
        }

        Optional<Party> party;
        try {
            party = player.partyId().isPresent() ? PartyAPI.get().getParty(player.partyId().get()) : Optional.empty();
        } catch (final JsonProcessingException e) {
            party = Optional.empty();
        }

        if (party.isEmpty()) {
            final Party createdParty;
            try {
                createdParty = PartyAPI.get().createParty(player.uniqueId(), player.memberLimit());
                if (!PartyAPI.get().onlinePlayerProvider().updatePartyId(player.uniqueId(), createdParty.id())) {
                    player.sendMessage("general.error");
                    PartyAPI.get().deleteParty(createdParty.id());
                    return;
                }
            } catch (final JsonProcessingException e) {
                player.sendMessage("general.error");
                return;
            }

            player.partyId(createdParty.id());
            player.sendMessage("command.invite.created_party");

            party = Optional.of(createdParty);
        } else if (!party.get().isLeader(player.uniqueId())) {
            player.sendMessage("command.invite.not_leader");
            return;
        }

        if (this.config.useMemberLimit() && party.get().members().size() >= party.get().maxMembers()) {
            player.sendMessage("command.invite.limit", party.get().maxMembers());
            return;
        }

        PartyAPI.get().createPartyRequest(player.name(), name, this.config.requestExpires());

        player.sendMessage("command.invite.sent", name);
        target.get().sendMessage("command.invite.received", player.name());
    }
}
