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
import java.util.Map;

/**
 * DTO für die Parameter von iotax_getOwnedObjects
 */
public class GetOwnedObjectsParams {

    private String owner;
    private Filter filter;
    private Options options;

    public GetOwnedObjectsParams(String owner, String structType) {
        this.owner = owner;
        this.filter = new Filter(structType, owner);
        this.options = new Options();
    }

    // Diese Methode wird für die "params"-Liste in IotaCallWrapper genutzt
    public List<Object> toParamsList() {
        return List.of(owner, Map.of("filter", filter, "options", options));
    }

    // Innere Klassen für die Struktur
    public static class Filter {
        @JsonProperty("MatchAll")
        public List<Object> matchAll;

        public Filter(String structType, String addressOwner) {
            this.matchAll = List.of(
                    Map.of("StructType", structType),
                    Map.of("AddressOwner", addressOwner));
        }
    }

    public static class Options {
        public boolean showType = true;
        public boolean showOwner = true;
        public boolean showPreviousTransaction = false;
        public boolean showDisplay = false;
        public boolean showContent = true;
        public boolean showBcs = false;
        public boolean showStorageRebate = false;
    }
}