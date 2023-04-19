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

package com.github.dominik48n.party.bungee;

import com.github.dominik48n.party.player.PlayerManager;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class BungeePlayerManager extends PlayerManager<ProxiedPlayer> {

    @Override
    protected void sendMessage(final @NotNull ProxiedPlayer player, final @NotNull Component component) {
        // TODO: Bungee audience
    }

    @Override
    protected @NotNull String playerName(final @NotNull ProxiedPlayer player) {
        return player.getName();
    }

    @Override
    protected @NotNull UUID playerUUID(final @NotNull ProxiedPlayer player) {
        return player.getUniqueId();
    }
}
