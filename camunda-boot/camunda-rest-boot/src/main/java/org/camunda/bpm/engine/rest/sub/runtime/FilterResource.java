package org.camunda.bpm.engine.rest.sub.runtime;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.runtime.FilterDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface FilterResource {
	FilterDto getFilter(String resourceId, Boolean itemCount);
    void deleteFilter(String resourceId);

	void updateFilter(String resourceId, @RequestBody FilterDto filterDto);
	
	Object executeSingleResult(String resourceId, HttpServletRequest request);
	
	Object querySingleResult(String resourceId, HttpServletRequest request, @RequestBody String extendingQuery);

	Object executeList(
			String resourceId, 
			HttpServletRequest request, 
			Integer firstResult, 
			Integer maxResults);

	Object queryList(
			String resourceId, 
			HttpServletRequest request, 
			Integer firstResult, 
			Integer maxResults,
			String extendingQuery);

	CountResultDto executeCount(String resourceId);

	CountResultDto queryCount(String resourceId, String extendingQuery);
	
	ResourceOptionsDto availableOperations(String resourceId, HttpServletRequest request);
}
