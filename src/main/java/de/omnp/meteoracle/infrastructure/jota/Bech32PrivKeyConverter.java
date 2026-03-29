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
