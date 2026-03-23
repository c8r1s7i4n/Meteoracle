package de.omnp.meteoracle.application.port;

import de.omnp.meteoracle.application.domain.MoveCall.NotarizationMetadata;

public interface ScanReflection {
    public String reflectTransactions(NotarizationMetadata metadata, String target_pkg_id); 
}
