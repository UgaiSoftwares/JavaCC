import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.4.0"
    java
    id("org.javacc.javacc") version "4.0.3"
    id("com.gradleup.shadow") version "9.4.2"
    jacoco
    id("org.jetbrains.dokka") version "2.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
//    id("com.github.sherter.google-java-format") version "0.9"
//    kotlin("jupyter.api") version "0.10.1-8"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("com.github.jk1.dependency-license-report") version "3.0.1"
    id("com.github.spotbugs") version "6.5.8"
    id("com.diffplug.spotless") version "8.1.0"
    application
}

group = "jp.live.ugai"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    compileTestKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport) // report is always generated after tests run
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "17"
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)
            // checkstyle like format mainly for integrations like Jenkins
            xml.required.set(true)
            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
            // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
            // with Github Code Scanning
            sarif.required.set(true)
        }
    }

    jacocoTestReport {
        dependsOn(test) // tests are required to run before generating the report
    }

    val execute by registering(JavaExec::class) {
        group = "application"
        mainClass.set(
            if (project.hasProperty("mainClass")) {
                project.property("mainClass") as String
            } else {
                application.mainClass.get()
            },
        )
        classpath = sourceSets.main.get().runtimeClasspath
    }

    val runActionScriptParser by registering(JavaExec::class) {
        group = "application"
        description = "Runs the JavaCC ActionScript validator."
        dependsOn("classes")
        mainClass.set("com.example.javacc.ActionScriptParser")
        classpath = sourceSets.main.get().runtimeClasspath

        if (project.hasProperty("script")) {
            args(project.property("script").toString())
        }
    }

    val runExpressionParser by registering(JavaExec::class) {
        group = "application"
        description = "Runs the JavaCC arithmetic-expression sample."
        dependsOn("classes")
        mainClass.set("com.example.javacc.ExpressionParser")
        classpath = sourceSets.main.get().runtimeClasspath

        if (project.hasProperty("expression")) {
            args(project.property("expression").toString())
        }
    }
}

application {
    mainClass.set("com.example.javacc.Main")
    applicationDefaultJvmArgs = listOf("--add-modules=jdk.incubator.vector")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml")
}

spotbugs {
    ignoreFailures.set(true)
}

spotless {
    java {
        target("src/*/java/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.27.0") // has its own section below
        formatAnnotations() // fixes formatting of type annotations, see below
    }
}

dokka.dokkaSourceSets {
    configureEach {
        jdkVersion.set(17)
        enableJdkDocumentationLink.set(false)
        enableKotlinStdLibDocumentationLink.set(false)
    }
}
