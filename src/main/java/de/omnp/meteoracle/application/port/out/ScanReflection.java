package de.omnp.meteoracle.application.port.out;

import java.util.List;

public interface ScanReflection {
    public String reflectTransactions(String target_pkg_id);
    
    public List<ScanPak> getAllScans();

    public ScanPak getScanById(String target_pkg_id);

}
