/*
    Meteoracle - Industrial Supply Chain Interoperability Layer
    Copyright (C) 2026 Christian Beissmann

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Original source: https://github.com/c8r1s7i4n/Meteoracle
    For contact and support, visit: https://omnipons.de
*/

package de.omnp.meteoracle.infrastructure.spi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.omnp.meteoracle.infrastructure.jota.Wallet;

/**
 * Contains alls information needed to interact with a specific move package
 * object on-chain. | (smart contract)
 */
@Component
public class IotaMetadata {

    protected final String rpcUrl;
    protected final String packageId = "0x30db7baf5bb8432e7f875ae5277e05091effc7b0e824269ecf242cacf0eca1a4";
    protected final String module = "entry_notarization4994";
    protected final String createFunction = "notarize_4994_scan";
    protected final String updateFunction = "update_4994_scan";
    protected final String privkey;

    protected final String fullStructType;

    protected String gasBudget = "50000000";

    protected final Wallet wallet;

    // Reads the .env file due to the dotenv import (build.gradle)
    public IotaMetadata(
            @Value("${rpc.url}") String rpcUrl,
            @Value("${iota.privkey}") String privkey) {

        this.rpcUrl = rpcUrl;
        this.privkey = privkey; // Values are already populated here!
        this.wallet = new Wallet(privkey);

        fullStructType = String.format(
                "%1$s::notarization::Notarization<%1$s::%2$s::Scan>",
                packageId,
                module);
    }
}
