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
 * Der Service nutzt die SPI-Implementierungen genau so, wie der Controller den
 * Service nutzt.
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
     * Checks if the Scan's package ID is an owned Notarization object and need to
     * get updated.
     */
    @Override
    public boolean checkIn(Scan object) {
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

    @Override
    public List<ScanPak> getScanTraceById(String package_id) {
        return reflection.getScanTraceById(package_id);
    }

}
