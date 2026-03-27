package de.omnp.meteoracle.application.port.in;

import java.util.List;

import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.domain.vda4994.Scan;


public interface ScanReceiver {
    public boolean checkIn(Scan object);

    public List<ScanPak> getScanAll();

    public ScanPak getScanById(String package_id);

    public List<ScanPak> getScanTraceById(String package_id);
}
