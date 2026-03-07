package de.omnp.meteoracle.application.port;

import de.omnp.meteoracle.application.domain.vda4994.Post;


public interface TransactionInterface {
    public boolean sendTransaction(Post object);
}
