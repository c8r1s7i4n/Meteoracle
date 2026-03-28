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
