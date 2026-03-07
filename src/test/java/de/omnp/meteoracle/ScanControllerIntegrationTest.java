package de.omnp.meteoracle;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.TransactionInterface;
import de.omnp.meteoracle.application.service.TransactionService;
import de.omnp.meteoracle.infrastructure.api.ScanController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    // Jackson (for reading and writing JSON)
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Statt @Mock nutzt du die echte Klasse:
        TransactionInterface realService = new TransactionService(); 
        scanController = new ScanController(realService); // Falls Konstruktor-Injection
        mockMvc = MockMvcBuilders.standaloneSetup(scanController).build();
    }

    // Integration test
    @Test
    public void testScanSuccess() throws Exception {
           
        // Demo GTL Label data
        Post post = new Post("EAN-13",
         "3700123300014",
         "17:15 Uhr",
         "myAndroidScanner",
         "scanTransaction",
         new JLocation(0, 0), 
         new JsonData()
        );

        // JSON-String "pretty" ausgeben
        String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(post);

        System.out.println(jsonContent);

        // Simuliert einen HTTP-Request & Assert
        mockMvc.perform(post("/scan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}