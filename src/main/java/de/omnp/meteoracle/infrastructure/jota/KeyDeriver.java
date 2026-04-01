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

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.nightcode.crypto.bip39.Bip39;
import org.nightcode.crypto.bip39.Bip39Exception;
import org.nightcode.crypto.bip39.dictionary.Dictionary;
import org.nightcode.crypto.bip39.dictionary.EnglishDictionary;

final class KeyDeriver {



    /**
     * Generate a private Key out of the mnemonic phrase with given derivation path
     * (hardened)
     * as Long[] <br>
     * and preparing for signing with Ed25519
     * 
     * <pre>
     *  String mnemonic = fancy nerve insect rebel ...
     *  long[] path = {44, 4218, 0, 0, 0};
     * </pre>
     * 
     * @return the privkey as a byte[]
     */
    protected static byte[] keyGen(String mnemonic, long[] path) {
        // 1. Calculate seed

        Dictionary dictionary = EnglishDictionary.instance();
        Bip39 bip39 = new Bip39(dictionary);

        byte[] seed = null;
        
        try {
            seed = bip39.createSeed(mnemonic, "");

            // 2. IOTA derivation: Master Key Derivation
            // HMAC-SHA512 = Key derivation function (KDF)
            HMac hmac = new HMac(new SHA512Digest());
            // "ed25519 seed" fixed salt as of SLIP-0010 / BIP-32
            hmac.init(new KeyParameter("ed25519 seed".getBytes()));
            hmac.update(seed, 0, seed.length);
            byte[] masterOutput = new byte[64];
            hmac.doFinal(masterOutput, 0);

            byte[] currentKey = Arrays.copyOfRange(masterOutput, 0, 32);
            byte[] currentChainCode = Arrays.copyOfRange(masterOutput, 32, 64);

            // 3. IOTA Path: e.g 44'/4218'/0'/0'/0'
            // Every level must be hardened (index | 0x80000000)
            for (long segment : path) {
                byte[] result = deriveChildKey(currentKey, currentChainCode, (segment | 0x80000000L));
                currentKey = Arrays.copyOfRange(result, 0, 32);
                currentChainCode = Arrays.copyOfRange(result, 32, 64);
            }
            
            return currentKey;
        
        } catch (Bip39Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * SLIP-0010 Ed25519 Implementation
     * 
     * @param parentKey
     * @param parentChainCode
     * @param index
     * @return
     */
    private static byte[] deriveChildKey(byte[] parentKey, byte[] parentChainCode, long index) {
        // 1. Prepare Data: 0x00 || PrivateKey || Index(BigEndian)
        ByteBuffer buffer = ByteBuffer.allocate(1 + 32 + 4);
        buffer.put((byte) 0x00);
        buffer.put(parentKey);
        buffer.putInt((int) index); // Note: index must be passed as hardened (0x80000000 | i)

        // 2. Perform HMAC-SHA512
        HMac hmac = new HMac(new SHA512Digest());
        hmac.init(new KeyParameter(parentChainCode));
        hmac.update(buffer.array(), 0, buffer.array().length);
        byte[] result = new byte[64];
        hmac.doFinal(result, 0);

        // 3. Return result (First 32 bytes = Child Key, Next 32 bytes = Chain Code)
        return result;
    }
}
