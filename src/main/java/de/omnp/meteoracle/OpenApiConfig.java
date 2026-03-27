package de.omnp.meteoracle;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Meteoracle Track & Trace API",
        version = "0.1.0",
        summary = "MVP for tamper-proof shipment tracking powered by IOTA Rebased & Spring Boot.",
        description = "This MVP addresses the critical lack of transparency in global supply chains by providing a tamper-proof record of every shipment milestone. Architected on a Spring Boot framework utilizing Hexagonal Architecture, the solution maintains a strict separation between core business logic and external technologies. This allows the system to function as a highly decoupled gateway where logistics events are processed through standardized ports and anchored onto the IOTA Rebased ledger via specialized adapters. By leveraging this modular design, the MVP ensures that data—from origin to destination—remains immutable and verifiable by all stakeholders in real-time, while providing the flexibility to scale or integrate with diverse ERP systems without compromising the integrity of the core domain."
    )
)
public class OpenApiConfig {
    
}
