package de.omnp.meteoracle.application.port;

import de.omnp.meteoracle.application.domain.MoveCall.NotarizationMetadata;
import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.jota.Wallet;

public interface ScanSender {
    // public byte[] signer(byte[] bytes);
    public boolean sendTransaction(Post post, Wallet wallet, NotarizationMetadata metadata);

    public boolean updateTransaction(Post post, Wallet wallet, NotarizationMetadata metadata, String objectAddress);
}
