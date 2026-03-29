/*
    Meteoracle: Industrial Supply Chain Interoperability Layer
    Copyright (C) 2026  Christian Beissmann

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Original source: https://github.com/c8r1s7i4n/Meteoracle
    For contact and support, visit: https://omnipons.de
*/

package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import de.omnp.meteoracle.application.port.in.ScanReceiver;
import de.omnp.meteoracle.application.port.out.ScanPak;
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

    @Test
    public void testGetAllScan() throws Exception {
        // 1. Prepare Mock Data (Domain Objects wrapped in ScanPak)
        // These represent the data coming from your TransactionAdapter/On-chain service
        Scan domainScan = new Scan(
            "Package_ABC_123", 
            "QR-CODE", 
            "VALUE_999", 
            "2026-03-28T10:00:00Z", 
            "Device_001", 
            "scanTransaction", 
            new JLocation(52.52, 13.40), // Berlin
            null
        );

        // ScanPak is your wrapper containing metadata (objectId, version)
        ScanPak pak = new ScanPak(domainScan, "0xOnChainAddress123", "5");
        
        // 2. Stub the service call
        // Depending on your constructor, this mock should be the one injected into TransactionService
        when(transactionReflection.getAllScans()).thenReturn(Arrays.asList(pak));

        // 3. Perform GET request and verify results
        mockMvc.perform(get("/api/v1/scan")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify JSON structure using JsonPath
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].package_id").value("Package_ABC_123"))
                .andExpect(jsonPath("$[0].onchainId").value("0xOnChainAddress123"))
                .andExpect(jsonPath("$[0].stateVersion").value("5"))
                .andExpect(jsonPath("$[0].location.latitude").value(52.52))
                .andExpect(jsonPath("$[0].deviceId").value("Device_001"));
    }

    @Test
    public void testGetScanById() throws Exception {
        // 1. Prepare Mock Data (Domain Objects wrapped in ScanPak)
        // These represent the data coming from your TransactionAdapter/On-chain service
        Scan domainScan = new Scan(
            "Package_ABC_123", 
            "QR-CODE", 
            "VALUE_999", 
            "2026-03-28T10:00:00Z", 
            "Device_001", 
            "scanTransaction", 
            new JLocation(52.52, 13.40), // Berlin
            null
        );

        // ScanPak is your wrapper containing metadata (objectId, version)
        ScanPak pak = new ScanPak(domainScan, "0xOnChainAddress123", "5");
        
        // 2. Stub the service call
        // Depending on your constructor, this mock should be the one injected into TransactionService
        when(transactionReflection.getScanById("Package_ABC_123")).thenReturn(pak);

        // 3. Perform GET request and verify results
        mockMvc.perform(get("/api/v1/track/Package_ABC_123") // Adjust path to your GET by ID route
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // Use $. to access the fields of the single root object
            .andExpect(jsonPath("$.package_id").value("Package_ABC_123"))
            .andExpect(jsonPath("$.onchainId").value("0xOnChainAddress123"))
            .andExpect(jsonPath("$.stateVersion").value("5"))
            .andExpect(jsonPath("$.location.latitude").value(52.52))
            .andExpect(jsonPath("$.deviceId").value("Device_001"));
    }

    @Test
    public void testGetScanTrace() throws Exception {
        // 1. Arrange: Create two historical states for the same package
        // State 1: Version 1 (The initial scan)
        Scan scanV1 = new Scan("Pkg_123", "EAN-13", "Value_1", "2026-03-01T10:00:00Z", 
                               "Dev_01", "scanTransaction", new JLocation(10.0, 10.0), null);
        ScanPak pakV1 = new ScanPak(scanV1, "0xAddress_ABC", "1");
    
        // State 2: Version 2 (An update/re-scan)
        Scan scanV2 = new Scan("Pkg_123", "EAN-13", "Value_1", "2026-03-01T11:00:00Z", 
                               "Dev_01", "scanTransaction", new JLocation(10.5, 10.5), null);
        ScanPak pakV2 = new ScanPak(scanV2, "0xAddress_ABC", "2");
    
        // Stub the service to return the list (Trace)
        List<ScanPak> trace = Arrays.asList(pakV2, pakV1); // Usually ordered newest first
        when(transactionReflection.getScanTraceById("Pkg_123")).thenReturn(trace);
    
        // 2. Act & Assert
        // IMPORTANT: Ensure this path matches the @GetMapping in your Controller!
        mockMvc.perform(get("/api/v1/trace/Pkg_123") 
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                // Verify first element (Newest)
                .andExpect(jsonPath("$[0].package_id").value("Pkg_123"))
                .andExpect(jsonPath("$[0].stateVersion").value("2"))
                .andExpect(jsonPath("$[0].location.latitude").value(10.5))
                // Verify second element (Oldest)
                .andExpect(jsonPath("$[1].stateVersion").value("1"))
                .andExpect(jsonPath("$[1].location.latitude").value(10.0));
    }
}