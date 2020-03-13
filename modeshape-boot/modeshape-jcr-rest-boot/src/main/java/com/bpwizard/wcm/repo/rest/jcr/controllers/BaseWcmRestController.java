package com.bpwizard.wcm.repo.rest.jcr.controllers;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.modeshape.web.jcr.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.handler.RestItemHandler;
import com.bpwizard.wcm.repo.rest.handler.RestNodeTypeHandler;
import com.bpwizard.wcm.repo.rest.handler.RestRepositoryHandler;
import com.bpwizard.wcm.repo.rest.handler.RestServerHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Library;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseWcmRestController {
	
	protected static final String WCM_ROOT_PATH = "/bpwizard/library";
	protected static final String WCM_ROOT_PATH_PATTERN = "/bpwizard/library/%s";
	protected static final String WCM_AT_PATH_PATTERN = "/bpwizard/library/%s/authoringTemplate/%s";
	protected static final String WCM_RT_PATH_PATTERN = "/bpwizard/library/%s/renderTemplate/%s";
	protected static final String WCM_CONTENT_LAYOUT_PATH_PATTERN = "/bpwizard/library/%s/contentAreaLayout/%s";
	protected static final String WCM_SITECONFIG_PATH_PATTERN = "/bpwizard/library/%s/siteConfig/%s";
	protected static final String WCM_CATEGORY_PATH_PATTERN = "/bpwizard/library/%s/category%s";
	protected static final String WCM_VALIDATTOR_PATH_PATTERN = "/bpwizard/library/%s/validationRule/%s";
	protected static final String WCM_WORKFLOW_PATH_PATTERN = "/bpwizard/library/%s/workflow/%s";
	protected static final String WCM_QUERY_PATH_PATTERN = "/bpwizard/library/%s/query/%s";
	protected static final String DEFAULT_WS = "default";
	protected static final String DRAFT_WS = "draft";
	protected static final String EXPIRED_WS = "expired";
	
	@Value("${bpw.modeshape.authoring.enabled:true}")
	protected boolean authoringEnabled = true;
	
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
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	protected boolean isControlField(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlField");
	}

	protected boolean isSiteArea(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:siteArea");
	}
	
	protected boolean isControlFieldMetaData(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:controlFieldMetaData");
	}

	protected boolean isLibrary(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:library");
	}

	protected boolean notSystemLibrary(RestNode node) {
		return !"system".equalsIgnoreCase(node.getName());
	}
	
	protected boolean isTheme(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:themeType");
	}

	protected boolean isRenderTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:renderTemplate");
	}

	protected boolean isContentAreaLayout(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:contentAreaLayout");
	}

	protected boolean isAuthortingTemplate(RestNode node) {
		return this.wcmUtils.checkNodeType(node, "bpw:authoringTemplate");
	}
	
  	public void doPurgeWcmItem(
  			String repository,
		    String workspace,
  			String absPath) { 
  		
  		absPath = (absPath.startsWith("/")) ? absPath : String.format("/%s", absPath);
  		try {
  			if (this.authoringEnabled) {
  				try {
		  			Session draftSession = this.repositoryManager.getSession(repository, "draft");
		  			Node draftNode = draftSession.getNode(absPath);
		  			// String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
		            // if ("Expired".equals(workflowState)) {
		  			    draftNode.remove();
		            	draftSession.save();
		            // }
  				} catch (Exception e) {
  					//TODO: 
  					e.printStackTrace();
  				}
  				try {
	            	Session expiredSession = this.repositoryManager.getSession(repository, "expired");
		  			Node expiredNode = expiredSession.getNode(absPath);
		  			// String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
		            // if ("Expired".equals(workflowState)) {
		  			expiredNode.remove();
		  			expiredSession.save();
		            // }
  				} catch (Exception e) {
  					//TODO: 
  					e.printStackTrace();
  				}
  			}
  			
  			try {
  				Session session = this.repositoryManager.getSession(repository, "default");
	  			Node node = session.getNode(absPath);
	            node.remove();
	            session.save();
  			} catch (PathNotFoundException ex) {
  				//logger.warn(String.format("Content item %s does not exist", absPath));
  			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  	};
  	
	protected boolean filterLibrary(Library library, String filter) {
		return StringUtils.hasText(filter) ? library.getName().startsWith(filter) : true;
	}
}
