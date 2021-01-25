package com.bpwizard.wcm.repo.rest.handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.jcr.model.SyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.Syndicator;
import com.bpwizard.wcm.repo.rest.jcr.model.UpdateSyndicationRequest;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmEvent;
import com.bpwizard.wcm.repo.rest.service.JsonNodeEventQueuePublisher;
import com.bpwizard.wcm.repo.rest.service.JsonNodeEventRepository;
import com.bpwizard.wcm.repo.rest.service.JsonNodeEventRestPublisher;
import com.bpwizard.wcm.repo.rest.service.SyndicatorRepository;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class JsonNodeSyndicationHandler extends AbstractHandler {
	private static final Logger logger = LoggerFactory.getLogger(JsonNodeSyndicationHandler.class);
	
	@Autowired
	private JsonNodeEventRepository wcmEventService;
	
	@Autowired
	private SyndicatorRepository syndicatorService;
	
	@Autowired
	private JsonNodeEventQueuePublisher wcmEventQueuePublisher;
	
	@Autowired
	private JsonNodeEventRestPublisher wcmEventRestPublisher;
	
	@Value("${syndication.transport.strategy}")
	private String syndicationStrategy; //rest or Kafka
	
	@Transactional
	public void syndicate(SyndicationRequest syndicationRequest, String token) throws RepositoryException, IOException {
		
		
		Syndicator syndicator = syndicatorService.getSyndicator(syndicationRequest.getSyndicationId());
		long currentTimeMillis = System.currentTimeMillis();
		Timestamp lastSyndication = (syndicationRequest.getEndTime().getTime() < currentTimeMillis) ? 
				syndicationRequest.getEndTime(): new Timestamp(currentTimeMillis);
		List<WcmEvent> cndEvents = wcmEventService.getCndBefore(syndicator, syndicationRequest.getEndTime());
		if (cndEvents != null) {
			for (WcmEvent cndEvent: cndEvents) {
				if ("kafka".equals(syndicationStrategy)) {
					wcmEventQueuePublisher.syndicateCndTypes(cndEvent, syndicator, token);
				} else {
					wcmEventRestPublisher.syndicateCndTypes(cndEvent, syndicator, token);
				}
			}
		}
		int pageSize = 20;
		int pageIndex = 0;
		int actualBatchSize = 0;
		List<WcmEvent> wcmEvents = null;
		int elements = 0;
		int items = 0;
		do {
			
			wcmEvents = wcmEventService.getWcmEventBefore(syndicator, syndicationRequest.getEndTime(), pageIndex, pageSize);
			actualBatchSize = wcmEvents.size();
			System.out.println(">>>>>>>>>>>>>>>>>>> size of batch:" + actualBatchSize);
			items += actualBatchSize;
			pageIndex += actualBatchSize;
			if (actualBatchSize > 0) {
				for (WcmEvent wcmEvent: wcmEvents) {
					ObjectNode contentNode = (ObjectNode) JsonUtils.bytesToJsonNode(wcmEvent.getContent());
					ArrayNode descendants = (ArrayNode) contentNode.get("descendants");
					System.out.println(">>>>>>>>>>>>>>>>>>> nodePath: " + wcmEvent.getLibrary() + "/" + wcmEvent.getNodePath());
					if (descendants != null) {
						elements += descendants.size();
						System.out.println(">>>>>>>>>>>>>>>>>>> number of elements:" + descendants.size());
					}
					if ("kafka".equals(syndicationStrategy)) {
						wcmEventQueuePublisher.syndicate(wcmEvent, syndicator, token);
					} else {
						wcmEventRestPublisher.syndicate(wcmEvent, syndicator, token);
					}					
				}
			}
		} while (actualBatchSize >= pageSize);
		logger.debug(">>>>>>>>>>>>>>>>>>> number of all items:" + items);
		System.out.println(">>>>>>>>>>>>>>>>>>> number of all elements:" + elements);
		UpdateSyndicationRequest updateSyndicationRequest = new UpdateSyndicationRequest();
		updateSyndicationRequest.setSyndicationId(syndicator.getId());
		updateSyndicationRequest.setLastSyndication(lastSyndication);
		syndicatorService.updateSyndicator(updateSyndicationRequest);
	}
}
