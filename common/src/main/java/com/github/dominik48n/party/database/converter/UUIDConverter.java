package com.github.dominik48n.party.database.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @Override
    public UUID convertToEntityAttribute(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}