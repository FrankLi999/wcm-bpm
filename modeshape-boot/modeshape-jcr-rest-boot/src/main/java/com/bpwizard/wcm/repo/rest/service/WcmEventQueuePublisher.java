package com.bpwizard.wcm.repo.rest.service;

import java.io.IOException;

import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;

public interface WcmEventQueuePublisher {
	public void syndicateCndTypes(WcmEvent wcmEvent) throws IOException;
	public void syndicate(WcmEvent wcmEvent) throws IOException;
}
