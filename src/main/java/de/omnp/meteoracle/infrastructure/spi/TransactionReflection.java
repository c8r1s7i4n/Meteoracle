package de.omnp.meteoracle.infrastructure.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.application.port.out.ScanReflection;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.dto.JLocationDTO;
// TODO: JsonData als raw String in der Blockchain speichern.
import de.omnp.meteoracle.infrastructure.api.dto.JsonDataDTO;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;
import de.omnp.meteoracle.infrastructure.spi.curl.GetOwnedObjectsParams;
import de.omnp.meteoracle.infrastructure.spi.curl.IotaCallWrapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class TransactionReflection implements ScanReflection {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ScannerMapper mapper;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * Determine if an object is owned by the address with the same package_id and needs to be updated.
     * @return the Object ID as a String
     */
    @Override
    public String reflectTransactions(String targetPackageId) {
        try {
            // 1. Request Body vorbereiten
            String fullStructType = String.format(
                    "%1$s::notarization::Notarization<%1$s::%2$s::Scan>", 
                    IotaMetadata.packageId,
                    IotaMetadata.module
            );
            
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(IotaMetadata.address, fullStructType);
            
            String preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                        "iotax_getOwnedObjects", 
                        queryParams.toParamsList()
                    )
            );

            // 2. HTTP Request bauen
            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(IotaMetadata.rpcUrl)
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

                            // // Debug-Ausgabe zum checken
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
    
    @Override
    public List<ScanPak> getAllScans() {
        // 1. Liste sofort initialisieren (verhindert NullPointerException beim Aufrufer)
        List<ScanPak> scans = new ArrayList<>();
        mapper = Mappers.getMapper(ScannerMapper.class);

        try {
            String fullStructType = String.format(
                    "%1$s::notarization::Notarization<%1$s::%2$s::Scan>", 
                    IotaMetadata.packageId,
                    IotaMetadata.module
            );
            
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(IotaMetadata.address, fullStructType);
            
            String preparedCallBody = objectMapper.writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                        "iotax_getOwnedObjects", 
                        queryParams.toParamsList()
                    )
            );

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(IotaMetadata.rpcUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode dataArray = rootNode.path("result").path("data");

                    if (dataArray.isArray()) {
                        for (JsonNode item : dataArray) {
                            // 1. Get the data node (contains both objectId and content)
                            JsonNode dataNode = item.path("data");
                            
                            // 2. Extract the objectId
                            String objectId = dataNode.path("objectId").asText();
                    
                            // 3. Navigate to the SCAN-INHALT
                            JsonNode scanFields = dataNode.path("content")
                                                        .path("fields")
                                                        .path("state")
                                                        .path("fields")
                                                        .path("data")
                                                        .path("fields");
                    
                            if (!scanFields.isMissingNode()) {
                                ScanDTO dto = mapJsonToScan(scanFields);
                                if (dto != null) {
                                    // 4. Manually set the objectId into the DTO
                                    dto.setOnchainId(objectId); 
                                    
                                    scans.add(new ScanPak(mapper.toDomain(dto), objectId));
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error when communicating with the IOTA RPC-Node", e);
            // Wir geben 'scans' zurück (die leer sein kann), aber nicht null!
        }
        
        return scans; 
    }

    public ScanPak getScanById(String package_id) {
        // 1. Liste sofort initialisieren (verhindert NullPointerException beim Aufrufer)
        // TODO: Eventuell eine Liste zurückgeben?
        List<Scan> scans = new ArrayList<>();
        mapper = Mappers.getMapper(ScannerMapper.class);

        try {
            String fullStructType = String.format(
                    "%1$s::notarization::Notarization<%1$s::%2$s::Scan>", 
                    IotaMetadata.packageId,
                    IotaMetadata.module
            );
            
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(IotaMetadata.address, fullStructType);
            
            String preparedCallBody = objectMapper.writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                        "iotax_getOwnedObjects", 
                        queryParams.toParamsList()
                    )
            );

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(IotaMetadata.rpcUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode dataArray = rootNode.path("result").path("data");

                    if (dataArray.isArray()) {
                        for (JsonNode item : dataArray) {
                            // NAVIGATION ZUM SCAN-INHALT:
                            // Der Pfad laut deinem cURL: data -> content -> fields -> state -> fields -> data -> fields
                            JsonNode scanFields = item.path("data")
                                                    .path("content")
                                                    .path("fields")
                                                    .path("state")
                                                    .path("fields")
                                                    .path("data")
                                                    .path("fields");

                            // Ab hier die Selektion
                            // 2. Extraktion mit korrektem Case (kleines id!)
                            String packageIdFromScan = scanFields.path("package_id").asText();

                            // 3. Object ID (wichtig für den Rückgabewert)
                            String objectId = item.path("data").path("objectId").asText();

                            // // Debug-Ausgabe zum checken
                            // System.out.println("Checking Object: " + objectId + " with package_id: " + packageIdFromScan);

                            if (package_id.equals(packageIdFromScan)) {
                                logger.info("Found Object ID: {}", objectId);

                                // Nur verarbeiten, wenn wir wirklich am Ziel angekommen sind
                                if (!scanFields.isMissingNode()) {
                                    ScanDTO dto = mapJsonToScan(scanFields);
                                    return new ScanPak(mapper.toDomain(dto), objectId);
                                }
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


    public ScanDTO mapJsonToScan(JsonNode fields) {
        
        ScanDTO dto = new ScanDTO();
    
        // Mapping der flachen Felder
        dto.setDeviceId(fields.path("device_id").asText());
        dto.setPackageId(fields.path("package_id").asText());
        dto.setType(fields.path("scan_type").asText());
        dto.setSymbology(fields.path("symbology").asText());
        dto.setTimestamp(fields.path("timestamp").asText());
        dto.setValue(fields.path("value").asText());

        // Mapping der Location (da diese ein Unter-Objekt ist)
        JsonNode locationFields = fields.path("location").path("fields");
        if (!locationFields.isMissingNode()) {
            // Falls dein ScanDTO Felder für Lat/Lon hat:
            dto.setLocation(new JLocationDTO(locationFields.path("latitude").asDouble(), locationFields.path("longitude").asDouble()));
        }

        return dto;
    }
}