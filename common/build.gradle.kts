plugins {
    id("java")
    id("com.avast.gradle.docker-compose") version "0.14.0"
}

group = "com.github.dominik48n.party"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("com.google.guava:guava:31.1-jre")
    compileOnly("net.kyori:adventure-api:4.13.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.13.1")

    implementation(project(":api"))
    implementation("redis.clients:jedis:4.3.2")
    implementation("org.flywaydb:flyway-core:8.0.5")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.2")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate:hibernate-core:6.2.4.Final")

    testImplementation("net.kyori:adventure-api:4.13.1")
    testImplementation("net.kyori:adventure-text-minimessage:4.13.1")
    testImplementation("com.google.guava:guava:31.1-jre")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")



    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testCompileOnly("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
}

dockerCompose {
    useComposeFiles.add("../docker-compose.yml")
    isRequiredBy(tasks.test)
}

tasks.test {
    useJUnitPlatform()
}
