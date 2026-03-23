package de.omnp.meteoracle.domain.vda4994;

import java.util.Arrays;

public class JsonData {

    private String specification_ID;
    private String specification_version;
    private String supplier_number;
    private String country_of_origin;
    private String receiver_plant;
    private String unloading_point;
    private String storage_location;
    private String supplier_number_of_the_Seller;
    private String delivery_note_number;
    private String shoring_location;
    private String custom_routing;
    private String expected_arrival_date;
    private String quantity;
    private String unit_of_measure;
    private String gross_weight;
    private String item_number;
    // private String package_ID;
    private String packing_material_type;
    private String expiration_date;
    private String production_date;
    private String batch;
    private String hardware_status;
    private String software_status;
    private String change_status;
    private String additional_information_to_the_Part_number;
    private String roHS_Directive;
    private String supplier_of_this_position;
    private String manufacturer_part_number;
    private String smallest_material_Packaging_unit;

    //dient für das entgegennehmen des automatisch erzeugten Zeitstempels von der API
    private String timestamp;

    public JsonData() {}

    public JsonData(String[][][] di)
    {
        unpackData(di);    
    }

    public void unpackData(String[][][] di)
    {
        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")]!=null && di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")].equals(""))                                //Durch die "und" verbindung wird die zweite abfrage nur überprüft wenn die erste abfrage nicht zutrifft (Sonst error wenn "equals" von null Objekt ausgeführt wird)
        {this.specification_ID = di[0][2][Arrays.asList(di[0][1]).indexOf("Specification ID")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")]!=null && !di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")].equals(""))
        {this.specification_version = di[0][2][Arrays.asList(di[0][1]).indexOf("Specification version")];}

        if (di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number")]!=null && di[0][2][Arrays.asList(di[0][1]).indexOf("Supplier number")].equals(""))
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
}
