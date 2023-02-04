import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
}

group = "oxyac"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.5")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.4.0")
    implementation("org.jsoup:jsoup:1.15.3")
    compileOnly("org.projectlombok:lombok")
    compileOnly("javax.transaction:transaction-api:1.1-rev-1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2:2.1.214")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    imageName.set("oxyac/asem-scheduler:${version}")
    environment.set(mapOf(
            "JAVA_TOOL_OPTIONS" to "--add-opens=java.base/java.time=ALL-UNNAMED",
            "BP_JVM_VERSION" to "17.*"
    ))

}
