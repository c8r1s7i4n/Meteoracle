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
