package de.omnp.meteoracle.infrastructure.api;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

@Tag(name = "Meteoracle API", description = "APIs to interact with the IOTA Rebased network <br> Based on the VDA4994 recommendation for the use of Global Transport Label in the automotive supply chain")
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

    // TODO: Custom error for displaying 500 error cause -> out of gas
    // @RequestBody -> (Header) Content-Type: application/json
    @Operation(summary = "API to create or update a Scan Notarization object")
    @PostMapping(path = "/scan")
    public ResponseEntity<Void> scan(@Valid @RequestBody ScanDTO object){
        logger.info("Received scan for POST transaction from device " + object.getDeviceId());
        // Convert DTO into Domain Object
        Scan post = mapper.toDomain(object);        
        if (transaction.checkIn(post)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            logger.error("Returning status code " + HttpStatus.INTERNAL_SERVER_ERROR); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
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
            scansDTO.add(dto);
        }

        // 3. Rückgabe mit HTTP Status 200 OK
        return ResponseEntity.ok(scansDTO);
    }

    @Operation(summary = "API to get a specific owned Scan Notarization object according to the package ID")
    @GetMapping(path = "/scan/{package_id}")
    public ResponseEntity<ScanDTO>getScanById(@PathVariable(name = "package_id") String package_id)
    { 
        ScanPak scan = this.transaction.getScanById(package_id);
        if (scan != null) {
            ScanDTO scanDTO = mapper.toDto(scan.scan());
            scanDTO.setOnchainId(scan.onChainId());

            return ResponseEntity.ok(scanDTO);
        }
        return ResponseEntity.ok(null); 
    }
}
