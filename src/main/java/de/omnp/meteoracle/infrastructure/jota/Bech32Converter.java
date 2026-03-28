package de.omnp.meteoracle.infrastructure.jota;

import java.io.ByteArrayOutputStream;

/**
 * Convert a Hex privKey to bech32 private key via implemented 8 to 5 bit
 * converter
 */
public class Bech32Converter {

    private static final String ALPHABET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    /** Converting (Hex) privKey to bech32 privKey */
    protected static String encodeBech32(String hrp, String hexInput) {
        byte[] data = hexToBytes(hexInput);

        // 1. Konvertierung von 8-bit zu 5-bit
        byte[] converted = convertBits(data, 8, 5, true);

        // 2. Bech32 Prüfsumme erstellen
        byte[] checksum = createChecksum(hrp, converted);

        // 3. Alles zusammenfügen und encodieren
        StringBuilder sb = new StringBuilder();
        sb.append(hrp).append("1");
        for (byte b : converted) {
            sb.append(ALPHABET.charAt(b));
        }
        for (byte b : checksum) {
            sb.append(ALPHABET.charAt(b));
        }
        return sb.toString();
    }

    /**
     * Converts the bytes of the privKey derived from the MasterKey
     * 
     * @param data
     * @param from
     * @param to
     * @param pad
     * @return
     */
    private static byte[] convertBits(byte[] data, int from, int to, boolean pad) {
        int acc = 0;
        int bits = 0;
        int maxv = (1 << to) - 1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte b : data) {
            int value = b & 0xff;
            acc = (acc << from) | value;
            bits += from;
            while (bits >= to) {
                bits -= to;
                out.write((acc >> bits) & maxv);
            }
        }
        if (pad && bits > 0) {
            out.write((acc << (to - bits)) & maxv);
        }
        return out.toByteArray();
    }

    private static byte[] createChecksum(String hrp, byte[] data) {
        byte[] values = expandHrp(hrp);
        byte[] combined = new byte[values.length + data.length + 6];
        System.arraycopy(values, 0, combined, 0, values.length);
        System.arraycopy(data, 0, combined, values.length, data.length);
        int mod = polymod(combined) ^ 1;
        byte[] ret = new byte[6];
        for (int i = 0; i < 6; i++) {
            ret[i] = (byte) ((mod >> (5 * (5 - i))) & 31);
        }
        return ret;
    }

    private static int polymod(byte[] values) {
        int[] GENERATOR = { 0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3 };
        int chk = 1;
        for (byte b : values) {
            int top = chk >> 25;
            chk = ((chk & 0x1ffffff) << 5) ^ (b & 0xff);
            for (int i = 0; i < 5; i++) {
                if (((top >> i) & 1) == 1)
                    chk ^= GENERATOR[i];
            }
        }
        return chk;
    }

    private static byte[] expandHrp(String hrp) {
        int len = hrp.length();
        byte[] ret = new byte[len * 2 + 1];
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (hrp.charAt(i) >> 5);
            ret[i + len + 1] = (byte) (hrp.charAt(i) & 31);
        }
        return ret;
    }

    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
