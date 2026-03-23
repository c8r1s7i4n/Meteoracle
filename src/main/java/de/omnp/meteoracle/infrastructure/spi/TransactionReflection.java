package de.omnp.meteoracle.infrastructure.spi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.omnp.meteoracle.application.domain.MoveCall.NotarizationMetadata;
import de.omnp.meteoracle.application.port.ScanReflection;
import de.omnp.meteoracle.infrastructure.spi.curl.GetOwnedObjectsParams;
import de.omnp.meteoracle.infrastructure.spi.curl.IotaCallWrapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionReflection implements ScanReflection {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * Determine if an object is owned by the address with the same package_id and needs to be updated.
     * @return the Object ID as a String
     */
    @Override
    public String reflectTransactions(NotarizationMetadata metadata, String targetPackageId) {
        try {
            // 1. Request Body vorbereiten
            String fullStructType = String.format(
                    "%1$s::notarization::Notarization<%1$s::%2$s::Scan>", 
                    metadata.packageId(),
                    metadata.module()
            );
            
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(metadata.address(), fullStructType);
            
            String preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                        "iotax_getOwnedObjects", 
                        queryParams.toParamsList()
                    )
            );

            // 2. HTTP Request bauen
            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(metadata.rpcUrl())
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            // 3. Request ausführen (Try-with-resources schließt Response automatisch)
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {

                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    
                    JsonNode dataArray = rootNode.path("result").path("data");

                    if (dataArray.isArray()) {
                        for (JsonNode item : dataArray) {
                            
                            // 1. Navigation zum Scan-Objekt
                            JsonNode scanFields = item.path("data")
                                .path("content")
                                .path("fields")
                                .path("state")
                                .path("fields")
                                .path("data")
                                .path("fields");

                            // 2. Extraktion mit korrektem Case (kleines id!)
                            String packageIdFromScan = scanFields.path("package_id").asText();

                            // 3. Object ID (wichtig für den Rückgabewert)
                            String objectId = item.path("data").path("objectId").asText();

                            // // Debug-Ausgabe zur Sicherheit
                            // System.out.println("Checking Object: " + objectId + " with package_id: " + packageIdFromScan);

                            if (targetPackageId.equals(packageIdFromScan)) {
                            logger.info("Found Object ID: {}", objectId);
                            return objectId; 
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error when communicating with the IOTA RPC-Node", e);
        }

        return null;
    }   
}