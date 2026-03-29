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

package de.omnp.meteoracle.infrastructure.MoveCall;

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
