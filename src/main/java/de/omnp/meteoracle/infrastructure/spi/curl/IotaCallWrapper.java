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
