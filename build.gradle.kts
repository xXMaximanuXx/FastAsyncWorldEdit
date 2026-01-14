import org.ajoberstar.grgit.Grgit
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
<<<<<<< HEAD
import org.gradle.configurationcache.extensions.capitalized
import xyz.jpenilla.runpaper.task.RunServer
import java.net.URI
=======
>>>>>>> main
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.codecov)
    jacoco
    id("buildlogic.common")
    id("com.gradleup.nmcp.aggregation") version "1.4.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

var rootVersion by extra("2.15.1")
var snapshot by extra("SNAPSHOT")
var revision: String by extra("")
var buildNumber by extra("")
var date: String by extra("")
ext {
    val git: Grgit = Grgit.open {
        dir = File("$rootDir/.git")
    }
    date = git.head().dateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
    revision = "-${git.head().abbreviatedId}"
    buildNumber = if (project.hasProperty("buildnumber")) {
        snapshot + "-" + project.properties["buildnumber"] as String
    } else {
        project.properties["snapshot"] as String
    }
}

version = String.format("%s-%s", rootVersion, buildNumber)

if (!project.hasProperty("gitCommitHash")) {
    apply(plugin = "org.ajoberstar.grgit")
    ext["gitCommitHash"] = try {
        extensions.getByName<Grgit>("grgit").head()?.abbreviatedId
    } catch (e: Exception) {
        logger.warn("Error getting commit hash", e)

        "no.git.id"
    }
}

val totalReport = tasks.register<JacocoReport>("jacocoTotalReport") {
    for (proj in subprojects) {
        proj.apply(plugin = "jacoco")
        proj.plugins.withId("java") {
            executionData(
                    fileTree(proj.layout.buildDirectory).include("**/jacoco/*.exec")
            )
            sourceSets(proj.the<JavaPluginExtension>().sourceSets["main"])
            reports {
                xml.required.set(true)
                xml.outputLocation.set(rootProject.layout.buildDirectory.file("reports/jacoco/report.xml"))
                html.required.set(true)
            }
            dependsOn(proj.tasks.named("test"))
        }
    }
}
afterEvaluate {
    totalReport.configure {
        classDirectories.setFrom(classDirectories.files.map {
            fileTree(it).apply {
                exclude("**/*AutoValue_*")
                exclude("**/*Registration.*")
            }
        })
    }
}

codecov {
    reportTask.set(totalReport)
}

allprojects {
    gradle.projectsEvaluated {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "1000"))
        }
        tasks.withType<Test>().configureEach {
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
            testLogging {
                events(FAILED)
                exceptionFormat = FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
            }
        }
    }
}

<<<<<<< HEAD
applyCommonConfiguration()
val supportedVersions = listOf("1.18.2", "1.19.4", "1.20", "1.20.4")
val foliaSupportedVersions = listOf("1.20.4")
=======
val supportedVersions: List<String> = listOf("1.20.4", "1.20.5", "1.20.6", "1.21", "1.21.1", "1.21.4", "1.21.5",
        "1.21.8", "1.21.10", "1.21.11")
>>>>>>> main

tasks {
    fun registerVersion(version: String, software: String, task: RunServer.() -> Unit = {}) {
        register<RunServer>("run${software.capitalized()}-$version") {
            minecraftVersion(version)
            pluginJars(*project(":worldedit-bukkit").getTasksByName("shadowJar", false)
                    .map { (it as Jar).archiveFile }
                    .toTypedArray())
<<<<<<< HEAD
            jvmArgs("-DPaper.IgnoreJavaVersion=true", "-Dcom.mojang.eula.agree=true")
            group = "run $software"
            runDirectory.set(file("run-$software-$version"))
            task(this)
        }
    }
    runServer {
        registerVersion("1.20.4", "paper")
=======
            jvmArgs("-DPaper.IgnoreJavaVersion=true", "-Dcom.mojang.eula.agree=true", "--add-modules=jdk.incubator.vector")
            group = "run paper"
            runDirectory.set(file("run-$it"))
        }
    }
    runServer<RunServer> {
        minecraftVersion(supportedVersions.last())
        pluginJars(*project(":worldedit-bukkit").getTasksByName("shadowJar", false).map { (it as Jar).archiveFile }
                .toTypedArray())
        jvmArgs("-Dcom.mojang.eula.agree=true")

>>>>>>> main
    }
    supportedVersions.forEach {
        registerVersion(it, "paper")
    }
    foliaSupportedVersions.forEach {
        registerVersion(it, "folia") {
            downloadsApiService.set(xyz.jpenilla.runtask.service.DownloadsAPIService.folia(project))
        }
    }

}

nmcpAggregation {
    centralPortal {
        publishingType = "AUTOMATIC"
        username = providers.gradleProperty("mavenCentralUsername")
        password = providers.gradleProperty("mavenCentralPassword")
    }

    publishAllProjectsProbablyBreakingProjectIsolation()
}
