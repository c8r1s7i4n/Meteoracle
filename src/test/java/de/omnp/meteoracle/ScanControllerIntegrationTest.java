package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.omnp.meteoracle.application.port.in.ScanReceiver;
import de.omnp.meteoracle.application.service.TransactionService;
import de.omnp.meteoracle.domain.vda4994.JLocation;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.ScanController;
import de.omnp.meteoracle.infrastructure.api.dto.JLocationDTO;
import de.omnp.meteoracle.infrastructure.api.dto.JsonDataDTO;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;
import de.omnp.meteoracle.infrastructure.spi.IotaMetadata;
import de.omnp.meteoracle.infrastructure.spi.TransactionAdapter;
import de.omnp.meteoracle.infrastructure.spi.TransactionReflection;
import tools.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
public class ScanControllerIntegrationTest {

    private MockMvc mockMvc;

    // 1. Mock the low-level infrastructure
    @Mock
    private IotaMetadata iotaMetadata;

    @Mock
    private TransactionAdapter transactionAdapter;

    @Mock
    private TransactionReflection transactionReflection;

    // 2. The real Mapper (usually better to use real mapper than a mock)
    private ScannerMapper mapper = Mappers.getMapper(ScannerMapper.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // 3. Manually wire the service with mocks to ensure clean state
        // This is where the Hexagonal "Service" is instantiated with mocked adapters
        ScanReceiver transactionService = new TransactionService(transactionAdapter, transactionReflection);
        
        // 4. Inject the service into the controller
        ScanController scanController = new ScanController(transactionService, mapper);
        
        this.mockMvc = MockMvcBuilders.standaloneSetup(scanController).build();
    }

    // Integration test "Web-to-Service"
    @Test
    public void testScan() throws Exception {

        // Stubbing
        when(transactionAdapter.sendTransaction(any(Scan.class))).thenReturn(true);

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

        when(transactionAdapter.sendTransaction(any(Scan.class))).thenReturn(true);

        // Demo GTL Label data
        ScanDTO scan = new ScanDTO("Package 01x355","EAN-13",
            "3700123300014",
            "2026-03-01T12:00:00Z",
            "myAndroidScanner",
            "scanTransaction",
            new JLocationDTO(0, 0), 
            new JsonDataDTO()
        );

        // JSON-String "pretty" ausgeben
        String jsonContent = objectMapper.writeValueAsString(scan);

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
        
        Scan scan = mapper.toDomain(dto);

        System.out.println("Convert result: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scan));
        
        assertEquals("myAndroidScanner", scan.getDeviceId());
        assertEquals("scanTransaction", scan.getType());
        assertEquals("3700123300014", scan.getValue());
        assertEquals(45, scan.getLocation().getLatitude());
    }

    @Test
    void shouldMapDomainToDTO() {

        Scan dom = new Scan("Package 01x333","EAN-13",
         "3700123300014",
         "2026-03-01T12:00:00Z",
         "myAndroidScanner",
         "scanTransaction",
         new JLocation(45, 46),
         null
        );
        
        ScanDTO scan = mapper.toDto(dom);

        System.out.println("Convert result: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scan));
        
        assertEquals("myAndroidScanner", scan.getDeviceId());
        assertEquals("scanTransaction", scan.getType());
        assertEquals("3700123300014", scan.getValue());
        assertEquals(45, scan.getLocation().getLatitude());
    }

    // TODO: Getter tests
}