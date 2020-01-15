package com.bpwizard.wcm.repo.content;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;

public class PublishContentItemDelegate implements JavaDelegate {
	private static final Logger logger = LogManager.getLogger(PublishContentItemDelegate.class);
	
	@Autowired
	protected RepositoryManager repositoryManager;

	@Autowired
	private WcmUtils wcmUtils;
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String repository = (String) delegate.getVariable("repository");
			String contentPath = (String) delegate.getVariable("contentPath");
			String baseUrl = (String) delegate.getVariable("baseUrl");
			
	        Session session = this.repositoryManager.getSession(repository, "draft"); 
	        Node contentNode = session.getNode(contentPath);
	        contentNode.setProperty("bpw:currentLifecycleState", "Published");
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.wcmUtils.getAuthoringTemplate(repository, "default", 
					atPath, baseUrl);
			ModeshapeUtils.grantPermissions(session, contentPath, at.getContentItemAcl().getOnPublishPermissions());
	        this.wcmUtils.unlock(repository, "draft", contentPath);
	        session.save();
	        session.getWorkspace().clone("default", contentPath, contentPath, true);
	        if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw new BpmnError("WCM_ERROR_PUBLISH");
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new BpmnError("WCM_ERROR_PUBLISH");
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new BpmnError("WCM_ERROR_PUBLISH");
		}			
	}
}
