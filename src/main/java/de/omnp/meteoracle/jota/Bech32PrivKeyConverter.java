package de.omnp.meteoracle.jota;

import org.bitcoinj.base.Bech32;
import org.bitcoinj.base.Bech32.Bech32Bytes;
import org.bitcoinj.base.Bech32.Bech32Data;


/**
 * Convert a bech32 privKey to a Hex private key via implemented 8 to 5 bit converter
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
