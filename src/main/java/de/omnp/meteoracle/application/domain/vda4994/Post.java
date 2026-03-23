package de.omnp.meteoracle.application.domain.vda4994;

public class Post
{
    // Unique identifier
    private String package_ID;
    
    private String symbology;
    private String value;
    private String timestamp;
    private String deviceId;
    private String type;
    
    //Hier werden die "nested" Objekte definiert
    private JLocation location;
    private JsonData jsonData;

    private String id;

    // default constructor for Jackson (testing purpose)
    public Post() {}

    public Post(String package_ID, String symbology, String value, String timestamp, String deviceId, String type, JLocation jLocation, JsonData jsonData)
    {
        this.package_ID = package_ID;

        this.symbology = symbology;
        this.value = value;

        this.location = jLocation;

        this.deviceId = deviceId;
        this.type = type;
        this.timestamp = timestamp;

        this.jsonData = jsonData;
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

    public JLocation getLocation() {
        return location;
    }

    public String getPackageId() {
        return this.package_ID;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    // Setter sind wichtig für den Mapper (mapstruct) (Interface)

    public JsonData getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonData jsonData) {
        this.jsonData = jsonData;
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

    public void setPackageId(String packageId) {
        this.package_ID = packageId;
    }
}
