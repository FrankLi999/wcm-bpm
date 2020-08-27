package com.bpwizard.wcm.repo.rest.bpm.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.spring.boot.commons.security.SpringPrincipal;
import com.bpwizard.spring.boot.commons.service.repo.domain.User;
import com.bpwizard.spring.boot.commons.service.repo.domain.UserRepository;
import com.bpwizard.spring.boot.commons.service.repo.exception.ResourceNotFoundException;
import com.bpwizard.wcm.repo.annotations.CurrentUser;
import com.bpwizard.wcm.repo.rest.RestHelper;
import com.bpwizard.wcm.repo.rest.WcmUtils;
import com.bpwizard.wcm.repo.rest.bpm.model.Resource;
import com.bpwizard.wcm.repo.rest.bpm.model.BpmApplication;
import com.bpwizard.wcm.repo.rest.bpm.model.BpmApplications;
import com.bpwizard.wcm.repo.rest.bpm.model.BpmLink;
import com.bpwizard.wcm.repo.rest.bpm.model.BpmLinks;
import com.bpwizard.wcm.repo.rest.bpm.model.UserProfile;
import com.bpwizard.wcm.repo.rest.jcr.controllers.BaseWcmRestController;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmError;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;
import com.bpwizard.wcm.repo.rest.modeshape.model.RestProperty;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.bpwizard.wcm.repo.rest.utils.WcmErrors;

@RestController
@RequestMapping(BpmContentRestController.BASE_URI)
@Validated
public class BpmContentRestController extends BaseWcmRestController {
	private static final Logger logger = LogManager.getLogger(BpmContentRestController.class);

	public static final String BASE_URI = "/bpm/content";

	// public static final String SITE_AREA_PATTERN = "/camunda/rootSiteArea/%s";
	public static final String REPOSITITORY = "bpwizard";
	public static final String WORKSPACE = "default";
    @Autowired
    private UserRepository userRepository;
    
	@GetMapping(path = "/application", produces = MediaType.APPLICATION_JSON_VALUE)
	public BpmApplications getBpmApplications(
			@RequestParam(name="wcmPath") final String wcmPath,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			//String siteAreaPath = String.format(SITE_AREA_PATTERN, "home/applications");
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, REPOSITITORY, WORKSPACE,
					WcmUtils.nodePath(wcmPath), WcmConstants.SITE_AREA_DEPTH);
			BpmApplications bpmApplications = new BpmApplications();

			for (RestNode childNode : saNode.getChildren()) {
				if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())) {
					for (RestProperty property : childNode.getJcrProperties()) {
						if ("bpw:title".equals(property.getName())) {
							bpmApplications.setTitle(property.getValues().get(0));
						} else if ("bpw:description".equals(property.getName())) {
							bpmApplications.setDescription(property.getValues().get(0));
						}
					}
					break;
				}
			}
			List<BpmApplication> applications = saNode.getChildren().stream()
					.filter(node -> WcmUtils.checkNodeType(node, "bpw:design_MyApplication_AT"))
					.map(this::toBpmApplication).collect(Collectors.toList());
			bpmApplications.setBpmApplications(applications);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return bpmApplications;
		} catch (RepositoryException ex) {
			logger.error(ex);
			throw new WcmRepositoryException(ex, WcmError.createWcmError(ex.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		}
	}

	@GetMapping(path = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
	public BpmLinks getBpmLinks(
			@RequestParam(name="wcmPath") final String wcmPath,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			// String siteAreaPath = String.format(SITE_AREA_PATTERN, "home/links");
			RestNode saNode = (RestNode) this.itemHandler.item(baseUrl, REPOSITITORY, WORKSPACE,
					WcmUtils.nodePath(wcmPath), WcmConstants.SITE_AREA_DEPTH);
			BpmLinks bpmLinks = new BpmLinks();

			for (RestNode childNode : saNode.getChildren()) {
				if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())) {
					for (RestProperty property : childNode.getJcrProperties()) {
						if ("bpw:title".equals(property.getName())) {
							bpmLinks.setTitle(property.getValues().get(0));
						} else if ("bpw:description".equals(property.getName())) {
							bpmLinks.setDescription(property.getValues().get(0));
						}
					}
					break;
				}
			}
			List<BpmLink> links = saNode.getChildren().stream()
					.filter(node -> WcmUtils.checkNodeType(node, "bpw:design_MyLink_AT")).map(this::toBpmLink)
					.collect(Collectors.toList());
			bpmLinks.setBpmLinks(links);
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return bpmLinks;
		} catch (RepositoryException ex) {
			logger.error(ex);
			throw new WcmRepositoryException(ex, WcmError.createWcmError(ex.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		}
	}

	@GetMapping(path = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserProfile userProfile(@CurrentUser SpringPrincipal userPrincipal) {
		UserProfile userProfile = new UserProfile();
		// Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.currentUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.currentUser().getId()));
		userProfile.setName(user.getName());
		userProfile.setEmail(user.getEmail());
		List<String> groups = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
		userProfile.setGroups(groups);
		return userProfile;
	}
	
	@GetMapping(path = "/resources", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Resource> getResources(
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.traceEntry();
		}
		try {
			String baseUrl = RestHelper.repositoryUrl(request);
			// String siteAreaPath = String.format(SITE_AREA_PATTERN, "home/links");
			RestNode resourcesNode = (RestNode) this.itemHandler.item(baseUrl, REPOSITITORY, WORKSPACE,
					WcmUtils.nodePath("/camunda/resources"), WcmConstants.SITE_AREA_DEPTH);
			List<Resource> resources = resourcesNode.getChildren().stream()
					.filter(node -> WcmUtils.checkNodeType(node, "bpw:camunda_resources"))
					.map(this::toResource)
					.collect(Collectors.toList());
			
			if (logger.isDebugEnabled()) {
				logger.traceExit();
			}
			return resources;
		} catch (RepositoryException ex) {
			logger.error(ex);
			throw new WcmRepositoryException(ex, WcmError.createWcmError(ex.getMessage(), WcmErrors.GET_NODE_ERROR, null));
		}
	}
	private Resource toResource(RestNode resourceNode) {
		Resource resource = new Resource();
		for (RestProperty property : resourceNode.getJcrProperties()) {
			if ("bpw:name".equals(property.getName())) {
				resource.setResource(property.getValues().get(0));
			} else if ("bpw:resourceId".equals(property.getName())) {
				resource.setResourceId(Integer.parseInt(property.getValues().get(0)));
			} else if ("bpw:permissions".equals(property.getName())) {
				resource.setPermissions(property.getValues());
			} else if ("bpw:title".equals(property.getName())) {
				resource.setTitle(property.getValues().get(0));
			} 
		}
		return resource;
	}
	
	// TODO: use bean utils to generalize it
	private BpmApplication toBpmApplication(RestNode contentItemNode) {
		BpmApplication bpmApplication = new BpmApplication();
		bpmApplication.setName(contentItemNode.getName());
		for (RestNode childNode : contentItemNode.getChildren()) {
			if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())
					|| WcmUtils.checkNodeType(childNode, WcmConstants.JCR_TYPE_PROPERTY_FOLDER)) {
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						bpmApplication.setTitle(property.getValues().get(0));
					} else if ("bpw:description".equals(property.getName())) {
						bpmApplication.setDescription(property.getValues().get(0));
					} else if ("bpw:name".equals(property.getName())) {
						bpmApplication.setName(property.getValues().get(0));
					}
				}
			} else if (WcmConstants.WCM_ITEM_ELEMENTS.equals(childNode.getName())) {
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("link".equals(property.getName())) {
						bpmApplication.setLink(property.getValues().get(0));
						break;
					}

				}
			}
		}
		return bpmApplication;
	}

	// TODO: use bean utils to generalize it
	private BpmLink toBpmLink(RestNode contentItemNode) {
		BpmLink bpmLink = new BpmLink();
		bpmLink.setName(contentItemNode.getName());
		for (RestNode childNode : contentItemNode.getChildren()) {
			if (WcmConstants.WCM_ITEM_PROPERTIES.equals(childNode.getName())
					|| WcmUtils.checkNodeType(childNode, WcmConstants.JCR_TYPE_PROPERTY_FOLDER)) {
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("bpw:title".equals(property.getName())) {
						bpmLink.setTitle(property.getValues().get(0));
					} else if ("bpw:description".equals(property.getName())) {
						bpmLink.setDescription(property.getValues().get(0));
					} else if ("bpw:name".equals(property.getName())) {
						bpmLink.setName(property.getValues().get(0));
					}
				}
			} else if (WcmConstants.WCM_ITEM_ELEMENTS.equals(childNode.getName())) {
				for (RestProperty property : childNode.getJcrProperties()) {
					if ("link".equals(property.getName())) {
						bpmLink.setLink(property.getValues().get(0));
						break;
					}
				}
			}
		}
		return bpmLink;
	}
}
