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
