package com.github.dominik48n.party.config;

import org.jetbrains.annotations.NotNull;

public record DatabaseConfig(boolean enableFlyWay, String dataBaseType, String database, String host, String port, String user, String password) {

    static @NotNull DatabaseConfig fromDocument(final @NotNull Document document) {
        return new DatabaseConfig(
                document.getBoolean("enable_flyway", true),
                document.getString("database_type", "mariadb"),
                document.getString("database", "CoreStorage"),
                document.getString("host", "localhost"),
                document.getString("port", "3306"),
                document.getString("user", "party_user"),
                document.getString("password", "party_user_password")
        );
    }

    @NotNull Document toDocument() {
        return new Document()
                .append("enable_flyway", this.enableFlyWay)
                .append("database_type", this.dataBaseType)
                .append("database", this.database)
                .append("host", this.host)
                .append("port", this.port)
                .append("user", this.user)
                .append("password", this.password);
    }

}
