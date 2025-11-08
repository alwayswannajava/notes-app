package com.notesapp.repository;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractBaseIntegrationTest {
    private static final String MONGO_DB_IMAGE = "mongo:7.0";
    private static final int MONGO_DB_PORT = 27017;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DB_IMAGE)
            .withExposedPorts(MONGO_DB_PORT);

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }
}
