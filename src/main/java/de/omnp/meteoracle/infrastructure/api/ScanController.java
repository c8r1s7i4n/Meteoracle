/*
    Meteoracle - Industrial Supply Chain Interoperability Layer
    Copyright (C) 2026 Christian Beissmann

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Original source: https://github.com/c8r1s7i4n/Meteoracle
    For contact and support, visit: https://omnipons.de
*/

package de.omnp.meteoracle.infrastructure.api;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import de.omnp.meteoracle.application.port.in.ScanReceiver;
import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Meteoracle API", description = "APIs to interact with the IOTA Rebased Network <br> Based on the VDA4994 recommendation for the use of Global Transport Label in the automotive supply chain")
@RestController
@RequestMapping("/api/v1")
public class ScanController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // Scannt nach allen Implementierungen vom entsprechendem Interface
    private final ScanReceiver transaction;

    private ScannerMapper mapper;

    // Konstruktor-Injection
    public ScanController(ScanReceiver transaction, ScannerMapper mapper) {
        this.transaction = transaction;
        this.mapper = mapper;
    }

    @Operation(summary = "API to create or update a Scan Notarization object")
    @PostMapping(path = "/scan")
    public ResponseEntity<Map<String, String>> scan(@Valid @RequestBody ScanDTO object) {
        logger.info("Received scan for POST transaction from device " + object.getDeviceId());
        
        Scan post = mapper.toDomain(object);        
        
        if (transaction.checkIn(post)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            logger.error("Returning status code " + HttpStatus.INTERNAL_SERVER_ERROR); 
            // Sends a custom message in JSON format.
            return ResponseEntity.status(500).body(Map.of("error", "Error 500, probably out of gas.")); 
        }
    }

    @Operation(summary = "API to get all owned Scan Notarization objects")
    @GetMapping(path = "/scan")
    public ResponseEntity<List<ScanDTO>>getScanAll()
    {
        // 1. Daten von der Blockchain/Service laden
        List<ScanPak> scans = transaction.getScanAll();
        List<ScanDTO> scansDTO = new ArrayList<>();
        
        // 2. Umwandlung von Domänen-Objekt (Scan) zu Transfer-Objekt (ScanDTO)
        // via MapStruct -> mapper.toDtoList(scans)
        // Hier als Beispiel mit Java Streams:
        for (ScanPak scan : scans) {
            ScanDTO dto = mapper.toDto(scan.scan());
            dto.setOnchainId(scan.onChainId());
            dto.setStateVersion(scan.stateVersion());
            scansDTO.add(dto);
        }

        // 3. Rückgabe mit HTTP Status 200 OK
        return ResponseEntity.ok(scansDTO);
    }

    @Operation(summary = "API to get the current state of a specific owned Scan Notarization object according to the package ID")
    @GetMapping(path = "/track/{package_id}")
    public ResponseEntity<ScanDTO>getScanById(@PathVariable(name = "package_id") String package_id)
    { 
        ScanPak scan = this.transaction.getScanById(package_id);
        if (scan != null) {
            ScanDTO scanDTO = mapper.toDto(scan.scan());
            scanDTO.setOnchainId(scan.onChainId());
            scanDTO.setStateVersion(scan.stateVersion());

            return ResponseEntity.ok(scanDTO);
        }
        return ResponseEntity.ok(null); 
    }

    @Operation(summary = "API to get the history of a specific owned Scan Notarization object according to the package ID")
    @GetMapping(path = "/trace/{package_id}")
    public ResponseEntity<List<ScanDTO>>getScanTraceById(@PathVariable(name = "package_id") String package_id)
    { 
        List<ScanPak> scans = new ArrayList<>();
        scans = this.transaction.getScanTraceById(package_id);
        
        List<ScanDTO> scansDTO = new ArrayList<>();

        if (scans != null) {
            for (ScanPak scan : scans) {
                ScanDTO scanDTO = mapper.toDto(scan.scan());
                scanDTO.setOnchainId(scan.onChainId());
                scanDTO.setStateVersion(scan.stateVersion());
                scansDTO.add(scanDTO);
            }
            return ResponseEntity.ok(scansDTO);
        }

        return ResponseEntity.ok(null); 
    }
}
