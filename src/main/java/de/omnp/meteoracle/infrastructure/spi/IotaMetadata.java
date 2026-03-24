package de.omnp.meteoracle.infrastructure.spi;

import org.springframework.stereotype.Component;

import de.omnp.meteoracle.infrastructure.jota.Wallet;


// TODO: Informationen via .env Datei einlesen.
@Component
public class IotaMetadata {

    protected static String address;
    protected static final String rpcUrl = "https://api.testnet.iota.cafe:443";
    protected static final String packageId = "0x30db7baf5bb8432e7f875ae5277e05091effc7b0e824269ecf242cacf0eca1a4";
    protected static final String module = "entry_notarization4994";
    protected static final String createFunction = "notarize_4994_scan";
    protected static final String updateFunction = "update_4994_scan";
    protected static String gasBudget = "50000000";

    protected static final Wallet wallet = new Wallet("iotaprivkey...");

    public IotaMetadata() {
        address = wallet.getAddress();
    }
}
