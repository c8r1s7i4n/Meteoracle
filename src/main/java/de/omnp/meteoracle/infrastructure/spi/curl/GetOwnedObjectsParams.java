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