package com.github.dominik48n.party.user;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dominik48n.party.api.player.PartyPlayerSettings;
import com.github.dominik48n.party.database.PartyPlayerSettingsImpl;

import java.io.IOException;

public class PartyPlayerSettingsDeserializer extends JsonDeserializer<PartyPlayerSettings> {

    @Override
    public PartyPlayerSettings deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        boolean allowInvitations = node.get("allow_invitations").asBoolean();
        return new PartyPlayerSettingsImpl().allowInvitations(allowInvitations);
    }
}
