package de.omnp.meteoracle.application.domain.vda4994;

import com.google.gson.annotations.SerializedName;                                                  //Hier wird mit gson (Retrofit) gearbeitet, welches die Java Objekte in JSON konvertiert. 

public class Post
{
    //Diese Felder sind Pflicht-Felder für die API
    private String symbology;
    private String value;
    private String timestamp;
    private String deviceId;
    private String type;

    //Hier werden die "nested" Objekte definiert
    private JLocation location;
    private JsonData jsonData;

    @SerializedName("id")                                                                           //Sollte der Variablenname nicht mit dem Json objekt übereinstimmen, kann man dies durch die Notation "@SerializedName()" angeben
    private String id;

    // default constructor for Jackson
    public Post() {}

    // TODO Eventuell noch @NotNull definieren
    public Post(String symbology, String value, String timestamp, String deviceId, String type, JLocation jLocation, JsonData jsonData)
    {
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

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public JsonData getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonData jsonData) {
        this.jsonData = jsonData;
    }
}
