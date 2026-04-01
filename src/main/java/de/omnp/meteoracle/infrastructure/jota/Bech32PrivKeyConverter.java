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

package de.omnp.meteoracle.infrastructure.jota;

import org.bitcoinj.base.Bech32;
import org.bitcoinj.base.Bech32.Bech32Bytes;
import org.bitcoinj.base.Bech32.Bech32Data;

/**
 * Convert a bech32 privKey to a Hex private key via implemented 8 to 5 bit
 * converter
 */
public class Bech32PrivKeyConverter {

    public static String bech32ToHex(String bech32Str) throws Exception {

        // Dekodiere Bech32-String
        Bech32Data bech32Data = Bech32.decode(bech32Str);
        // Konvertiere 5-Bit-Gruppen in Bytes (8-Bit)
        Bech32Bytes bech32Bytes = (Bech32Bytes) bech32Data;
        byte[] dataBytes = bech32Bytes.decode5to8();
        // In Hex umwandeln
        StringBuilder hex = new StringBuilder();
        for (byte b : dataBytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
