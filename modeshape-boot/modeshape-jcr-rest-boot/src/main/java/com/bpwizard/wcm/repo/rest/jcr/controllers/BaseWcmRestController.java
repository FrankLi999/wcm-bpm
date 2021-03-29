package com.bpwizard.wcm.repo.rest.jcr.controllers;

import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.bpwizard.wcm.repo.rest.handler.RestWcmItemHandler;
import com.bpwizard.wcm.repo.rest.service.WcmEventService;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseWcmRestController {
	@Value("${bpw.modeshape.authoring.enabled:false}")
	protected boolean authoringEnabled;
	
	@Value("${bpw.modeshape.syndication.enabled:false}")
	protected boolean syndicationEnabled;
	
	@Autowired
	protected RestWcmItemHandler wcmItemHandler;
	
	@Autowired
	protected RepositoryManager repositoryManager;

	@Autowired
	protected RestRepositoryHandler repositoryHandler;

	@Autowired
	protected RestNodeTypeHandler nodeTypeHandler;

	@Autowired
	protected RestServerHandler serverHandler;

	@Autowired
	protected WcmUtils wcmUtils;
	
	@Autowired	
	protected WcmEventService wcmEventService;

	protected ObjectMapper objectMapper = new ObjectMapper();
}
