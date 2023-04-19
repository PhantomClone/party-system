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

package com.github.dominik48n.party.velocity;

import com.github.dominik48n.party.command.CommandManager;
import com.github.dominik48n.party.config.ProxyPluginConfig;
import com.github.dominik48n.party.user.UserManager;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandManager implements RawCommand {

    private final @NotNull UserManager<Player> userManager;
    private final @NotNull CommandManager commandManager;

    public VelocityCommandManager(final @NotNull UserManager<Player> userManager, final @NotNull PartyVelocityPlugin plugin) {
        this.commandManager = new CommandManager() {
            @Override
            public void runAsynchronous(final @NotNull Runnable runnable) {
                plugin.server().getScheduler().buildTask(plugin, runnable).schedule();
            }

            @Override
            public @NotNull ProxyPluginConfig config() {
                return plugin.config();
            }
        };
        this.userManager = userManager;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!(invocation.source() instanceof final Player player)) {
            invocation.source().sendMessage(Component.text("This command is only available for players!"));
            return;
        }

        this.commandManager.execute(this.userManager.createOrGetPlayer(player), invocation.arguments().split(" "));
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return this.commandManager.tabComplete(invocation.arguments().split(" "));
    }
}
