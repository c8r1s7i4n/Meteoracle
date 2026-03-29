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
