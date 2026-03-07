package de.omnp.meteoracle.application.service;

import de.omnp.meteoracle.application.domain.ddd.DomainService;
import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.TransactionInterface;

@DomainService
public class TransactionService implements TransactionInterface {

    public boolean sendTransaction(Post object) {
        // TODO: Sending into the iota network.
        return true;
    }
}
