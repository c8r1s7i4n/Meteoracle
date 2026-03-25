package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.spi.IotaMetadata;
import de.omnp.meteoracle.infrastructure.spi.TransactionReflection;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class TransactionReflectionTest {
    private MockWebServer mockWebServer;
    private TransactionReflection reflection;

    @Mock
    private IotaMetadata metadata;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2. Get the ACTUAL local URL from the server (e.g., http://localhost:54321)
        String localUrl = mockWebServer.url("/").toString();

        // Inject the local mock server URL into your metadata mock
        this.metadata = new IotaMetadata(localUrl, "iotaprivkey1qz2kv3fjtnwfk9n838az04mq5faakajgja7hggxrgas0ke5pd7fe7u9qwn6");
        // Mock other metadata fields (wallet, etc.) if the private methods use them
        
        reflection = new TransactionReflection(metadata);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getTransactionById() {
        // 1. Arrange: Mock-Verhalten definieren
        List<ScanPak> mockData = List.of(
            new ScanPak(new Scan(), "0xeee"),
            new ScanPak(new Scan(), "0xeef")
        );
        
        when(reflection.getAllScans()).thenReturn(mockData);
        
        // 1. Queue Response for 'prepareTransaction' (The first POST call)
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"result\": {\"txBytes\": \"dGhpcyBpcyBhIGZha2UgdHJhbnNhY3Rpb24=\"}}")
                .addHeader("Content-Type", "application/json"));

        // 2. Queue Response for 'executeTransaction' (The second POST call)
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // 3. Act: Call the PUBLIC method
        List<ScanPak> result = reflection.getAllScans();

        // 4. Assert
        assertNotNull(result, "Die Liste sollte nicht null sein");
    }
}
