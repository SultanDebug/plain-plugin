plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "com.hzq"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.baomidou:mybatis-plus-generator:3.0.7")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.0.7")
    implementation("com.alibaba:fastjson:1.2.60")
    implementation("mysql:mysql-connector-java:8.0.31")
    implementation("org.postgresql:postgresql:42.2.8")
    implementation("org.freemarker:freemarker:2.3.15")
    implementation("com.intellij:forms_rt:7.0.3")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("222.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
