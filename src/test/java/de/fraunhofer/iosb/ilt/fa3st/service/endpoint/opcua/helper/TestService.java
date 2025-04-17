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
package de.fraunhofer.iosb.ilt.fa3st.service.endpoint.opcua.helper;

import de.fraunhofer.iosb.ilt.fa3st.service.endpoint.opcua.OpcUaEndpointConfig;
import de.fraunhofer.iosb.ilt.fa3st.service.endpoint.opcua.helper.assetconnection.TestAssetConnectionConfig;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.digitaltwin.fa3st.common.exception.AssetConnectionException;
import org.eclipse.digitaltwin.fa3st.common.exception.ConfigurationException;
import org.eclipse.digitaltwin.fa3st.common.model.AASFull;
import org.eclipse.digitaltwin.fa3st.common.model.AASSimple;
import org.eclipse.digitaltwin.fa3st.service.Service;
import org.eclipse.digitaltwin.fa3st.service.config.CoreConfig;
import org.eclipse.digitaltwin.fa3st.service.config.ServiceConfig;
import org.eclipse.digitaltwin.fa3st.service.filestorage.memory.FileStorageInMemoryConfig;
import org.eclipse.digitaltwin.fa3st.service.messagebus.internal.MessageBusInternalConfig;
import org.eclipse.digitaltwin.fa3st.service.persistence.memory.PersistenceInMemoryConfig;


/**
 * An AAS Test service.
 */
public class TestService extends Service {

    public TestService(OpcUaEndpointConfig config, TestAssetConnectionConfig assetConnectionConfig, boolean full) throws ConfigurationException, AssetConnectionException {
        super(ServiceConfig.builder()
                .core(CoreConfig.builder()
                        .requestHandlerThreadPoolSize(2)
                        .build())
                .persistence(PersistenceInMemoryConfig.builder()
                        .initialModel(full ? AASFull.createEnvironment() : AASSimple.createEnvironment())
                        .build())
                .endpoint(config)
                .messageBus(MessageBusInternalConfig.builder()
                        .build())
                .assetConnections(assetConnectionConfig != null ? List.of(assetConnectionConfig) : new ArrayList<>())
                .fileStorage(new FileStorageInMemoryConfig())
                .build());
    }
}
