/**
 * Copyright (c) 2021 Fraunhofer IOSB, eine rechtlich nicht selbstaendige
 * Einrichtung der Fraunhofer-Gesellschaft zur Foerderung der angewandten
 * Forschung e.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fraunhofer.iosb.ilt.fa3st.service.endpoint.opcua.helper.assetconnection;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
import org.eclipse.digitaltwin.fa3st.service.assetconnection.AbstractAssetOperationProviderConfig;


public class TestOperationProviderConfig extends AbstractAssetOperationProviderConfig {

    private final List<OperationVariable> outputArgs;

    public TestOperationProviderConfig(List<OperationVariable> outputArgs) {
        if (outputArgs == null) {
            this.outputArgs = new ArrayList<>();
        }
        else {
            this.outputArgs = outputArgs;
        }
    }


    public List<OperationVariable> getOutputArgs() {
        return outputArgs;
    }
}
