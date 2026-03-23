package de.omnp.meteoracle.infrastructure.spi;

import org.springframework.stereotype.Component;

import de.omnp.meteoracle.infrastructure.jota.Wallet;


@Component
public class IotaMetadata {

    protected static String address;
    protected static final String rpcUrl = "https://api.testnet.iota.cafe:443";
    protected static final String packageId = "0x0849cd69ff8946217824e40e41acfbcd8f1a3d77c3220ffa618ec57d55c74bed";
    protected static final String module = "entry_notarization4994";
    protected static final String createFunction = "notarize_4994_scan";
    protected static final String updateFunction = "update_4994_scan";
    protected static String gasBudget = "5000000";

    protected static final Wallet wallet = new Wallet("iotaprivkey...");

    public IotaMetadata() {
        address = wallet.getAddress();
    }
}
