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
