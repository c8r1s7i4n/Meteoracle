package de.omnp.meteoracle.infrastructure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import de.omnp.meteoracle.domain.vda4994.JLocation;
import de.omnp.meteoracle.domain.vda4994.JsonData;
import de.omnp.meteoracle.domain.vda4994.Scan;
import de.omnp.meteoracle.infrastructure.api.dto.JLocationDTO;
import de.omnp.meteoracle.infrastructure.api.dto.JsonDataDTO;
import de.omnp.meteoracle.infrastructure.api.dto.ScanDTO;


@Mapper(componentModel = "spring")
public interface ScannerMapper {

    /**
     * Richtung: VOM DTO (Eingabe) ZUM Domain-Objekt (Ergebnis)
     * Wird im Controller aufgerufen, wenn ein Request reinkommt.
     */

    // Mapping ist ganz gut wenn das Domain Objekt sich verändert, aber die API gleich bleiben soll
    // @Mapping(source = "deviceId", target = "id") // 'source' ist im DTO, 'target' ist in Domain
    public Scan toDomain(ScanDTO dtoObj);
    public JsonData toDomain(JsonDataDTO jsonDataDTO);
    public JLocation toDomain(JLocationDTO jLocation);
    /**
     * Richtung: VOM Domain-Objekt (Eingabe) ZUM DTO (Ergebnis)
     * Wird genutzt, wenn du dem User Daten als Antwort zurückschickst.
     * OPTIONAL als Antwort an den Client
     */
    // @Mapping(source = "id", target = "deviceId") // Jetzt ist 'id' die Quelle (Domain)
    @Mapping(source = "packageId", target = "package_id")
    @Mapping(target = "onchainId", ignore = true)
    public ScanDTO toDto(Scan domainObj);
    public JsonDataDTO toDto(JsonData domainObj);
    public JLocationDTO toDoto(JLocation domainObj);


}
