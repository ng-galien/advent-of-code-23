plugins {
    id("java")
}

group = "com.github.nggalien.adventofcode23"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
//Configure the compiler to use UTF-8 and enable preview features
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("--enable-preview", "-Xlint:preview"))
}
//Run tests with --enable-preview
tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
}
//Run the application with --enable-preview
tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}