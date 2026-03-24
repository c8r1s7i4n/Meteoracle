package de.omnp.meteoracle.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// Reihenfolge für die API zum anzeigen
@JsonPropertyOrder({ "package_id", "symbology", "value", "timestamp", "deviceId", "type", "location", "jsonData" })
public class ScanDTO
{
    //Diese Felder sind Pflicht-Felder für die API
    @Schema(description = "The unique package ID")
    @NotNull
    private String package_id;

    @Schema(description = "The barcode symbology used, for instance EAN-13.")
    @NotNull
    private String symbology;

    @Schema(description = "The scanned barcode ID, for instance a GTIN-13 code.")
    @NotNull
    private String value;

    @Schema(description = "The transaction timestamp generated from the device (ISO 8601).")
    @NotNull
    private String timestamp;

    @Schema(description = "The Scanner Device Id.")
    @NotNull
    private String deviceId;

    @Schema(description = "The transaction type, like 'scanTransaction'")
    @NotNull
    private String type;
    
    //Hier werden die "nested" Objekte definiert
    @Schema(description = "The Reader location.")
    @NotNull
    private JLocationDTO location;

    @JsonInclude(value = Include.NON_NULL)
    @Schema(description = "Additional scanned information. Only use these fields if necessary, as they increase the gas price.")
    private JsonDataDTO jsonData;

    @JsonInclude(value = Include.NON_NULL)
    @Schema(hidden = true)
    private String id;

    @JsonInclude(value = Include.NON_NULL)
    private String onchain_id;

    // default constructor for Jackson (testing purpose)
    public ScanDTO() {}

    public ScanDTO(String package_id, String symbology, String value, String timestamp, String deviceId, String type, JLocationDTO jLocation, JsonDataDTO jsonData)
    {
        this.package_id = package_id;

        this.symbology = symbology;
        this.value = value;

        this.location = jLocation;

        this.deviceId = deviceId;
        this.type = type;
        this.timestamp = timestamp;

        this.jsonData = jsonData;
    }

    @JsonProperty("package_id")
    public void setPackageId(String packageId) {
        this.package_id = packageId;
    }

    // Is here instead of the top variable defined because the method name does not match the declared class variable
    @JsonProperty("package_id")
    @Schema(description = "The unique package ID")
    @NotNull
    public String getPackageId() {
        return this.package_id;
    }

    public String getSymbology() {
        return symbology;
    }

    public String getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public JLocationDTO getLocation() {
        return location;
    }

    public void setLocation(JLocationDTO location) {
        this.location = location;
    }

    public JsonDataDTO getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataDTO jsonData) {
        this.jsonData = jsonData;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public void setSymbology(String symbology) {
        this.symbology = symbology;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public String getOnchainId() {
        return this.onchain_id;
    }

    /**
     * Sets the on-chain object ID
     * @param objectId
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public void setOnchainId(String onchainId) {
        this.onchain_id = onchainId;
    }
}