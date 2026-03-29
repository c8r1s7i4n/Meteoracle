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

package de.omnp.meteoracle.domain.vda4994;

public class Scan
{
    // Unique identifier
    private String package_id;
    
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
    public Scan() {}

    public Scan(String package_id, String symbology, String value, String timestamp, String deviceId, String type, JLocation jLocation, JsonData jsonData)
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
        return this.package_id;
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
        this.package_id = packageId;
    }
}
