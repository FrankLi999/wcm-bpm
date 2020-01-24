/*
 * Copyright 2017 Pete Cornish
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.gateway.engine.hazelcast;

import io.apiman.gateway.engine.IRequiresInitialization;
import io.apiman.gateway.engine.hazelcast.common.HazelcastBackingStoreProvider;
import io.apiman.gateway.engine.storage.component.AbstractSharedStateComponent;

import java.util.Map;

/**
 * Shared state component backed by a Hazelcast Map. This allows the shared state
 * to be easily clusterable.
 *
 * @author Pete Cornish
 */
public class HazelcastSharedStateComponent extends AbstractSharedStateComponent implements IRequiresInitialization {
    private final Map<String, String> componentConfig;

    /**
     * Constructor.
     */
    public HazelcastSharedStateComponent(Map<String, String> componentConfig) {
        super(new HazelcastBackingStoreProvider());
        this.componentConfig = componentConfig;
    }

    @Override
    public void initialize() {
        if (Boolean.valueOf(componentConfig.get(HazelcastBackingStoreProvider.CONFIG_EAGER_INIT))) {
            getStore();
        }
    }
}
