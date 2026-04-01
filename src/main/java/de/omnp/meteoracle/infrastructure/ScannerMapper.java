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

    // Mapping ist ganz gut wenn das Domain Objekt sich verändert, aber die API
    // gleich bleiben soll
    // @Mapping(source = "deviceId", target = "id") // 'source' ist im DTO, 'target'
    // ist in Domain
    public Scan toDomain(ScanDTO dtoObj);

    public JsonData toDomain(JsonDataDTO jsonDataDTO);

    public JLocation toDomain(JLocationDTO jLocation);

    /**
     * Richtung: VOM Domain-Objekt (Eingabe) ZUM DTO (Ergebnis)
     * Wird genutzt, wenn du dem User Daten als Antwort zurückschickst.
     * OPTIONAL als Antwort an den Client
     */
    // @Mapping(source = "id", target = "deviceId") // Jetzt ist 'id' die Quelle
    // (Domain)
    @Mapping(source = "packageId", target = "package_id")
    @Mapping(target = "onchainId", ignore = true)
    @Mapping(target = "stateVersion", ignore = true)
    public ScanDTO toDto(Scan domainObj);

    public JsonDataDTO toDto(JsonData domainObj);

    public JLocationDTO toDoto(JLocation domainObj);
}
