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
