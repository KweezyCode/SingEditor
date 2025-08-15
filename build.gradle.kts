plugins {
    id("java")
    id("io.freefair.lombok") version "8.14"
}

group = "com.kweezy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.formdev:flatlaf:3.4.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.19.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.19.2")

    // Jackson modules for java8 and parameter names
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.19.2")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.15.2")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}