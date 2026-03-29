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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.omnp.meteoracle.application.port.out.ScanSender;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.dto.JsonDataDTO;
import de.omnp.meteoracle.infrastructure.jota.IntentMessage;
import de.omnp.meteoracle.infrastructure.jota.Signer;
import de.omnp.meteoracle.infrastructure.spi.curl.IotaCallWrapper;
import de.omnp.meteoracle.infrastructure.spi.curl.MoveCallParam;
import de.omnp.meteoracle.infrastructure.spi.curl.UnsafeMoveCallParam;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class TransactionAdapter implements ScanSender {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // Bean (Component) via constuctor injection
    private IotaMetadata metadata;

    private ScannerMapper mapper;

    private ObjectMapper objectMapper;

    public TransactionAdapter(ScannerMapper mapper, IotaMetadata metadata) {
        this.mapper = mapper;
        this.metadata = metadata;
        this.objectMapper = new ObjectMapper();
    }

    // Preparing the object and metadata
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    /**
     * Sends an unsafe move_call through the Transaction Builder API of the RPC-Node
     * <br>
     * Prepares the tx_bytes for signing.
     */
    @Override
    public boolean sendTransaction(Scan scan) {
        String txBytes = prepareTransaction(scan);

        if (txBytes != null) {
            List<String> signature = new ArrayList<>();
            String sig = sign(Base64.getDecoder().decode(txBytes));
            signature.add(sig);
            if (sig != null) {
                if (executeTransaction(txBytes, signature)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                logger.error("Error while signing the transaction");
            }
        } else {
            logger.error("Error while getting the tx_bytes - (Probably too less Gas)");
        }
        return false;
    }

    private String prepareTransaction(Scan scan) {

        String preparedCallBody = null;
        List<String> data = null;

        // The order of execution is strictly enforced as defined in the smart contract
        // entry function. | (create)
        data = new ArrayList<String>();
        data.add(scan.getPackageId());
        data.add(scan.getSymbology());
        data.add(scan.getValue());
        data.add(scan.getTimestamp());
        data.add(scan.getDeviceId());
        data.add(scan.getType());
        data.add(String.valueOf(scan.getLocation().getLatitude()));
        data.add(String.valueOf(scan.getLocation().getLongitude()));
        try {
            JsonDataDTO dataDTO = mapper.toDto(scan.getJsonData());
            data.add(objectMapper.writeValueAsString(dataDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            data.add(null);
        }
        data.add("0x6"); // System clock address

        try {
            // Generates the Request Body
            preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<UnsafeMoveCallParam>("unsafe_moveCall", new UnsafeMoveCallParam(
                            metadata.wallet.getAddress(), metadata.packageId, metadata.module, metadata.createFunction,
                            metadata.gasBudget, data)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(preparedCallBody, mediaType);

        Request request = new Request.Builder()
                .url(metadata.rpcUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string(); // Einmal lesen
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode resultNode = rootNode.path("result");

                if (!resultNode.isMissingNode()) {
                    String txBytes = resultNode.path("txBytes").asText();
                    return txBytes;
                } else {
                    logger.error("Could not find result field in response");
                }
            } else {
                logger.error(
                        "Error sending the transaction into the network. RPC node response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // For the updating process
    private String prepareTransaction(Scan post, String objectAddress) {

        String preparedCallBody = null;
        List<String> data = null;

        // The order of execution is strictly enforced as defined in the smart contract
        // entry function. | (update)
        data = new ArrayList<String>();
        data.add(objectAddress);
        data.add(post.getPackageId());
        data.add(post.getSymbology());
        data.add(post.getValue());
        data.add(post.getTimestamp());
        data.add(post.getDeviceId());
        data.add(post.getType());
        data.add(String.valueOf(post.getLocation().getLatitude()));
        data.add(String.valueOf(post.getLocation().getLongitude()));
        try {
            // 1. Convert the Domain object (from the Scan) into the DTO (JsonDataDTO)
            // MapStruct does this. This is the object that has the @JsonInclude annotation.
            JsonDataDTO dataDto = mapper.toDto(post.getJsonData());
        
            // 2. Turn the DTO into a String using the INJECTED ObjectMapper.
            // Jackson will now see the @JsonInclude and REMOVE the null fields.
            String jsonString = objectMapper.writeValueAsString(dataDto);
        
            data.add(jsonString);
        } catch (JsonProcessingException e) {
            logger.error("Serialization failed", e);
            data.add(null);
        }
        data.add("0x6"); // System clock address

        try {
            // Generates the Request Body
            preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<UnsafeMoveCallParam>("unsafe_moveCall", new UnsafeMoveCallParam(
                            metadata.wallet.getAddress(), metadata.packageId, metadata.module, metadata.updateFunction,
                            metadata.gasBudget, data)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(preparedCallBody, mediaType);

        Request request = new Request.Builder()
                .url(metadata.rpcUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {

                String responseBody = response.body().string(); // Einmal lesen
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode resultNode = rootNode.path("result");

                if (!resultNode.isMissingNode()) {
                    String txBytes = resultNode.path("txBytes").asText();
                    return txBytes;
                } else {
                    logger.error("Could not find result field in response");
                }
            } else {
                logger.error(
                        "Error sending the transaction into the network. RPC node response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private String sign(byte[] txBytes) {
        try {
            byte[] intentMessageHash = IntentMessage.createIntentMessage(IntentMessage.TRANSACTION_DATA, txBytes);
            byte[] signature = Signer.signHash(intentMessageHash, metadata.wallet.getRawPrivKey());
            byte[] payload = Signer.assembleEd25519Payload(signature, metadata.wallet.getRawPublicKey());

            return Base64.getEncoder().encodeToString(payload);

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    private boolean executeTransaction(String txBytes, List<String> signatures) {
        try {
            String preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(new IotaCallWrapper<MoveCallParam>("iota_executeTransactionBlock",
                            new MoveCallParam(txBytes, signatures)));

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);

            Request request = new Request.Builder()
                    .url(metadata.rpcUrl)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return true;
                } else {
                    logger.error("Error sending the transaction into the network. RPC node response code: "
                            + response.code());
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTransaction(Scan post, String objectAddress) {

        String txBytes = prepareTransaction(post, objectAddress);

        if (txBytes != null) {
            List<String> signature = new ArrayList<>();
            String sig = sign(Base64.getDecoder().decode(txBytes));
            signature.add(sig);
            if (sig != null) {
                if (executeTransaction(txBytes, signature)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                logger.error("Error while signing the transaction");
            }
        } else {
            logger.error("Error while getting the tx_bytes when updating (Probably too less Gas)");
        }
        return false;
    }
}
