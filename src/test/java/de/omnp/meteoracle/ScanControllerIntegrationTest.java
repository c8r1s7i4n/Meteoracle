package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.ScanReceiver;
import de.omnp.meteoracle.application.service.TransactionService;
import de.omnp.meteoracle.infrastructure.api.ScanController;
import de.omnp.meteoracle.infrastructure.api.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.dto.JLocationDTO;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;
import de.omnp.meteoracle.infrastructure.spi.TransactionAdapter;
import de.omnp.meteoracle.infrastructure.spi.TransactionReflection;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import de.omnp.meteoracle.application.domain.vda4994.JLocation;
import de.omnp.meteoracle.application.domain.vda4994.JsonData;


public class ScanControllerIntegrationTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ScanController scanController;

    @InjectMocks
    private ScannerMapper mapper;

    // Jackson (for reading and writing JSON)
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mapper = Mappers.getMapper(ScannerMapper.class);

        TransactionAdapter transactionAdapter = new TransactionAdapter();
        TransactionReflection transactionReflection = new TransactionReflection();


        ScanReceiver realService = new TransactionService(transactionAdapter, transactionReflection);
        scanController = new ScanController(realService, Mappers.getMapper(ScannerMapper.class)); // Konstruktor-Injection
        mockMvc = MockMvcBuilders.standaloneSetup(scanController).build();
    }

    // Integration test
    @Test
    public void testScan() throws Exception {

        String jsonBodyString = "{"
            + "\"package_id\": \"GFS\"," 
            + "\"symbology\": \"EAN-13\","
            + "\"value\": \"3700123300014\","
            + "\"timestamp\": \"2026-03-01T12:00:00Z\","
            + "\"deviceId\": \"myAndroidScanner\","
            + "\"type\": \"scanTransaction\","
            + "\"jsonData\": null,"
            + "\"id\": null,"
            + "\"location\": {"
                + "\"latitude\": 45.0,"
                + "\"longitude\": 46.0"
            + "}"
            + "}";

        // Simuliert einen HTTP-Request & Assert
        mockMvc.perform(post("/api/v1/scan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyString))
                .andExpect(status().isOk());
    }

    @Test
    void convertPOJOtoJSON() throws Exception {

        // Demo GTL Label data
        Post post = new Post("Package 01x355","EAN-13",
            "3700123300014",
            "2026-03-01T12:00:00Z",
            "myAndroidScanner",
            "scanTransaction",
            new JLocation(0, 0), 
            new JsonData()
        );

        // JSON-String "pretty" ausgeben
        String jsonContent = objectMapper.writeValueAsString(post);

        // String jsonContentPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(post);
        // System.out.println(jsonContentPretty);

        // Simuliert einen HTTP-Request & Assert
        mockMvc.perform(post("/api/v1/scan")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonContent))
        .andExpect(status().isOk());
    }

    @Test
    void shouldMapDtoToDomain() {

        ScanDTO dto = new ScanDTO("Package 01x333","EAN-13",
         "3700123300014",
         "2026-03-01T12:00:00Z",
         "myAndroidScanner",
         "scanTransaction",
         new JLocationDTO(45, 46),
         null
        );
        
        Post post = mapper.toDomain(dto);

        System.out.println("Convert result: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(post));
        
        assertEquals("myAndroidScanner", post.getDeviceId());
        assertEquals("scanTransaction", post.getType());
        assertEquals("3700123300014", post.getValue());
        assertEquals(45, post.getLocation().getLatitude());
    }
}