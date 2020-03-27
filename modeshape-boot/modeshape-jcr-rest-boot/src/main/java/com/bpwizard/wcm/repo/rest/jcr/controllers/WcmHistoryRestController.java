package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmHistory;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmVersion;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;

@RestController
@RequestMapping(WcmHistoryRestController.BASE_URI)
@Validated
public class WcmHistoryRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(WcmHistoryRestController.class);
	
	public static final String BASE_URI = "/wcm/api/history";
	
	@GetMapping(path = "/library/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, WcmHistory>> getLibraryHistory(		
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam(name="path") String wcmPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Map<String, WcmHistory> libraryHistories = new HashMap<>();
			Session session = this.repositoryManager.getSession(repository, workspace);
			libraryHistories.put("library", this.doGetHistory(session, wcmPath));
			libraryHistories.put("renderTemplate", this.doGetHistory(session, String.format("%s/renderTemplate", wcmPath)));
			libraryHistories.put("contentItem", this.doGetHistory(session, String.format("%s/rootSiteArea", wcmPath)));
			libraryHistories.put("authoringTemplate", this.doGetHistory(session, String.format("%s/authoringTemplate", wcmPath)));
			libraryHistories.put("contentAreaLayout", this.doGetHistory(session, String.format("%s/contentAreaLayout", wcmPath)));
			libraryHistories.put("category", this.doGetHistory(session, String.format("%s/category", wcmPath)));
			libraryHistories.put("workflow", this.doGetHistory(session, String.format("%s/workflow", wcmPath)));
			return ResponseEntity.status(HttpStatus.OK).body(libraryHistories);
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}

	@GetMapping(path = "/item/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WcmHistory> getVersionHistory(		
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam(name="path") String wcmPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			Session session = this.repositoryManager.getSession(repository, workspace);
			WcmHistory history = this.doGetHistory(session, wcmPath);
			return ResponseEntity.status(HttpStatus.OK).body(history);
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	@PutMapping(path = "/item/{repository}/{workspace}")
	public ResponseEntity<?> restore(		
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam(name="path") String wcmPath,
			@RequestParam(name="version") String versionName,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		
		try {
			Session session = this.repositoryManager.getSession(repository, workspace);
			this.doRestore(session, wcmPath, versionName);
			if (this.authoringEnabled) {
				session = this.repositoryManager.getSession(repository, WcmConstants.DRAFT_WS);
				this.doRestore(session, wcmPath, versionName);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (Throwable t) {
			throw new WcmRepositoryException(t);
		}	
	}
	
	private WcmHistory doGetHistory(Session session, String wcmPath) throws RepositoryException {
		VersionManager vm = session.getWorkspace().getVersionManager();
		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
		// .getVersionLabels(version)
		VersionHistory vh = vm.getVersionHistory(absPath);
		VersionIterator versionIterator = vh.getAllVersions();
		List<WcmVersion> wcmVersions = new ArrayList<>();
		while(versionIterator.hasNext()) {
		    Version version = versionIterator.nextVersion();
		    WcmVersion wcmVersion = new WcmVersion();
		    wcmVersion.setCreated(version.getCreated().toString());
		    wcmVersion.setIdentifier(version.getIdentifier());
		    wcmVersion.setLabels(vh.getVersionLabels(version));
		    wcmVersion.setName(version.getName());
		}
		WcmHistory wcmHistory = new WcmHistory();
		wcmHistory.setWcmPath(wcmPath);
		wcmHistory.setVersions(wcmVersions.stream().toArray(WcmVersion[]::new));
		return wcmHistory;
	}
	
	private void doRestore(Session session, String wcmPath, String versionName) throws RepositoryException {
		VersionManager vm = session.getWorkspace().getVersionManager();
		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
		vm.restore(absPath, versionName, true);
	}
}
