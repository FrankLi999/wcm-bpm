/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bpwizard.wcm.repo.jcr;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * {@link FactoryBean} which uses ModeShape to provide {@link Repository} instances
 */
public class ModeShapeRepositoryFactory implements FactoryBean<Repository> {

    private static final Logger logger = LoggerFactory.getLogger(ModeShapeRepositoryFactory.class);
    private static final ModeShapeEngine ENGINE = new ModeShapeEngine();

    private Resource configuration;
    private Repository repository;

    @PostConstruct
    public void start() throws Exception {
        if (configuration == null) {
            throw new IllegalArgumentException("The repository configuration file is required");
        }
        ENGINE.start();
        RepositoryConfiguration repositoryConfiguration = RepositoryConfiguration.read(configuration.getURL());
        repository = ENGINE.deploy(repositoryConfiguration);
        ENGINE.startRepository(repositoryConfiguration.getName());
    }

    @PreDestroy
    public void stop() throws Exception {
//        try {
//            ENGINE.shutdown().get(10, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            LOG.error("Error while waiting for the ModeShape engine to shutdown", e);
//        }
//        
        Future<Boolean> future = ENGINE.shutdown();
        if ( future.get() ) {  // blocks until the engine is shutdown
        	logger.info("Shutdown successful");
        }
    }

    public void setConfiguration(Resource configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public Repository getObject() throws Exception {
        return repository;
    }

    @Override
    public Class<?> getObjectType() {
        return javax.jcr.Repository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}