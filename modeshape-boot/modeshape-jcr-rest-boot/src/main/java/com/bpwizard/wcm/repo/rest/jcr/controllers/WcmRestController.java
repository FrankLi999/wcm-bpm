package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.ControlField;
import com.bpwizard.wcm.repo.rest.jcr.model.JsonForm;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNavigatorFilter;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(WcmRestController.BASE_URI)
@Validated
public class WcmRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmRestController.class);
	
	public static final String BASE_URI = "/wcm/api";
	
	@Autowired
	private WcmRequestHandler wcmRequestHandler;
	
	@GetMapping(path = "/jsonform/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, JsonForm[]> getAuthoringTemplateAsJsonForm(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, JsonForm[]> jsonForms = this.wcmRequestHandler.getSystemAuthoringTemplateAsJsonForm(repository, workspace, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return jsonForms;
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}

	@GetMapping(path = "/control/{repository}/{workspace}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ControlField[] getControlField(@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			ControlField[] ControlFileds = this.wcmRequestHandler.getControlField(repository, workspace, request);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return ControlFileds;
		} catch (RepositoryException re) {
			logger.error(re);
			throw new WcmRepositoryException(re, WcmError.createWcmError(re.getMessage(), WcmErrors.GET_CONTROL_ERROR, null));
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
		
	@PutMapping("/wcmItem/unlock/{repository}/{workspace}")
    public void unlock(
			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
			@RequestParam("path") String absPath) { 
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			this.wcmUtils.unlock(repository, workspace, absPath);
    	} catch (RepositoryException re) { 
    		logger.error(re);
			throw new WcmRepositoryException(re, WcmError.createWcmError(re.getMessage(), WcmErrors.UNLOCK_ITEM_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
	};

    @PutMapping("/wcmItem/restore/{repository}/{workspace}")
  	public void restore(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String absPath, 
  			@RequestParam("version") String version) {
    	if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
    	try {
	    	javax.jcr.Workspace ws = this.repositoryManager.getSession(repository, workspace).getWorkspace();
			javax.jcr.version.VersionManager vm = ws.getVersionManager();
			
		    vm.restore(absPath, version, true);
			javax.jcr.lock.LockManager lm = ws.getLockManager();
			if (lm.isLocked(absPath)) {
				lm.unlock(absPath);
			} 
    	} catch (RepositoryException re) { 
    		logger.error(re);
			throw new WcmRepositoryException(re, WcmError.createWcmError(re.getMessage(), WcmErrors.RESTORE_ITEM_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
    	if (logger.isDebugEnabled()) {
			logger.traceExit();
		}
  	}

//    @DeleteMapping("/wcmItem/purge/{repository}/{workspace}")
//  	public ResponseEntity<?> purgeWcmItem(
//  			@PathVariable("repository") String repository,
//		    @PathVariable("workspace") String workspace,
//  			@RequestParam("path") String wcmPath) { 
//  		if (logger.isDebugEnabled()) {
//			logger.traceEntry();
//		}
//  		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
//  		try {
//  			this.wcmRequestHandler.purgeWcmItem(repository, workspace, absPath);
//  	  		if (logger.isDebugEnabled()) {
//  				logger.traceExit();
//  			}
//  			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
//		} catch (WcmRepositoryException e ) {
//			logger.error(String.format("Failed to delete item %s from expired repository. Content item does not exist", absPath), e);
//			throw e;
//	    } catch (Throwable t) {
//	    	logger.error(t);
//			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
//		}
//
//  	};

  	@DeleteMapping("/wcmItem/delete/{repository}/{workspace}")
  	public void deleteWcmItem(
  			@PathVariable("repository") String repository,
		    @PathVariable("workspace") String workspace,
  			@RequestParam("path") String wcmPath) { 
  		String absPath = (wcmPath.startsWith(WcmConstants.NODE_ROOT_PATH)) ? wcmPath : String.format(WcmConstants.NODE_ROOT_PATH_PATTERN, wcmPath);
  		try {
  			Session session = this.repositoryManager.getSession(repository, WcmConstants.DEFAULT_WS);
  			Node node = session.getNode(absPath);
  			node.remove();
            session.save();
            if (this.authoringEnabled) {
	            session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
	  			node = session.getNode(absPath);
	  			node.remove();
	            session.save();
            }
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.PURGE_ITEM_ERROR, new String[] {absPath}));
	    } catch (Throwable t) {
	    	logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
  	};
  	
  	@PostMapping(path = "/wcmNodes/{repository}/{workspace}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public WcmNode[] getWcmNodes(
			@PathVariable("repository") final String repository, 
			@PathVariable("workspace") final String workspace,
			@RequestBody final WcmNavigatorFilter filter,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			String absPath = WcmUtils.nodePath(filter.getWcmPath());
			RestNode saNode = (RestNode) this.wcmItemHandler.item(baseUrl, repository, workspace, absPath, WcmConstants.READ_DEPTH_THREE_LEVEL);
			
			WcmNode[] wcmNodes = saNode.getChildren().stream()
			    .filter(node -> this.applyFilter(node, filter))
			    .map(node -> this.toWcmNode(node, repository, workspace, filter.getWcmPath()))
			    .toArray(WcmNode[]::new);

			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return wcmNodes; 
		} catch (WcmRepositoryException e ) {
			logger.error(e);
			throw e;
		} catch (RepositoryException re) { 
			logger.error(re);
			throw new WcmRepositoryException(re, new WcmError(re.getMessage(), WcmErrors.GET_NODE_ERROR, null));
	    } catch (Throwable t) {
			logger.error(t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}
	};
		
    private boolean applyFilter(final RestNode node, WcmNavigatorFilter filter) {
		return (filter == null || filter.getNodeTypes() == null) ?
				node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> nodeType.startsWith("bpw:"))
		    : node.getJcrProperties().stream()
				.filter(property -> "jcr:primaryType".equals(property.getName()))
				.map(property -> property.getValues().get(0))
				.anyMatch(nodeType -> Arrays.stream(
						filter.getNodeTypes()).anyMatch(nodeType::matches) &&
						this.propertyMatch(node, filter, nodeType));
	}
	
	private boolean propertyMatch(RestNode node, WcmNavigatorFilter filter, String nodeType) {
		if (filter.getConditions() == null || filter.getConditions().get(nodeType) == null) { 
			return true; 
		} 
		Map<String, String> nameValues = filter.getConditions().get(nodeType);
		Set<String> properties = new HashSet<>();
		properties.addAll(nameValues.keySet());
		for (RestProperty property: node.getJcrProperties()) {
			if (nameValues.get(property.getName()) != null && 
					property.getValues().get(0).equals(nameValues.get(property.getName()))) {
				properties.remove(property.getName());
				if (properties.size() == 0) {
					break;
				}
			} 
		}
		return properties.size() == 0;
	}
	
	private WcmNode toWcmNode(RestNode node, String repository, String workspace, String wcmPath) {
		WcmNode wcmNode = new WcmNode();
		wcmNode.setRepository(repository);
		wcmNode.setWorkspace(workspace);
		wcmNode.setName(node.getName());
		wcmNode.setId(node.getId());
		for (RestProperty property: node.getJcrProperties()) {
			if ("jcr:primaryType".equals(property.getName())) {
				wcmNode.setNodeType(property.getValues().get(0));
			} else if ("jcr:createdBy".equals(property.getName())) {
				wcmNode.setOwner(property.getValues().get(0));
			} 
		}
	
		for (RestNode childNode: node.getChildren()) {
			if (WcmUtils.checkNodeType(childNode, "bpw:workflowNode")) {
				for (RestProperty property: childNode.getJcrProperties()) {
					if ("workflowStage".equals(property.getName())) {
						wcmNode.setStatus(property.getValues().get(0));
						break;
					}
				}
			}
		}
		wcmNode.setWcmPath(String.format(wcmPath.startsWith("/")? WcmConstants.WCM_REL_PATH_PATTERN : 
			WcmConstants.WCM_PATH_PATTERN, wcmPath, node.getName()));
		return wcmNode;
	}
	
	protected void deleteDraftItem(String repository, String path) throws RepositoryException {
		Session draftSession = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
		Item item = draftSession.getItem(path);
        item.remove();
        draftSession.save();
	}
}
