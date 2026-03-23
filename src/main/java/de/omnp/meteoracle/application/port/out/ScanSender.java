package de.omnp.meteoracle.application.port.out;

import de.omnp.meteoracle.domain.vda4994.Scan;

public interface ScanSender {
    // public byte[] signer(byte[] bytes);
    public boolean sendTransaction(Scan scan);

    public boolean updateTransaction(Scan scan, String objectAddress);
}
