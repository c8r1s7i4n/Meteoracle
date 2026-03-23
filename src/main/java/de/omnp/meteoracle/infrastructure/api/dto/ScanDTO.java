package de.omnp.meteoracle.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

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
    @Schema(description = "Additional scanned information.")
    private JsonDataDTO jsonData;

    @JsonInclude(value = Include.NON_NULL)
    @Schema(hidden = true)
    private String id;

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
}