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

    // Preparing the object and metadata
    private static final ObjectMapper objectMapper = new ObjectMapper();
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

    // TODO: Hier noch den scan (post) miteinbeziehen
    private String prepareTransaction(Scan scan) {
        
        String preparedCallBody = null;
        List<String> data = null;

        // In dieser Reihenfolge! Wie im Vertrag (SC) definiert. (Create)
        data = new ArrayList<String>();
        data.add(scan.getPackageId());
        data.add(scan.getSymbology());
        data.add(scan.getValue());
        data.add(scan.getTimestamp());
        data.add(scan.getDeviceId());
        data.add(scan.getType());
        data.add(String.valueOf(scan.getLocation().getLatitude()));
        data.add(String.valueOf(scan.getLocation().getLongitude()));
        data.add("0x6");    // System clock address
        
        try {
            // Generates the Request Body
            preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<UnsafeMoveCallParam>("unsafe_moveCall", new UnsafeMoveCallParam(
                            IotaMetadata.address, IotaMetadata.packageId, IotaMetadata.module, IotaMetadata.createFunction, IotaMetadata.gasBudget, data)));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   

        RequestBody body = RequestBody.create(preparedCallBody, mediaType);

        Request request = new Request.Builder()
                .url(IotaMetadata.rpcUrl)
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // For the updating process
    private String prepareTransaction(Scan post, String objectAddress) {
        
        String preparedCallBody = null;
        List<String> data = null;

        // In dieser Reihenfolge! Wie im Vertrag (SC) definiert. (Update)
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
        data.add("0x6");    // System clock address

        try {
            // Generates the Request Body
            preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<UnsafeMoveCallParam>("unsafe_moveCall", new UnsafeMoveCallParam(
                        IotaMetadata.address, IotaMetadata.packageId, IotaMetadata.module, IotaMetadata.updateFunction, IotaMetadata.gasBudget, data)));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   

        RequestBody body = RequestBody.create(preparedCallBody, mediaType);

        Request request = new Request.Builder()
                .url(IotaMetadata.rpcUrl)
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private String sign(byte[] txBytes) {
        try {
            byte[] intentMessageHash = IntentMessage.createIntentMessage(IntentMessage.TRANSACTION_DATA, txBytes);
            byte[] signature = Signer.signHash(intentMessageHash, IotaMetadata.wallet.getRawPrivKey());
            byte[] payload = Signer.assembleEd25519Payload(signature, IotaMetadata.wallet.getRawPublicKey());

            return Base64.getEncoder().encodeToString(payload);

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    // TODO: Eventuell noch einen dry-run vorher
    private boolean executeTransaction(String txBytes, List<String> signatures) {
        try {
            String preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(new IotaCallWrapper<MoveCallParam>("iota_executeTransactionBlock",
                            new MoveCallParam(txBytes, signatures)));

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);

            Request request = new Request.Builder()
                    .url(IotaMetadata.rpcUrl)
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
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
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
