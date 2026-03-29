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

package de.omnp.meteoracle.application.port.in;

import java.util.List;

import de.omnp.meteoracle.application.port.out.ScanPak;
import de.omnp.meteoracle.domain.vda4994.Scan;


public interface ScanReceiver {
    public boolean checkIn(Scan object);

    public List<ScanPak> getScanAll();

    public ScanPak getScanById(String package_id);

    public List<ScanPak> getScanTraceById(String package_id);
}
