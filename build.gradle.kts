plugins {
    id("java")
    id("io.freefair.lombok") version "8.14.2"
}

group = "com.kweezy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.formdev:flatlaf:3.6.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0")

    // Jackson modules for java8 and parameter names
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.20.0")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.20.0")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}