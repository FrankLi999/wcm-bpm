package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.ModeshapeUtils;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.AuthoringTemplate;
import com.bpwizard.wcm.repo.rest.jcr.model.ContentItem;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping(ContentItemRestController.BASE_URI)
@Validated
public class ContentItemRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(ContentItemRestController.class);
	
	public static final String BASE_URI = "/wcm/api/contentItem";

	@PostMapping(path = "/save-drfat", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson());
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PostMapping(path = "/create-publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAndPublishContentItem(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath(), contentItem.getName());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), WcmConstants.DEFAULT_WS, absPath, contentItem.toJson());
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			if (authoringEnabled) {
				Session draftSession = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DRAFT_WS);
				draftSession.getWorkspace().clone(WcmConstants.DEFAULT_WS, absPath, absPath, true);
				// draftSession.save();
			}
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PostMapping(path = "/edit-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editDraft(
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), absPath, contentItem.toJson());

			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				String workspaceName = contentItem.getWorkspace();
				Session session = this.repositoryManager.getSession(repositoryName, workspaceName);
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PostMapping(path = "/cancel-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> canelDraft(
			@PathVariable("repository") String repository,
			@RequestParam("path") String contentItemPath,
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authoring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			Session session = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
			session.getWorkspace().clone(WcmConstants.DRAFT_WS, contentItemPath, contentItemPath, true);
			// session.save();
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/update-published", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateContentItem(@RequestBody ContentItem contentItem, HttpServletRequest request) { 
		try {
			contentItem.setCurrentLifecycleState("Published"); //TODO
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), contentItem.getWorkspace(), 
					contentItem.getAuthoringTemplate(), request);
			String baseUrl = RestHelper.repositoryUrl(request);
			if (at.getContentItemAcl() != null) {
			    contentItem.setAcl(at.getContentItemAcl().getOnPublishPermissions());
			}
			JsonNode contentItemJson = contentItem.toJson();
			this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), contentItem.getWorkspace(), absPath, contentItemJson);
			Session session = this.repositoryManager.getSession(contentItem.getRepository(), WcmConstants.DEFAULT_WS);
			if (contentItem.getAcl() != null) {
				ModeshapeUtils.grantPermissions(session, absPath, contentItem.getAcl());
			}
			this.wcmUtils.unlock(contentItem.getRepository(), contentItem.getWorkspace(), absPath);
			if (this.authoringEnabled) {
				this.itemHandler.updateItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItemJson);
//				Session draftSession = this.repositoryManager.getSession(contentItem.getRepository(), );
//				draftSession.getWorkspace().clone(DEFAULT_WS, contentItem.getNodePath(), contentItem.getNodePath(), true);
				// draftSession.save();
			}
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@GetMapping(path = "/get/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem getContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String wcmPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = WcmUtils.nodePath(wcmPath);
			RestNode contentItemNode = (RestNode) this.itemHandler.item(baseUrl, repository, workspace,
					absPath, 4);
			
			ContentItem contentItem = new ContentItem(); 
			contentItem.setRepository(repository);
			contentItem.setWorkspace(workspace);
			contentItem.setWcmPath(wcmPath.startsWith("/") ? wcmPath : "/" + wcmPath);
			Map<String, String> elements = new HashMap<>();
			Map<String, String> properties = new HashMap<>();
			contentItem.setElements(elements);
			properties.put("name", contentItemNode.getName());
			contentItem.setProperties(properties);
	
			this.resolveWorkflowNode(contentItem, contentItemNode, properties);
			for (RestProperty property: contentItemNode.getJcrProperties()) {
				if ("bpw:authoringTemplate".equals(property.getName())) {
					contentItem.setAuthoringTemplate(property.getValues().get(0));
				} else if ("bpw:categories".equals(property.getName())) {
					contentItem.setCategories(property.getValues().toArray(new String[property.getValues().size()]));
					properties.put("categories", String.join(",", property.getValues()));
				} 
			}
			for (RestNode node: contentItemNode.getChildren()) {
				if (this.wcmUtils.checkNodeType(node, "bpw:contentElementFolder")) {
					for (RestNode enode: node.getChildren()) {
						if (this.wcmUtils.checkNodeType(enode, "bpw:contentElement")) {
							for (RestProperty property: enode.getJcrProperties()) {
								if ("bpw:value".equals(property.getName())) {
									elements.put(enode.getName(), property.getValues().get(0));
									break;
								} 
							}
						}
					}
				} else if (this.wcmUtils.checkNodeType(node, "bpw:propertyElementFolder")) {
					for (RestNode pnode: node.getChildren()) {
						if (this.wcmUtils.checkNodeType(pnode, "bpw:contentElement")) {
							for (RestProperty property: pnode.getJcrProperties()) {
								if ("bpw:value".equals(property.getName())) {
									properties.put(pnode.getName(), property.getValues().get(0));
									break;
								} 
							}
						}
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentItem;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PostMapping(path = "/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> creatContentItemAsDraft(			
			@RequestBody ContentItem contentItem, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			contentItem.setCurrentLifecycleState(WcmConstants.DRAFT_WS); //TODO
			String absPath = WcmUtils.nodePath(contentItem.getWcmPath());
			AuthoringTemplate at = this.doGetAuthoringTemplate(contentItem.getRepository(), WcmConstants.DRAFT_WS, 
					contentItem.getAuthoringTemplate(), request);
			contentItem.setAcl(at.getContentItemAcl().getOnSaveDraftPermissions());
			
			// this.itemHandler.addItem(request, contentItem.getRepository(), contentItem.getWorkspace(), path, contentItem.toJson());
			String baseUrl = RestHelper.repositoryUrl(request);
			this.itemHandler.addItem(baseUrl, contentItem.getRepository(), WcmConstants.DRAFT_WS, absPath, contentItem.toJson());
			if (contentItem.getAcl() != null) {
				String repositoryName = contentItem.getRepository();
				Session session = this.repositoryManager.getSession(repositoryName, WcmConstants.DRAFT_WS);
				ModeshapeUtils.grantPermissions(session, absPath, at.getContentItemAcl().getOnSaveDraftPermissions());
			}
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/reject-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rejectContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
			
			UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = principal.getUsername();
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-reject-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/approve-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> approveContentItemDraft(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {			
			UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = principal.getUsername();
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        Node commentsNode = contentNode.getNode("comments");
	        Node commentNode = commentsNode.addNode("comment-"+ username + "-approval-" + System.currentTimeMillis(), "bpw:comment");
	        commentNode.setProperty("bpw:comment", comment);
	        commentNode.setProperty("bpw:reviewer", username);
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnReviewedDraftPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        //TODO: notify editor
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> publishContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String contentItemPath,
			@RequestBody String comment, 
			HttpServletRequest request)
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		if (!this.authoringEnabled) {
			if (logger.isDebugEnabled()) {
				logger.traceExit("Authring is not enabled");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		try {
	        Session session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS); 
	        Node contentNode = session.getNode(contentItemPath);
	        contentNode.setProperty("bpw:currentLifecycleState", "Published");
	        String atPath = contentNode.getProperty("bpw:authoringTemplate").getString();
			AuthoringTemplate at = this.doGetAuthoringTemplate(repository, workspace, 
					atPath, request);
			ModeshapeUtils.grantPermissions(session, contentItemPath, at.getContentItemAcl().getOnPublishPermissions());
			this.wcmUtils.unlock(repository, workspace, contentItemPath);
	        session.save();
	        Session defaultSession = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
	        defaultSession.getWorkspace().clone(WcmConstants.DRAFT_WS, contentItemPath, contentItemPath, true);
	        // defaultSession.save();
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}		
	}
	
	@PutMapping(path = "/lock/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ContentItem lockContentItem(
		    @PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("path") String absPath,
		    HttpServletRequest request) 
		    throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.doLock(repository, workspace, absPath);
			ContentItem contentItem = this.getContentItem(repository, workspace, absPath, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return contentItem;
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
	}
	
	@PutMapping("/expire/{repository}/{workspace}")
  	public void expireContentItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath) { 
    	if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
  		try {
  			Session session = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
  			Node node = session.getNode(absPath);
  			String workflowState = node.getProperty("bpw:currentLifecycleState").getValue().getString();
            if ("Published".equals(workflowState)) {
            	node.remove();
            	session.save();
            	if (this.authoringEnabled) {
	        		session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
	      			node = session.getNode(absPath);
	            	node.setProperty("bpw:currentLifecycleState", WcmConstants.EXPIRED_WS);
	            	session.save();
            	}
            }
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
  	};
  	
  	@PutMapping("/workflowState/{repository}/{workspace}")
  	public void updateWcmItemWorkflowStage(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
		    @RequestParam("state") String state,
  			@RequestParam("path") String absPath) { 
  		try {
  			Session session = this.repositoryManager.getSession(repository, workspace);
  			Node node = session.getNode(absPath);
            node.setProperty("bpw:currentLifecycleState", state);
  			session.save();
		} catch (WcmRepositoryException e ) {
			throw e;
		} catch (RepositoryException re) { 
			throw new WcmRepositoryException(re);
	    } catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}
  	};
}
