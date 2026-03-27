package de.omnp.meteoracle.application.port.out;

import de.omnp.meteoracle.domain.vda4994.Scan;

/**
 * Contains the Scan object as well as relevant on-chain metrics
 */
public record ScanPak(Scan scan, String onChainId, String stateVersion) {}
