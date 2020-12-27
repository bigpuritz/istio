dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.boot:spring-boot-actuator-autoconfigure")
    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8-all:2.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}