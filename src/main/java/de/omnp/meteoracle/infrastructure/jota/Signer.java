package de.omnp.meteoracle.infrastructure.jota;

import java.nio.ByteBuffer;
import java.security.Security;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Signer {
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    /**
     * TODO: Funktionsbeschreibung anpassen
     * @return Returns a 64 Byte[] Signature
     */
    public static byte[] signHash(byte[] intentMessageHash, byte[] rawPrivKey) throws Exception {
        // 1. Create PrivateKeyParameters from your 32-byte private key
        Ed25519PrivateKeyParameters privateKeyParams = new Ed25519PrivateKeyParameters(rawPrivKey, 0);

        // 2. Initialize Ed25519Signer
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKeyParams);

        // 3. Update with the 32-byte hash
        signer.update(intentMessageHash, 0, intentMessageHash.length);

        // 4. Generate the 64-byte signature
        return signer.generateSignature();
    }

    public static byte[] assembleEd25519Payload(byte[] signature64, byte[] publicKey32) {
        byte flag = 0x00; // Flag for Ed25519 Pure
        
        // 1 (flag) + 64 (sig) + 32 (pk) = 97 bytes
        ByteBuffer buffer = ByteBuffer.allocate(97);
        buffer.put(flag);
        buffer.put(signature64);
        buffer.put(publicKey32);
        
        return buffer.array();
    }
}
