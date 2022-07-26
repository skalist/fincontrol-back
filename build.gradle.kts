import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jwtVersion = "0.11.5"
val mockkVersion = "1.12.3"
val assertjVersion = "3.22.0"

plugins {
	id("org.springframework.boot") version "2.6.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.google.protobuf") version "0.8.19"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21"
}

group = "com.fincontrol"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
	implementation("org.flywaydb:flyway-core")
	implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.2")
	implementation("com.google.protobuf:protobuf-kotlin:3.21.1")
	implementation("io.grpc:grpc-netty-shaded:1.47.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-orgjson:$jwtVersion")
	runtimeOnly("org.postgresql:postgresql")
	api("io.grpc:grpc-protobuf:1.47.0")
	api("io.grpc:grpc-kotlin-stub:1.2.1")
	api("io.grpc:grpc-stub:1.47.0")
	api("com.google.protobuf:protobuf-kotlin:3.21.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("org.assertj:assertj-core:$assertjVersion")
	testImplementation("org.testcontainers:junit-jupiter:1.17.1")
	testImplementation("org.testcontainers:postgresql:1.17.1")

	kapt("org.hibernate:hibernate-jpamodelgen")
}

sourceSets {
	main {
		proto {
			srcDir("src/main/protobuf")
		}
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.19.4"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.47.0"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:1.2.1:jdk7@jar"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc")
				id("grpckt")
			}
			it.builtins {
				id("kotlin")
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
