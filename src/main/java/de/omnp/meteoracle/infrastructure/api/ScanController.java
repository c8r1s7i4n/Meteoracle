package de.omnp.meteoracle.infrastructure.api;

import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.ScanReceiver;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;
import jakarta.validation.Valid;

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
    @PostMapping(path = "/scan")
    public ResponseEntity<Void> scan(@Valid @RequestBody ScanDTO object){
        logger.info("Received scan for POST transaction from device " + object.getDeviceId());
        // Convert DTO into Domain Object
        Post post = mapper.toDomain(object);
        
        if (transaction.checkIn(post)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            logger.error("Returning status code " + HttpStatus.INTERNAL_SERVER_ERROR); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
}
