package com.github.dominik48n.party.user;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.dominik48n.party.api.player.PartyPlayerSettings;

import java.io.IOException;

public class PartyPlayerSettingsSerializer extends StdSerializer<PartyPlayerSettings> {

    public PartyPlayerSettingsSerializer(final Class<PartyPlayerSettings> t) {
        super(t);
    }

    @Override
    public void serialize(PartyPlayerSettings value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeBooleanField("allow_invitations", value.allowInvitations());
        gen.writeEndObject();
    }
}
