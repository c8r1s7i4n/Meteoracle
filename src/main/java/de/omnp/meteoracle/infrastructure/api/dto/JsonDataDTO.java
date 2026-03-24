package de.omnp.meteoracle.infrastructure.api.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_NULL)
public class JsonDataDTO {

    @JsonProperty("specification_ID")
    private String specification_ID;

    @JsonProperty("specification_version")
    private String specification_version;

    @JsonProperty("supplier_number")
    private String supplier_number;

    @JsonProperty("country_of_origin")
    private String country_of_origin;

    @JsonProperty("receiver_plant")
    private String receiver_plant;

    @JsonProperty("unloading_point")
    private String unloading_point;

    @JsonProperty("storage_location")
    private String storage_location;

    @JsonProperty("supplier_number_of_the_Seller")
    private String supplier_number_of_the_Seller;

    @JsonProperty("delivery_note_number")
    private String delivery_note_number;

    @JsonProperty("shoring_location")
    private String shoring_location;

    @JsonProperty("custom_routing")
    private String custom_routing;

    @JsonProperty("expected_arrival_date")
    private String expected_arrival_date;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("unit_of_measure")
    private String unit_of_measure;

    @JsonProperty("gross_weight")
    private String gross_weight;

    @JsonProperty("item_number")
    private String item_number;

    @JsonProperty("packing_material_type")
    private String packing_material_type;

    @JsonProperty("expiration_date")
    private String expiration_date;

    @JsonProperty("production_date")
    private String production_date;

    @JsonProperty("batch")
    private String batch;

    @JsonProperty("hardware_status")
    private String hardware_status;

    @JsonProperty("software_status")
    private String software_status;

    @JsonProperty("change_status")
    private String change_status;

    @JsonProperty("additional_information_to_the_Part_number")
    private String additional_information_to_the_Part_number;

    @JsonProperty("roHS_Directive")
    private String roHS_Directive;

    @JsonProperty("supplier_of_this_position")
    private String supplier_of_this_position;

    @JsonProperty("manufacturer_part_number")
    private String manufacturer_part_number;

    @JsonProperty("smallest_material_Packaging_unit")
    private String smallest_material_Packaging_unit;

    //dient für das entgegennehmen des automatisch erzeugten Zeitstempels von der API
    @JsonProperty("timestamp")
    private String timestamp;
    

    public JsonDataDTO() {}

    public JsonDataDTO(String[][][] di)
    {
        unpackData(di);    
    }

    public void unpackData(String[][][] di)
    {
        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")].equals(""))                                //Durch die "und" verbindung wird die zweite abfrage nur überprüft wenn die erste abfrage nicht zutrifft (Sonst error wenn "equals" von null Objekt ausgeführt wird)
        {this.specification_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")].equals(""))
        {this.specification_version = di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number")].equals(""))
        {this.supplier_number = di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Country of origin")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Country of origin")].equals(""))
        {this.country_of_origin = di[0][2][Arrays.asList(di[0][1]).indexOf("Country of origin")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Receiver plant")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Receiver plant")].equals(""))
        {this.receiver_plant = di[0][2][Arrays.asList(di[0][1]).indexOf("Receiver plant")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Unloading point")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Unloading point")].equals(""))
        {this.unloading_point = di[0][2][Arrays.asList(di[0][1]).indexOf("Unloading point")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Storage location")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Storage location")].equals(""))
        {this.storage_location = di[0][2][Arrays.asList(di[0][1]).indexOf("Storage location")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number of the Seller")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number of the Seller")].equals(""))
        {this.supplier_number_of_the_Seller = di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number of the Seller")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Delivery note number")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Delivery note number")].equals(""))
        {this.delivery_note_number = di[0][2][Arrays.asList(di[0][1]).indexOf("Delivery note number")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Shoring location / point of use")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Shoring location / point of use")].equals(""))
        {this.shoring_location = di[0][2][Arrays.asList(di[0][1]).indexOf("Shoring location / point of use")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Custom routing")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Custom routing")].equals(""))
        {this.custom_routing = di[0][2][Arrays.asList(di[0][1]).indexOf("Custom routing")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Expected arrival date")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Expected arrival date")].equals(""))
        {this.expected_arrival_date = di[0][2][Arrays.asList(di[0][1]).indexOf("Expected arrival date")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Quantity")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Quantity")].equals(""))
        {this.quantity = di[0][2][Arrays.asList(di[0][1]).indexOf("Quantity")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Unit of measure")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Unit of measure")].equals(""))
        {this.unit_of_measure = di[0][2][Arrays.asList(di[0][1]).indexOf("Unit of measure")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Gross weight")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Gross weight")].equals(""))
        {this.gross_weight = di[0][2][Arrays.asList(di[0][1]).indexOf("Gross weight")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Item number")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Item number")].equals(""))
        {this.item_number = di[0][2][Arrays.asList(di[0][1]).indexOf("Item number")];}

        // if (di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID J")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID J")].equals(""))
        // {this.package_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID J")];}

        // if (di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 1J")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 1J")].equals(""))
        // {this.package_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 1J")];}

        // if (di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 5J")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 5J")].equals(""))
        // {this.package_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 5J")];}

        // if (di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 6J")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 6J")].equals(""))
        // {this.package_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Package ID 6J")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Packing material type")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Packing material type")].equals(""))
        {this.packing_material_type = di[0][2][Arrays.asList(di[0][1]).indexOf("Packing material type")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Expiration date")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Expiration date")].equals(""))
        {this.expiration_date = di[0][2][Arrays.asList(di[0][1]).indexOf("Expiration date")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Production date")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Production date")].equals(""))
        {this.production_date = di[0][2][Arrays.asList(di[0][1]).indexOf("Production date")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Batch")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Batch")].equals(""))
        {this.batch = di[0][2][Arrays.asList(di[0][1]).indexOf("Batch")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Hardware status")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Hardware status")].equals(""))
        {this.hardware_status = di[0][2][Arrays.asList(di[0][1]).indexOf("Hardware status")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Software status")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Software status")].equals(""))
        {this.software_status = di[0][2][Arrays.asList(di[0][1]).indexOf("Software status")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Change status")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Change status")].equals(""))
        {this.change_status = di[0][2][Arrays.asList(di[0][1]).indexOf("Change status")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Additional information to the Part number")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Additional information to the Part number")].equals(""))
        {this.additional_information_to_the_Part_number = di[0][2][Arrays.asList(di[0][1]).indexOf("Additional information to the Part number")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("RoHS Directive")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("RoHS Directive")].equals(""))
        {this.roHS_Directive = di[0][2][Arrays.asList(di[0][1]).indexOf("RoHS Directive")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier of this position (if necessary different from supplier)")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier of this position (if necessary different from supplier)")].equals(""))
        {this.supplier_of_this_position = di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier of this position (if necessary different from supplier)")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Manufacturer's part number")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Manufacturer's part number")].equals(""))
        {this.manufacturer_part_number = di[0][2][Arrays.asList(di[0][1]).indexOf("Manufacturer's part number")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("smallest material Packaging unit")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("smallest material Packaging unit")].equals(""))
        {this.smallest_material_Packaging_unit = di[0][2][Arrays.asList(di[0][1]).indexOf("smallest material Packaging unit")];}
    }

    //Sollte kein Wert inizialisiert werden, wird "null" returned
    public String getSpecification_ID() {
        return specification_ID;
    }

    public String getSpecification_version() {
        return specification_version;
    }

    public String getSupplier_number() {
        return supplier_number;
    }

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public String getReceiver_plant() {
        return receiver_plant;
    }

    public String getUnloading_point() {
        return unloading_point;
    }

    public String getStorage_location() {
        return storage_location;
    }

    public String getSupplier_number_of_the_Seller() {
        return supplier_number_of_the_Seller;
    }

    public String getDelivery_note_number() {
        return delivery_note_number;
    }

    public String getShoring_location() {
        return shoring_location;
    }

    public String getCustom_routing() {
        return custom_routing;
    }

    public String getExpected_arrival_date() {
        return expected_arrival_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public String getGross_weight() {
        return gross_weight;
    }

    public String getItem_number() {
        return item_number;
    }

    // public String getPackage_ID() {
    //     return package_ID;
    // }

    public String getPacking_material_type() {
        return packing_material_type;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public String getProduction_date() {
        return production_date;
    }

    public String getBatch() {
        return batch;
    }

    public String getHardware_status() {
        return hardware_status;
    }

    public String getSoftware_status() {
        return software_status;
    }

    public String getChange_status() {
        return change_status;
    }

    public String getAdditional_information_to_the_Part_number() {
        return additional_information_to_the_Part_number;
    }

    public String getRoHS_Directive() {
        return roHS_Directive;
    }

    public String getSupplier_of_this_position() {
        return supplier_of_this_position;
    }

    public String getManufacturer_part_number() {
        return manufacturer_part_number;
    }

    public String getSmallest_material_Packaging_unit() {
        return smallest_material_Packaging_unit;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    // For Jackson to serialize/deserialze

    public void setSpecification_ID(String specification_ID) {
        this.specification_ID = specification_ID;
    }

    public void setSpecification_version(String specification_version) {
        this.specification_version = specification_version;
    }

    public void setSupplier_number(String supplier_number) {
        this.supplier_number = supplier_number;
    }

    public void setCountry_of_origin(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }

    public void setReceiver_plant(String receiver_plant) {
        this.receiver_plant = receiver_plant;
    }

    public void setUnloading_point(String unloading_point) {
        this.unloading_point = unloading_point;
    }

    public void setStorage_location(String storage_location) {
        this.storage_location = storage_location;
    }

    public void setSupplier_number_of_the_Seller(String supplier_number_of_the_Seller) {
        this.supplier_number_of_the_Seller = supplier_number_of_the_Seller;
    }

    public void setDelivery_note_number(String delivery_note_number) {
        this.delivery_note_number = delivery_note_number;
    }

    public void setShoring_location(String shoring_location) {
        this.shoring_location = shoring_location;
    }

    public void setCustom_routing(String custom_routing) {
        this.custom_routing = custom_routing;
    }

    public void setExpected_arrival_date(String expected_arrival_date) {
        this.expected_arrival_date = expected_arrival_date;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public void setGross_weight(String gross_weight) {
        this.gross_weight = gross_weight;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public void setPacking_material_type(String packing_material_type) {
        this.packing_material_type = packing_material_type;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setHardware_status(String hardware_status) {
        this.hardware_status = hardware_status;
    }

    public void setSoftware_status(String software_status) {
        this.software_status = software_status;
    }

    public void setChange_status(String change_status) {
        this.change_status = change_status;
    }

    public void setAdditional_information_to_the_Part_number(String additional_information_to_the_Part_number) {
        this.additional_information_to_the_Part_number = additional_information_to_the_Part_number;
    }

    public void setRoHS_Directive(String roHS_Directive) {
        this.roHS_Directive = roHS_Directive;
    }

    public void setSupplier_of_this_position(String supplier_of_this_position) {
        this.supplier_of_this_position = supplier_of_this_position;
    }

    public void setManufacturer_part_number(String manufacturer_part_number) {
        this.manufacturer_part_number = manufacturer_part_number;
    }

    public void setSmallest_material_Packaging_unit(String smallest_material_Packaging_unit) {
        this.smallest_material_Packaging_unit = smallest_material_Packaging_unit;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
   
}