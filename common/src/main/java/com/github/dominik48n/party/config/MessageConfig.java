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

package com.github.dominik48n.party.config;

import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class MessageConfig {

    static @NotNull MessageConfig fromDocument(final @NotNull Document document) {
        final MessageConfig config = new MessageConfig();

        loadMessages(config, document, "");
        config.prefix = document.getString("prefix", config.prefix());
        return config;
    }

    private static void loadMessages(final @NotNull MessageConfig config, final @NotNull Document document, final @NotNull String keyPrefix) {
        for (final String key : document.keys()) {
            final String fullKey = (keyPrefix.isEmpty() ? "" : keyPrefix + '.') + key;
            if (document.isDocument(key)) {
                loadMessages(config, document.getDocument(key), fullKey);
                continue;
            }

            try {
                final MessageFormat messageFormat = new MessageFormat(document.getString(key, "weird message"));
                config.messages.put(fullKey, messageFormat);
            } catch (final IllegalArgumentException ignored) {
            }
        }
    }

    private final @NotNull Map<String, MessageFormat> messages = Maps.newHashMap();
    private @NotNull String prefix = "<gray>[<gradient:#d896ff:#be29ec>Party</gradient>]";

    public @NotNull Component getMessage(final @NotNull String key, final @NotNull Object... replacements) {
        final MessageFormat messageFormat = this.messages.getOrDefault(key, new MessageFormat(key));
        final String messageString = messageFormat.format(replacements);
        return MiniMessage.miniMessage().deserialize(messageString.replace("%prefix%", this.prefix));
    }

    @NotNull Document toDocument() {
        return new Document()
                .append("prefix", this.prefix)
                .append("general", new Document()
                        .append("updates", new Document()
                                .append("new_version", "%prefix% <green>There is a new version of Party System<dark_gray>:</dark_gray> <bold>{0}</bold>")
                                .append("failed", "%prefix% <red>The latest version of Party System could not be queried.")
                        )
                        .append("player_not_online", "%prefix% <red>{0} is not online.")
                        .append("error", "%prefix% <red>There has been an error. Please try again later."))
                .append("party", new Document()
                        .append("join", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient> <green>has joined the party.")
                        .append("left", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient> <red>left the party.")
                        .append("kick", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient> <red>was kicked out of the party.")
                        .append("connect_to_server", "%prefix% The party will now join the server <gradient:#d896ff:#be29ec>{0}</gradient>.")
                        .append("chat", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient><dark_gray>: <gray>{1}")
                        .append("new_leader", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient> is now the <gradient:#940000:#c30101>party leader</gradient>."))
                .append("command", new Document()
                        .append("invite", new Document()
                                .append("created_party", "%prefix% <green>You have created a new party.")
                                .append("already_in_party", "%prefix% <red>The player is already in a party.")
                                .append("already_invited", "%prefix% <red>You have already invited this player to your party.")
                                .append("self", "%prefix% <red>You can't invite yourself to a party.")
                                .append("not_leader", "%prefix% <red>You must be the party leader to invite players.")
                                .append("sent", "%prefix% <green>You sent a party request to <gradient:#d896ff:#be29ec>{0}</gradient>.")
                                .append("received", "%prefix% <green>You received a party request from <gradient:#d896ff:#be29ec>{0}</gradient>. " +
                                        "<dark_gray>[<green><click:run_command:/party accept {0}>Accept</click></green>] | " +
                                        "[<red><click:run_command:/party deny {0}>Decline</click></red>]")
                                .append("limit", "%prefix% <red>You would exceed the member limit of {0} with the invitation, which is why you cannot invite anyone."))
                        .append("accept", new Document()
                                .append("already", "%prefix% <red>You are already in a party.")
                                .append("no_request", "%prefix% <red>You either didn't receive a party request or it has expired.")
                                .append("limit", "%prefix% <red>The party has reached its member limit, so you can't join.")
                                .append("joined", "%prefix% <green>You joined the party."))
                        .append("deny", new Document()
                                .append("no_request", "%prefix% <red>You either didn't receive a party request or it has expired.")
                                .append("declined", "%prefix% <red>You declined the party request.")
                                .append("other", "%prefix% <gradient:#d896ff:#be29ec>{0}</gradient> <red>declined your party request."))
                        .append("promote", new Document()
                                .append("not_leader", "%prefix% <red>This action can only be performed as a party leader.")
                                .append("self", "%prefix% <red>You cannot promote yourself to leader.")
                                .append("changed", "%prefix% You promoted <gradient:#d896ff:#be29ec>{0}</gradient> to <gradient:#940000:#c30101>leader</gradient>."))
                        .append("kick", new Document()
                                .append("not_leader", "%prefix% <red>Only leaders can kick members out of the party.")
                                .append("self", "%prefix% <red>You can't kick yourself out of the party.")
                                .append("kicked", "%prefix% <red>You got kicked out of the party.")
                                .append("leader", "%prefix% You kicked <gradient:#d896ff:#be29ec>{0}</gradient> out of the party."))
                        .append("usage", new Document()
                                .append("invite", "%prefix% <red>Usage: /party invite <player>")
                                .append("accept", "%prefix% <red>Usage: /party accept <player>")
                                .append("deny", "%prefix% <red>Usage: /party deny <player>")
                                .append("promote", "%prefix% <red>Usage: /party promote <player>")
                                .append("kick", "%prefix% <red>Usage: /party kick <player>")
                                .append("chat", "%prefix% <red>Usage: /p <message>"))
                        .append("list", new Document()
                                .append("without_member_limit", "%prefix% <gold>Party information<newline>" +
                                        " <gray>Leader<dark_gray>: <gradient:#940000:#c30101>{0}<newline>" +
                                        " <gray>Members<dark_gray>: <color:#d896ff>{1}")
                                .append("member_limit", "%prefix% <gold>Party information<newline>" +
                                        " <gray>Leader<dark_gray>: <gradient:#940000:#c30101>{0}<newline>" +
                                        " <gray>Members ({2}/{3}) <dark_gray>: <color:#d896ff>{1}")
                        )
                        .append("leave", "%prefix% <red>You left the party.")
                        .append("not_in_party", "%prefix% <red>You are not in a party.")
                        .append("not_in_your_party", "%prefix% <red>The player is not in your party.")
                        .append("user_not_loaded", "%prefix% <red>Your data has not yet been fully loaded, please wait a moment.")
                        .append("help", "%prefix% <gold>Party management<newline>" +
                                " <yellow>/party invite <player> <gray>Invites a player to the party<newline>" +
                                " <yellow>/party accept <player> <gray>Accepts a request<newline>" +
                                " <yellow>/party deny <player> <gray>Declines a request<newline>" +
                                " <yellow>/party list <gray>Lists all party members<newline>" +
                                " <yellow>/party leave <gray>Leaves the party<newline>" +
                                " <yellow>/party kick <player> <gray>Kicks a player from the party<newline>" +
                                " <yellow>/party promote <player> <gray>Promotes a player<newline>" +
                                " <yellow>/p <message> <gray>Sends a party message")
                );
    }

    // The following methods exist only for tests.

    @NotNull Map<String, MessageFormat> messages() {
        return this.messages;
    }

    @NotNull String prefix() {
        return this.prefix;
    }
}
