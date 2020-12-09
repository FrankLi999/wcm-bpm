package com.bpwizard.wcm.repo.rest.jcr.controllers;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.service.domain.Role;
import com.bpwizard.spring.boot.commons.service.domain.RoleService;
import com.bpwizard.spring.boot.commons.service.domain.User;
import com.bpwizard.spring.boot.commons.service.domain.UserService;
import com.bpwizard.spring.boot.commons.service.domain.UnpagedSort;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.jcr.model.Grant;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmGroup;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmPermission;
import com.bpwizard.wcm.repo.rest.jcr.model.WcmPrincipal;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;
import com.bpwizard.wcm.repo.validation.ValidateString;

@SuppressWarnings("removal")
@RestController
@RequestMapping(WcmAclRestController.BASE_URI)
@Validated
public class WcmAclRestController extends BaseWcmRestController {
	public static final String BASE_URI = "/wcm/api/acl";
	private static final Logger logger = LoggerFactory.getLogger(WcmAclRestController.class);

	private static String PRINCIPAL_TYPE_GROUP = "group";
	private static String PRINCIPAL_TYPE_USER = "user";
	
	private static String ROLE_ADMINISTRATOR = "administrator";
	private static String ROLE_EDITOR = "editor";
	private static String ROLE_VIEWER = "viewer";
	
	private static String[] readPrivileges = new String[] {
	  Privilege.JCR_READ
	};
	
	private static String[] editorPrivileges = new String[] {
	  Privilege.JCR_READ,
	  Privilege.JCR_WRITE,
	  Privilege.JCR_REMOVE_NODE,
	  Privilege.JCR_ADD_CHILD_NODES,
	  Privilege.JCR_REMOVE_CHILD_NODES,
	  Privilege.JCR_REMOVE_CHILD_NODES
	};
	
	private static String[] adminPrivileges = new String[] {
		Privilege.JCR_ALL 
	};
			
	private static final String JCR_ALL = "jcr:all";
	private static final String JCR_WRITE = "jcr:write";
	private static final String JCR_READ = "jcr:read";
	
	@Autowired
	private RoleService<Role, Long> roleService;
	
	@Autowired
	private  UserService<User<Long>, Long> userService;
	
	@PutMapping(path = "/grant/{repository}/{workspace}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateGrants(
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestBody Grant grant, HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			this.grantAcl(grant, repository, workspace);
			if (this.authoringEnabled) {
				grantAcl(grant, repository, WcmConstants.DRAFT_WS);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (WcmRepositoryException e) {
			logger.error("Failed to update grant", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to update grant", t);
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.UPDATE_GRANTS_ERROR, null));
		}	
	}
	
	@GetMapping(path = "/grant/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Grant> getAcls(		
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam(name="path") String wcmPath,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Session session = this.repositoryManager.getSession(repository, workspace);
			AccessControlManager acm = session.getAccessControlManager();
			Grant grant = this.doGetAcl(session, acm, wcmPath);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(grant);
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get acl", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get acl", t);
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.GET_ACL_ERROR, null));
		}		
	}
	
	@GetMapping(path = "/library-grant/{repository}/{workspace}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Grant>> getLibraryAcls(		
			@PathVariable("repository") String repository,
			@PathVariable("workspace") String workspace, 
			@RequestParam(name="path") String wcmPath, 
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Map<String, Grant> libraryGrants = new HashMap<>();
			Session session = this.repositoryManager.getSession(WcmConstants.BPWIZARD_REPO, WcmConstants.DEFAULT_WS);
			
			AccessControlManager acm = session.getAccessControlManager();
			libraryGrants.put("library", this.doGetAcl(session, acm, wcmPath));
			libraryGrants.put("renderTemplate", this.doGetAcl(session, acm, String.format("%s/renderTemplate", wcmPath)));
			libraryGrants.put("contentItem", this.doGetAcl(session, acm, String.format("%s/rootSiteArea", wcmPath)));
			libraryGrants.put("authoringTemplate", this.doGetAcl(session, acm, String.format("%s/authoringTemplate", wcmPath)));
			libraryGrants.put("contentAreaLayout", this.doGetAcl(session, acm, String.format("%s/contentAreaLayout", wcmPath)));
			libraryGrants.put("category", this.doGetAcl(session, acm, String.format("%s/category", wcmPath)));
			libraryGrants.put("workflow", this.doGetAcl(session, acm, String.format("%s/workflow", wcmPath)));
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(libraryGrants);
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get library acl", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get library acl", t);
			throw new WcmRepositoryException(t, WcmError.UNEXPECTED_ERROR);
		}	
	}
	
	@GetMapping(path = "/groups", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String[]> getGroups(
			@RequestParam(name="filter", defaultValue = "") String filter,
		    @RequestParam(name="sort", defaultValue = "asc") 
			@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
			String sortDirection,
		    @RequestParam(name="pageIndex", defaultValue = "-1") int pageIndex,
		    @RequestParam(name="pageSize", defaultValue = "10") @Min(3) @Max(100) int pageSize,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {
			Sort sort = "asc".equals(sortDirection) ? Sort.by("name").ascending() : Sort.by("name").descending();
			Pageable pageable = (pageIndex < 0) ? pageable = UnpagedSort.of(sort) : PageRequest.of(pageIndex, pageSize, sort); 
			String roles[] = this.roleService.findAllRoleNames(pageable).stream().toArray(String[]::new);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.OK).body(roles);
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get grroup", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get grroup", t);
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.GET_GROUPS_ERROR, null));
		}	
	}
	
	@GetMapping(path = "/users", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String[]> getUsers(
			@RequestParam(name="filter", defaultValue = "") String filter,
		    @RequestParam(name="sort", defaultValue = "asc") 
			@ValidateString(acceptedValues={"asc", "desc"}, message="Sort order can only be asc or desc")
			String sortDirection,
		    @RequestParam(name="pageIndex", defaultValue = "-1") int pageIndex,
		    @RequestParam(name="pageSize", defaultValue = "10") @Min(3) @Max(100) int pageSize,
			HttpServletRequest request) 
			throws WcmRepositoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry");
		}
		try {	
			String users[] = null;
			Sort sort = "asc".equals(sortDirection) ? Sort.by("name").ascending() : Sort.by("name").descending();
			Pageable pageable = (pageIndex < 0) ? pageable = UnpagedSort.of(sort) : PageRequest.of(pageIndex, pageSize, sort);
			this.userService.findAllNames(pageable).stream().toArray(String[]::new);
			if (logger.isDebugEnabled()) {
				logger.debug("Exit");
			}
			return ResponseEntity.status(HttpStatus.OK).body(users);
				
		} catch (WcmRepositoryException e) {
			logger.error("Failed to get user", e);
			throw e;
		} catch (Throwable t) {
			logger.error("Failed to get user", t);
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.GET_USERS_ERROR, null));
		}	
	}
	
	private void grantAcl(Grant grant, String repository, String workspace) {
		String absPath = String.format(grant.getWcmPath().startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, grant.getWcmPath());
		try {
			Session session = this.repositoryManager.getSession(repository, workspace);
			AccessControlManager acm = session.getAccessControlManager();
			AccessControlList acl = null;
			AccessControlPolicyIterator it = acm.getApplicablePolicies(absPath);
			if (it.hasNext()) {
			    acl = (AccessControlList)it.nextAccessControlPolicy();
			} else {
			    acl = (AccessControlList)acm.getPolicies(absPath)[0];
			}
			for (AccessControlEntry ace: acl.getAccessControlEntries()) {
				acl.removeAccessControlEntry(ace);
			}
		
			for (WcmPermission permission: grant.getPermissions()) {
				String[] priviledgeNames = this.getAggregatedWcmPrivilidges(permission);
				Principal principal = this.getPrincipal(permission);
				Privilege[] priviledges = new Privilege[priviledgeNames.length];
				for (int i = 0; i < priviledgeNames.length; i++) {
					priviledges[i] = acm.privilegeFromName(priviledgeNames[i]);
				}
				acl.addAccessControlEntry(principal, priviledges);
			}
			
			acm.setPolicy(absPath, acl);
			session.save();
		} catch (Throwable t) {
			throw new WcmRepositoryException(t, new WcmError(t.getMessage(), WcmErrors.GRANT_ACL_ERROR, null));
		}	
	}

	private Principal getPrincipal(WcmPermission permission) {
		return  "PRINCIPAL_TYPE_GROUP".equals(permission.getPrincipalType()) ? new WcmGroup(permission.getPrincipalId()) : new WcmPrincipal(permission.getPrincipalId());
	}

	private String[] getAggregatedWcmPrivilidges(WcmPermission permission) {
		String[] priviledges = null;
		for (String role: permission.getWcmRoles()) {
			if (role.equals(ROLE_ADMINISTRATOR)) {
				priviledges = adminPrivileges;
				break;
			}
			if (role.equals(ROLE_EDITOR)) {
				priviledges = editorPrivileges;
			}
			priviledges = (priviledges == null) ? readPrivileges : priviledges;
		}
		return priviledges;
		
	}
	
	private Grant doGetAcl(Session session, AccessControlManager acm, String wcmPath ) throws RepositoryException {
		String absPath = String.format(wcmPath.startsWith("/") ? WcmConstants.NODE_ROOT_PATH_PATTERN : WcmConstants.NODE_ROOT_REL_PATH_PATTERN, wcmPath);
		AccessControlList acl = null;
		AccessControlPolicyIterator it = acm.getApplicablePolicies(absPath);
		if (it.hasNext()) {
		    acl = (AccessControlList)it.nextAccessControlPolicy();
		} else {
		    acl = (AccessControlList)acm.getPolicies(absPath)[0];
		}
		List<WcmPermission> permissions = new ArrayList<>();
		List<String> wcmRoles = new ArrayList<>();
		for (AccessControlEntry aclEntry: acl.getAccessControlEntries()) {
			Principal principal = aclEntry.getPrincipal();
			WcmPermission wcmPermission = new WcmPermission();
			wcmPermission.setPrincipalId(principal.getName());
			wcmPermission.setPrincipalType((principal instanceof Group) ? PRINCIPAL_TYPE_GROUP : PRINCIPAL_TYPE_USER);
			Privilege[] privileges = aclEntry.getPrivileges();
			wcmRoles.clear();
			for (Privilege privilege: privileges) {
				if (JCR_ALL.equals(privilege.getName()) || Privilege.JCR_REMOVE_NODE.equals(privilege.getName())) {
					wcmRoles.add(ROLE_ADMINISTRATOR);
					break;
				}
				if (JCR_WRITE.equals(privilege.getName())) {
					wcmRoles.add(ROLE_EDITOR);
					break;
				}
				if (JCR_READ.equals(privilege.getName())) {
					wcmRoles.add(ROLE_VIEWER);
					break;
				}
			}
			wcmPermission.setWcmRoles(wcmRoles.toArray(new String[wcmRoles.size()]));
			permissions.add(wcmPermission);
		}

		Grant grant = new Grant();
		grant.setWcmPath(wcmPath);
		grant.setPermissions(permissions.toArray(new WcmPermission[permissions.size()]));
		return grant;
	}
	
}
