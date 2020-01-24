/*
 * Copyright 2014 JBoss Inc
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
package io.apiman.gateway.platforms.war;

import io.apiman.gateway.api.rest.impl.IEngineAccessor;
import io.apiman.gateway.engine.IEngine;
import io.apiman.gateway.platforms.war.WarGateway;

/**
 * Accessor for the engine - allows the REST implementation to get
 * access to the engine created by the web app bootstrapper.
 *
 * @author eric.wittmann@redhat.com
 */
public class WarEngineAccessor implements IEngineAccessor {
    
    /**
     * Constructor.
     */
    public WarEngineAccessor() {
    }
    
    /**
     * @see io.apiman.gateway.api.rest.impl.IEngineAccessor#getEngine()
     */
    @Override
    public IEngine getEngine() {
        return WarGateway.engine;
    }

}
