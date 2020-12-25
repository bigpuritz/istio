dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
//
//docker {
//    springBootApplication {
//        baseImage.set("openjdk:11-jre-slim-buster")
//        maintainer.set("bigpuritz")
//        images.set(setOf("${project.name}:${project.version}", "${project.name}:latest"))
//    }
//}


