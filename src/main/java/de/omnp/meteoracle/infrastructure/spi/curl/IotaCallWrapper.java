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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für den unsafe_moveCall der IOTA JSON-RPC API.
 * Dieses Objekt lebt im Adapter-Bereich.
 * <br>
 *
 * Der standardisierte JSON-RPC 2.0 Umschlag für alle IOTA-Anfragen.
 * T ist hier ein Platzhalter (Generic) für die spezifischen Parameter.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IotaCallWrapper<T> {

    @JsonProperty("jsonrpc")
    private final String jsonrpc = "2.0";

    @JsonProperty("id")
    private int id = 1;

    @JsonProperty("method")
    private String method;

    @JsonProperty("params")
    private T params;

    public IotaCallWrapper() {
    }

    public IotaCallWrapper(String method, T params) {
        this.method = method;
        this.params = params;
    }
}
