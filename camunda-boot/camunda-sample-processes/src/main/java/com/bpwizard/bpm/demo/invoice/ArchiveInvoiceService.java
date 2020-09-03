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
package com.bpwizard.bpm.demo.invoice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.FileValue;

/**
 * <p>
 * This is an empty service implementation illustrating how to use a plain Java
 * Class as a BPMN 2.0 Service Task delegate.
 * </p>
 */
public class ArchiveInvoiceService implements JavaDelegate {

	private static final Logger logger = LogManager.getLogger(ArchiveInvoiceService.class);

	public void execute(DelegateExecution execution) throws Exception {

		Boolean shouldFail = (Boolean) execution.getVariable("shouldFail");
		FileValue invoiceDocumentVar = execution.getVariableTyped("invoiceDocument");

		if (shouldFail != null && shouldFail) {
			throw new ProcessEngineException("Could not archive invoice...");
		} else {
			logger.info("\n\n  ... Now archiving invoice " + execution.getVariable("invoiceNumber") + ", filename: "
					+ invoiceDocumentVar.getFilename() + " \n\n");
			System.out.println("\n\n  ... Now archiving invoice " + execution.getVariable("invoiceNumber") + ", filename: "
					+ invoiceDocumentVar.getFilename() + " \n\n");
		}

	}

}
