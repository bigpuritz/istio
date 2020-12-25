import com.google.cloud.tools.jib.gradle.JibExtension
import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
    id("org.springframework.boot") version "2.4.1" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    id("com.google.cloud.tools.jib") version "2.7.0" apply false
    id("java")
}


allprojects {

    group = "com.example.istio"
    version = "1.0.0"

    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "java")

    configure<SpringBootExtension> {
        buildInfo()
    }

    configure<JibExtension> {
        to {
            image = "bigpuritz/${project.name}:${project.version}"
            // tags = setOf("tag1", "tag2")
        }
        from {
            image = "openjdk:11-jre-slim-buster"
            // image = "gcr.io/distroless/java:11"
        }

        container {
            labels = mapOf("maintainer" to "bigpuritz")
            ports = listOf("8080")

            /*
            See: https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md#why-is-my-image-created-48-years-ago
            For reproducibility purposes, Jib sets the creation time of the container images to the Unix epoch
            (00:00:00, January 1st, 1970 in UTC).
            If you would like to use a different timestamp,
            set the jib.container.creationTime / <container><creationTime> parameter to an ISO 8601 date-time.
            You may also use the value USE_CURRENT_TIMESTAMP to set the creation time to the actual build time,
            but this sacrifices reproducibility since the timestamp will change with every build.
             */
        }
        outputPaths {
            tar = buildDir.resolve("${project.name}-${project.version}-image.tar").absolutePath
            digest = buildDir.resolve("${project.name}-${project.version}-image.digest").absolutePath
            imageId = buildDir.resolve("${project.name}-${project.version}-image.id").absolutePath
            imageJson = buildDir.resolve("${project.name}-${project.version}-image.json").absolutePath
        }
    }

    tasks {

        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        test {
            testLogging.showExceptions = true
            useJUnitPlatform()
        }
    }
}

tasks {

    register("k8s-deploy") {
        group = "k8s"
        doLast {
            rootProject.subprojects {
                exec {
                    workingDir = projectDir
                    commandLine = "kubectl apply -f k8s.yaml".split(" ")
                }
            }
        }
    }
    register("k8s-istio") {
        group = "k8s"
        doLast {
            exec {
                workingDir = projectDir
                commandLine = "kubectl apply -f istio-gateway-vs.yaml".split(" ")
            }
        }
    }

}