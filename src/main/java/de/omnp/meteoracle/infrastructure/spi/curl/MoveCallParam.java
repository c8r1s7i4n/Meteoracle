package de.omnp.meteoracle.infrastructure.spi.curl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveCallParam {

    @JsonProperty("tx_bytes")
    private String txBytes;

    @JsonProperty("signatures")
    private List<String> signatures;

    public MoveCallParam() {
    }

    public MoveCallParam(String txBytes, List<String> signatures) {
        this.txBytes = txBytes;
        this.signatures = signatures;
    }

    public String getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(String txBytes) {
        this.txBytes = txBytes;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

}
