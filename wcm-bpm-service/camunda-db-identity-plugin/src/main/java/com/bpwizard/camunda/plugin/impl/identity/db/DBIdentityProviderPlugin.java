/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bpwizard.camunda.plugin.impl.identity.db;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.camunda.bpm.engine.impl.persistence.GenericManagerFactory;

import com.bpwizard.camunda.identity.impl.db.DbIdentityServiceProvider;

/**
 * <p>
 * {@link ProcessEnginePlugin} providing Ldap Identity Provider support
 * </p>
 *
 * <p>
 * This class extends {@link DatabaseConfiguration} such that the configuration
 * properties can be set directly on this class vie the
 * <code>&lt;properties .../&gt;</code> element in bpm-platform.xml /
 * processes.xml
 * </p>
 *
 * @author Daniel Meyer
 *
 */
public class DBIdentityProviderPlugin implements ProcessEnginePlugin {

	public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
		System.out.println(">>>>>>>>>>>>> init db DBIdentityProviderPlugin:" + processEngineConfiguration);
		SessionFactory identityProviderSessionFactory = new GenericManagerFactory(DbIdentityServiceProvider.class);
		System.out.println(">>>>>>>>>>>>> init db DBIdentityProviderPlugin 1:" + identityProviderSessionFactory);
		processEngineConfiguration.setIdentityProviderSessionFactory(identityProviderSessionFactory);
		System.out.println(">>>>>>>>>>>>> init db DBIdentityProviderPlugin 2:");
	}

	@Override
	public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
		// Do nothing
	}

	@Override
	public void postProcessEngineBuild(ProcessEngine processEngine) {
		// Do nothing
	}
}
