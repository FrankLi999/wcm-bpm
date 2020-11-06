package com.bpwizard.wcm.repo.rest.jcr.controllers;

import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bpwizard.wcm.repo.rest.SyndicationUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseWcmRestController {
	@Value("${bpw.modeshape.authoring.enabled:true}")
	protected boolean authoringEnabled = true;
	
	@Value("${bpw.modeshape.syndication.enabled:true}")
	protected boolean syndicationEnabled = true;
	
	@Autowired
	protected RestItemHandler itemHandler;

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
	protected SyndicationUtils syndicationUtils;

	protected ObjectMapper objectMapper = new ObjectMapper();
}
