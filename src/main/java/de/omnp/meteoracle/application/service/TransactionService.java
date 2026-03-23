package de.omnp.meteoracle.application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.omnp.meteoracle.application.port.in.ScanReceiver;
import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.application.port.out.ScanReflection;
import de.omnp.meteoracle.application.port.out.ScanSender;
import de.omnp.meteoracle.domain.vda4994.Scan;

// DER SERVICE: Implementiert IN, nutzt OUT
/**
 * Der Service ist die Implementierung des Inbound-Ports (UseCase).
 * Er orchestriert den Ablauf, ohne technische Details zu kennen. 
 * Der Service nutzt die SPI-Implementierungen genau so, wie der Controller den Service nutzt.
 */
@Service
public class TransactionService implements ScanReceiver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ScanReflection reflection;
        
    // Der Service nutzt den Outbound-Port (das SPI)
    private final ScanSender sender;

    // Konstruktor-Injektion (Kein @Autowired nötig, da pure Java/Hexagonal)
    public TransactionService(ScanSender sender, ScanReflection reflection) {
        this.sender = sender;
        this.reflection = reflection;
    }

    /**
     * Checks if the Scan's package ID is an owned Notarization object and need to get updated.
     */
    @Override
    public boolean checkIn(Scan object) {        
        // TODO: objectAddress from the object associated with the address and wants to get updated
        String objectToUpdate = new String();
        objectToUpdate = reflection.reflectTransactions(object.getPackageId());
        if (objectToUpdate != null) {
            logger.info("Initializing the update process for object: " + objectToUpdate);
            if (this.sender.updateTransaction(object, objectToUpdate)) {
                return true;
            } else {
                return false;
            }     
        } else if (this.sender.sendTransaction(object)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ScanPak> getScanAll() {
        return reflection.getAllScans();
    }

    @Override
    public ScanPak getScanById(String package_id) {
        return reflection.getScanById(package_id);
    }
    
}
