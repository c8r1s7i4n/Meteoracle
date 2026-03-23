package de.omnp.meteoracle.application.domain.MoveCall;

public record NotarizationMetadata(
    String address,
    String rpcUrl,
    String packageId,
    String module,
    String createFunction,
    String updateFunction,
    String gasBudget
) {}