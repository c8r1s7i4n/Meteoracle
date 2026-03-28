package de.omnp.meteoracle.infrastructure.jota;

import java.util.Base64;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;

/**
 * IOTA Wallet based on Ed25519 via SLIP-0010 <br>
 * Specification: <br>
 * - BIP-32 + SLIP-0010 <br>
 * - BIP-39 <br>
 * - BIP-44 <br>
 * <br>
 * According to:
 * https://docs.iota.org/developer/cryptography/transaction-auth/keys-addresses
 */
public class Wallet {

    private String mnemonic;
    private long[] path;
    private byte[] rawPrivKey;
    private byte[] rawPublicKey;
    private String bech32Key;
    private String publicBase64Key;
    private String address;

    /**
     * Generate an IOTA Wallet from mnemonic phrase
     * 
     * @param mnemonic
     * @param path
     */
    public Wallet(String mnemonic, long[] path) {
        this.mnemonic = mnemonic;
        this.path = path;
        setRawPrivKey();
        bech32KeyGen();
        setPublicKeyz();
        setIotaAddress();
    }

    /**
     * Generate an IOTA Wallet from Bech32 privkey starting with "iotaprivkey"
     * 
     * @param bech32Key
     */
    public Wallet(String bech32Key) {
        this.bech32Key = bech32Key;
        setRawPrivKey(bech32Key);
        setPublicKeyz();
        setIotaAddress();
    }

    protected void setRawPrivKey() {
        this.rawPrivKey = KeyDeriver.keyGen(mnemonic, path);
    }

    /**
     * From Bech32 privKey to (Hex) Raw privKey
     */
    protected void setRawPrivKey(String bech32Key) {
        String hexOutput;
        try {
            hexOutput = Bech32PrivKeyConverter.bech32ToHex(bech32Key);
            if (hexOutput.startsWith("00")) {
                // Decode the string (which is already 00-stripped) back into the original
                // binary bytes
                this.rawPrivKey = Hex.decode(hexOutput.substring(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setPublicKeyz() {
        // 4. Final Public Key derivation for IOTA
        // The Identity is derived from the expanded scalar
        Ed25519PrivateKeyParameters privParams = new Ed25519PrivateKeyParameters(this.rawPrivKey, 0);
        Ed25519PublicKeyParameters pubParams = privParams.generatePublicKey();

        byte[] publicKeyBytes = new byte[32];
        pubParams.encode(publicKeyBytes, 0);

        this.rawPublicKey = publicKeyBytes;

        this.publicBase64Key = Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    protected void setIotaAddress() {
        // 1. Initialize BLAKE2b with 256 bits (32 bytes) output
        Blake2bDigest blake2b = new Blake2bDigest(256);

        // 2. Ed25519 Exception: DO NOT prepend any flag bytes
        blake2b.update(this.rawPublicKey, 0, this.rawPublicKey.length);

        // 3. Finalize and extract the 32-byte address
        byte[] addressBytes = new byte[32];
        blake2b.doFinal(addressBytes, 0);

        this.address = "0x" + Hex.toHexString(addressBytes);
    }

    /**
     * Generate Bech32 key from rawPrivKey
     * 
     * @param currentKey
     */
    protected void bech32KeyGen() {
        try {
            // Prepend flag bytes of 0x00 for Ed25519
            this.bech32Key = Bech32Converter.encodeBech32("iotaprivkey", "00" + Hex.toHexString(this.rawPrivKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getRawPrivKey() {
        return this.rawPrivKey;
    }

    public byte[] getRawPublicKey() {
        return this.rawPublicKey;
    }

    public String getBech32Key() {
        return this.bech32Key;
    }

    public String getPublicBase64Key() {
        return this.publicBase64Key;
    }

    public String getAddress() {
        return this.address;
    }
}
