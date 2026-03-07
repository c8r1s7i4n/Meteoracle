package de.omnp.meteoracle.infrastructure.api;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.TransactionInterface;
import jakarta.validation.Valid;


@RestController
public class ScanController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // Scannt nach allen Implementierungen vom entsprechendem Interface
    private final TransactionInterface transaction;

    public ScanController(TransactionInterface transaction) {
        this.transaction = transaction;
    }
    @PostMapping(path = "/scan")
    public ResponseEntity<Void> scan(@Valid @RequestBody Post object){
        if (transaction.sendTransaction(object)) {
            logger.info("Successfully sent message from {} into the network from the location {}", object.getDeviceId(), new String(String.valueOf(object.getLocation().getLatitude()) + " and " + String.valueOf(object.getLocation().getLongitude())));
            return ResponseEntity.ok().build();
        } else {
            logger.error("Error when trying to send transaction from {} with the object {} into the network", object.getDeviceId(), object.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  
        }
    }
}
