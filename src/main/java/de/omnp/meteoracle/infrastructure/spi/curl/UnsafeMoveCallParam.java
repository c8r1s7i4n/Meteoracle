/*
    Meteoracle - Industrial Supply Chain Interoperability Layer
    Copyright (C) 2026 Christian Beissmann

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Original source: https://github.com/c8r1s7i4n/Meteoracle
    For contact and support, visit: https://omnipons.de
*/

package de.omnp.meteoracle.infrastructure.spi.curl;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Repräsentiert das "params"-Objekt für den unsafe_moveCall.
 * Diese Klasse ist ein reines DTO für die technische Kommunikation.
 */
public class UnsafeMoveCallParam {

    @JsonProperty("signer")
    private String signer;

    @JsonProperty("package_object_id")
    private String packageObjectId;

    @JsonProperty("module")
    private String module;

    @JsonProperty("function")
    private String function;

    @JsonProperty("type_arguments")
    private List<String> typeArguments = new ArrayList<>();

    @JsonProperty("arguments")
    private List<String> arguments = new ArrayList<>();

    @JsonProperty("gas_budget")
    private String gasBudget; // Muss als String gesendet werden (u64 Regel)

    // Standard-Konstruktor für Jackson
    public UnsafeMoveCallParam() {
    }

    // Komfort-Konstruktor
    public UnsafeMoveCallParam(String signer, String packageObjectId, String module, String function, String gasBudget,
            List<String> data) {
        this.signer = signer;
        this.packageObjectId = packageObjectId;
        this.module = module;
        this.function = function;
        this.gasBudget = gasBudget;
        this.arguments = data;
    }

    // Getter und Setter
    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getPackageObjectId() {
        return packageObjectId;
    }

    public void setPackageObjectId(String packageObjectId) {
        this.packageObjectId = packageObjectId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<String> getTypeArguments() {
        return typeArguments;
    }

    public void setTypeArguments(List<String> typeArguments) {
        this.typeArguments = typeArguments;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getGasBudget() {
        return gasBudget;
    }

    public void setGasBudget(String gasBudget) {
        this.gasBudget = gasBudget;
    }
}