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

package com.bpwizard.wcm.repo.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.modeshape.jcr.api.RepositoriesContainer;
import org.modeshape.jcr.api.txn.TransactionManagerLookup;
import org.modeshape.web.jcr.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.bpwizard.wcm.repo.context.SpringContext;
import com.bpwizard.wcm.repo.jcr.ModeshapeTransactionManagerLookup;

@Configuration
public class ModeshapeConfig {
	private static final Logger logger = LoggerFactory.getLogger(ModeshapeConfig.class);
	@Autowired 
	private TransactionManager transactionManager;
	
	@Autowired
	private Environment env;
	
	@Bean
	@DependsOn({"modeshapeDataSource"})
    public SpringContext sprinContext() {
    	SpringContext springContext = new SpringContext();
    	return springContext;
    }
	
	@Bean("webRepositoryManager")
	@DependsOn({"transactionManager", "transactionManagerLookup", "sprinContext"})
	public RepositoryManager repositoryManagerDev() throws IOException {
		logger.debug("Entering ...");
		String activeProfile = env.getActiveProfiles()[0];
		
		Map<String, Object> factoryParams = new HashMap<>();
		
		factoryParams.put(RepositoriesContainer.REPOSITORY_NAME, "bpwizard");
		factoryParams.put(RepositoriesContainer.URL, new ClassPathResource(String.format("modeshape/config/repository-config-%s.json", activeProfile)).getURL().toExternalForm());
		RepositoryManager repositoryManager = new RepositoryManager(factoryParams);
		logger.debug("Exiting ...");
		return repositoryManager;
	}
	
	@Bean
    public TransactionManagerLookup transactionManagerLookup(TransactionManager transactionManager) {
    	ModeshapeTransactionManagerLookup transactionManagerLookup = new ModeshapeTransactionManagerLookup();
    	transactionManagerLookup.setTransactionManager(this.transactionManager);
    	return transactionManagerLookup;
    }
}
