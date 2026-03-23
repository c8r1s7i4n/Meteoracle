package de.omnp.meteoracle.application.domain.MoveCall;

public class MoveCallCommand {
    private final String signer;
    private final String packageId;
    private final String module;
    private final String function;
    private final String gasBudget;

    public MoveCallCommand(String signer, String packageId, String module, String function, String gasBudget) {
        this.signer = signer;
        this.packageId = packageId;
        this.module = module;
        this.function = function;
        this.gasBudget = gasBudget;
    }

    public String getSigner() {
        return signer;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getModule() {
        return module;
    }

    public String getFunction() {
        return function;
    }

    public String getGasBudget() {
        return gasBudget;
    }
    
}
