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
package org.camunda.bpm.engine.rest.sub.repository.controller;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.rest.IncidentRestService;
import org.camunda.bpm.engine.rest.controller.AbstractRestProcessEngineAware;
import org.camunda.bpm.engine.rest.dto.runtime.IncidentDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.repository.IncidentResource;
import org.camunda.bpm.engine.runtime.Incident;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IncidentRestService.PATH + "/{incidentId}")
public class IncidentResourceRestController extends AbstractRestProcessEngineAware implements IncidentResource {
	
	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public IncidentDto getIncident(@PathVariable("incidentId") String incidentId) {
		Incident incident = this.processEngine.getRuntimeService().createIncidentQuery().incidentId(incidentId).singleResult();
		if (incident == null) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, "No matching incident with id " + incidentId);
		}
		return IncidentDto.fromIncident(incident);
	}

	@DeleteMapping(path="/")
	public void resolveIncident(@PathVariable("incidentId") String incidentId) {
		try {
			this.processEngine.getRuntimeService().resolveIncident(incidentId);
		} catch (NotFoundException e) {
			throw new InvalidRequestException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (BadUserRequestException e) {
			throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
