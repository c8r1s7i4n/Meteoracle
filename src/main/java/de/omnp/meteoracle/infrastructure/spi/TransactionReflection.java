package de.omnp.meteoracle.infrastructure.spi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.application.port.out.ScanReflection;
import de.omnp.meteoracle.infrastructure.ScannerMapper;
import de.omnp.meteoracle.infrastructure.api.dto.JLocationDTO;
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

    // Bean (Component) via constuctor injection
    private IotaMetadata metadata;

    private ScannerMapper mapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    private final String fullStructType;

    public TransactionReflection(IotaMetadata metadata) {
        this.metadata = metadata;
        this.fullStructType = metadata.fullStructType;
    }

    /**
     * Determine if an object is owned by the address with the same package_id and
     * needs to be updated.
     * 
     * @return the Object ID as a String
     */
    @Override
    public String reflectTransactions(String targetPackageId) {
        try {
            // 1. Request Body vorbereiten
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(metadata.wallet.getAddress(), fullStructType);

            String preparedCallBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                            "iotax_getOwnedObjects",
                            queryParams.toParamsList()));

            // 2. HTTP Request bauen
            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(metadata.rpcUrl)
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
        // 1. Liste sofort initialisieren (verhindert NullPointerException beim
        // Aufrufer)
        List<ScanPak> scans = new ArrayList<>();
        mapper = Mappers.getMapper(ScannerMapper.class);

        try {
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(metadata.wallet.getAddress(), fullStructType);

            String preparedCallBody = objectMapper.writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                            "iotax_getOwnedObjects",
                            queryParams.toParamsList()));

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(metadata.rpcUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode dataArray = rootNode.path("result").path("data");

                    if (dataArray.isArray()) {
                        for (JsonNode item : dataArray) {
                            // 1. Get the data node
                            JsonNode dataNode = item.path("data");

                            // 2. Extract the objectId
                            String objectId = dataNode.path("objectId").asText();

                            // 3. Get the "fields" node (this contains state_version_count AND state)
                            JsonNode rootFields = dataNode.path("content").path("fields");

                            // 4. Extract the version count directly from rootFields
                            String versionCount = rootFields.path("state_version_count").asText();

                            // 5. Navigate to the SCAN-INHALT (the nested data)
                            JsonNode scanFields = rootFields.path("state")
                                    .path("fields")
                                    .path("data")
                                    .path("fields");

                            if (!scanFields.isMissingNode()) {
                                ScanDTO dto = mapJsonToScan(scanFields);
                                if (dto != null) {
                                    // 6. Manually set the version count into the DTO
                                    scans.add(new ScanPak(mapper.toDomain(dto), objectId, versionCount));
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
        mapper = Mappers.getMapper(ScannerMapper.class);

        try {
            GetOwnedObjectsParams queryParams = new GetOwnedObjectsParams(metadata.wallet.getAddress(), fullStructType);

            String preparedCallBody = objectMapper.writeValueAsString(
                    new IotaCallWrapper<List<Object>>(
                            "iotax_getOwnedObjects",
                            queryParams.toParamsList()));

            RequestBody body = RequestBody.create(preparedCallBody, mediaType);
            Request request = new Request.Builder()
                    .url(metadata.rpcUrl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode rootNode = objectMapper.readTree(response.body().string());
                    JsonNode dataArray = rootNode.path("result").path("data");

                    if (dataArray.isArray()) {
                        for (JsonNode item : dataArray) {
                            JsonNode dataNode = item.path("data");

                            // 1. Get the fields node (the parent of both state and state_version_count)
                            JsonNode rootFields = dataNode.path("content").path("fields");

                            // 2. Extract the version count
                            String versionCount = rootFields.path("state_version_count").asText();

                            // 3. Navigate to the nested SCAN-INHALT
                            JsonNode scanFields = rootFields.path("state")
                                    .path("fields")
                                    .path("data")
                                    .path("fields");

                            if (!scanFields.isMissingNode()) {
                                String packageIdFromScan = scanFields.path("package_id").asText();
                                String objectId = dataNode.path("objectId").asText();

                                if (package_id.equals(packageIdFromScan)) {
                                    logger.info("Found Object ID: {}", objectId);

                                    ScanDTO dto = mapJsonToScan(scanFields);
                                    if (dto != null) {
                                        // 4. Return the mapped object immediately
                                        return new ScanPak(mapper.toDomain(dto), objectId, versionCount);
                                    }
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

    @Override
    public List<ScanPak> getScanTraceById(String package_id) {
        List<ScanPak> history = new ArrayList<>();

        // 1. Get the current object state to find the ObjectID
        ScanPak current = getScanById(package_id);
        if (current == null)
            return history;

        String objectId = current.onChainId();

        try {
            // 2. Query Transaction Blocks for this Object
            // Correct: filter and options are siblings in the same object
            ObjectNode paramsObject = objectMapper.createObjectNode();
            paramsObject.putObject("filter").put("ChangedObject", objectId);
            ObjectNode options = paramsObject.putObject("options");
            options.put("showEvents", true);
            options.put("showObjectChanges", true);

            String txQueryBody = objectMapper.writeValueAsString(
                    new IotaCallWrapper<>("iotax_queryTransactionBlocks",
                            Arrays.asList(paramsObject, null, 50, true)));
            options.put("showEvents", true);
            options.put("showObjectChanges", true);

            JsonNode txResult = executeRpcCall(txQueryBody);
            JsonNode txData = txResult.path("result").path("data");

            Long versionZeroNumber = null;

            if (txData.isArray()) {
                // START FROM THE BOTTOM: Iterate from the last index to 0
                for (int i = txData.size() - 1; i >= 0; i--) {
                    JsonNode txBlock = txData.get(i);

                    // 1. Process Events (Versions 1-N)
                    JsonNode events = txBlock.path("events");
                    for (JsonNode event : events) {
                        if (event.path("type").asText().contains("NotarizationUpdated")) {
                            JsonNode parsedJson = event.path("parsedJson");

                            // Extract the version count from the event payload
                            String versionCount = parsedJson.path("state_version_count").asText();

                            JsonNode scanFields = parsedJson.path("updated_state").path("data");

                            if (!scanFields.isMissingNode()) {
                                ScanDTO dto = mapJsonToScan(scanFields);
                                if (dto != null) {
                                    // Newest Scan at the top
                                    history.add(0, new ScanPak(mapper.toDomain(dto), objectId, versionCount));
                                }
                            }
                        }

                        // 2. Look for the "Created" change (Version 0)
                        // Since we are moving bottom-up, we likely find this in the first few
                        // iterations!
                        if (versionZeroNumber == null) {
                            JsonNode changes = txBlock.path("objectChanges");
                            for (JsonNode change : changes) {
                                if ("created".equals(change.path("type").asText()) &&
                                        objectId.equals(change.path("objectId").asText())) {

                                    versionZeroNumber = change.path("version").asLong();
                                    logger.info("Fast-found Version 0 at version: {}", versionZeroNumber);
                                    // Optimization: Once we find the type 'created', we don't need to check
                                    // objectChanges in
                                    // older blocks
                                    break;
                                }
                            }
                        }
                    }
                }

                // 3. Fetch Version 0 (Created State)
                if (versionZeroNumber != null) {
                    ScanPak v0 = fetchPastObject(objectId, versionZeroNumber);
                    if (v0 != null)
                        history.add(v0);
                }
            }

        } catch (Exception e) {
            logger.error("Failed to fetch history for package: {}", package_id, e);
            e.printStackTrace();
        }

        return history;
    }

    /**
     * Helper to fetch a specific version using iota_tryGetPastObject
     */
    private ScanPak fetchPastObject(String objectId, long version) throws IOException {
        ObjectNode options = objectMapper.createObjectNode();
        options.put("showContent", true);
        options.put("showOwner", true);

        String body = objectMapper.writeValueAsString(
                new IotaCallWrapper<>("iota_tryGetPastObject",
                        Arrays.asList(objectId, version, options)));

        JsonNode response = executeRpcCall(body);

        // 1. Get the root fields (parent of state_version_count)
        JsonNode rootFields = response.path("result").path("details").path("content").path("fields");

        // 2. Extract version count
        String versionCount = rootFields.path("state_version_count").asText();

        // 3. Navigate to scan data
        JsonNode fields = rootFields.path("state").path("fields").path("data").path("fields");

        if (!fields.isMissingNode()) {
            ScanDTO dto = mapJsonToScan(fields);
            if (dto != null) {
                return new ScanPak(mapper.toDomain(dto), objectId, versionCount);
            }
        }
        return null;
    }

    /**
     * Generic helper to execute RPC calls (Refactor your existing OkHttp logic
     * here)
     */
    private JsonNode executeRpcCall(String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, mediaType);
        Request request = new Request.Builder()
                .url(metadata.rpcUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null)
                throw new IOException("RPC Error");
            return objectMapper.readTree(response.body().string());
        }
    }

    public ScanDTO mapJsonToScan(JsonNode fields) {
        ScanDTO dto = new ScanDTO();

        // 1. Mapping der flachen Felder
        dto.setDeviceId(fields.path("device_id").asText());
        dto.setPackageId(fields.path("package_id").asText());
        dto.setType(fields.path("scan_type").asText()); // Achtung: Im RPC hieß es "scan_type"
        dto.setSymbology(fields.path("symbology").asText());
        dto.setTimestamp(fields.path("timestamp").asText());
        dto.setValue(fields.path("value").asText());

        // 2. Use the Smart Helper for Location (Fixes the NULL issue)
        dto.setLocation(mapLocation(fields.path("location")));

        // 3. Use the Smart Helper for JsonData (Unpacks the escaped String)
        dto.setJsonData(mapJsonData(fields.path("json_data")));

        // Optional: If the field exists in the current node, map it
        if (fields.has("state_version_count")) {
            dto.setStateVersion(fields.get("state_version_count").asText());
        }

        return dto;
    }

    private JLocationDTO mapLocation(JsonNode locationNode) {
        if (locationNode == null || locationNode.isMissingNode())
            return null;

        // Normalize the node
        JsonNode data = getFields(locationNode);
        JLocationDTO loc = new JLocationDTO(data.path("latitude").asDouble(), data.path("longitude").asDouble());

        return loc;
    }

    private JsonDataDTO mapJsonData(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isMissingNode())
            return null;

        // Use your existing getFields to handle the 'fields' wrapper
        JsonNode dataNode = getFields(jsonNode);

        // Extract the stringified JSON from the 'content' field
        String jsonString = dataNode.path("content").asText();

        if (jsonString == null || jsonString.isEmpty())
            return null;

        try {
            // Use the objectMapper to turn that string into the actual DTO
            return objectMapper.readValue(jsonString, JsonDataDTO.class);
        } catch (Exception e) {
            logger.error("Failed to parse nested JsonData string: " + jsonString, e);
            return null;
        }
    }

    /**
     * Safely extracts fields from both "Flat" (Events) and "Nested" (Objects) JSON
     */
    private JsonNode getFields(JsonNode node) {
        if (node == null || node.isMissingNode())
            return node;
        // If the node has a child called "fields", that's where the data lives (Object
        // style)
        // Otherwise, the node itself is the data (Event style)
        return node.has("fields") ? node.get("fields") : node;
    }

}