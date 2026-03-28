package de.omnp.meteoracle.infrastructure.jota;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import java.nio.ByteBuffer;

public class IntentMessage {

    public static final int TRANSACTION_DATA = 0;
    public static final int TRANSACTION_EFFECTS = 1;
    public static final int CHECKPOINT_SUMMARY = 2;
    public static final int PERSONAL_MESSAGE = 3;
    public static final int SENDER_SIGNED_TRANSACTION = 4;
    public static final int PROOF_OF_POSSESSION = 5;
    public static final int BRIDGE_EVENT_DEPRECATED = 6;
    public static final int CONSENSUS_BLOCK = 7;
    public static final int DISCOVERY_PEERS = 8;
    public static final int AUTHORITY_CAPABILITIES = 9;

    /**
     * Including Blake2b hashing
     * 
     * @param constant
     * @param tx_bytes
     * @return Blake2b Hash of the IntentMessage as 32 byte[]
     */
    public static byte[] createIntentMessage(int constant, byte[] tx_bytes) {
        if (constant == TRANSACTION_DATA) {
            // The 3-byte Intent prefix
            byte[] intent = new byte[] { 0x00, 0x00, 0x00 };
            // Combine: Intent + BCS_Serialized_Data
            byte[] intentMessage = ByteBuffer.allocate(intent.length + tx_bytes.length)
                    .put(intent)
                    .put(tx_bytes)
                    .array();

            // Returns the hashed intentMessage
            return blake2bHashing(intentMessage);
        } else {
            return null;
        }
    }

    private static byte[] blake2bHashing(byte[] intentMessage) {
        // 256 bits = 32 bytes
        // The constructor takes the digest size in BITS

        Blake2bDigest digest = new Blake2bDigest(256);

        digest.update(intentMessage, 0, intentMessage.length);

        byte[] hash = new byte[digest.getDigestSize()]; // This will be 32 bytes
        digest.doFinal(hash, 0);

        return hash;
    }
}
