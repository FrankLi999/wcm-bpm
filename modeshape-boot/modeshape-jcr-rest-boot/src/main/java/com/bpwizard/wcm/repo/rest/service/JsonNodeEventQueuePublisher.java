package com.bpwizard.wcm.repo.rest.service;

import java.io.IOException;

import javax.jcr.RepositoryException;

import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

public interface JsonNodeEventQueuePublisher {
	public void syndicateCndTypes(WcmEvent wcmEvent, Syndicator syndicator, String token) throws IOException;
	public void syndicate(WcmEvent wcmEvent, Syndicator syndicator, String token) throws RepositoryException, IOException;
}
